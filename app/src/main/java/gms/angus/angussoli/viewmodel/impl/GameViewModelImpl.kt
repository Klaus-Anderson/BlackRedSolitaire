package gms.angus.angussoli.viewmodel.impl

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import gms.angus.angussoli.BuildConfig
import gms.angus.angussoli.R
import gms.angus.angussoli.model.*
import gms.angus.angussoli.module.GlideApp
import gms.angus.angussoli.view.GameActivity
import gms.angus.angussoli.view.HighScoreFragment
import gms.angus.angussoli.view.PreferencesFragment
import gms.angus.angussoli.view.ToPlayFragment
import gms.angus.angussoli.viewmodel.GameViewModel
import kotlinx.coroutines.*


class GameViewModelImpl(application: Application) : GameViewModel, AndroidViewModel(application) {
    override val redDiscardTextVisibilityLiveData = MutableLiveData(View.INVISIBLE)
    override val blackDiscardTextVisibilityLiveData = MutableLiveData(View.INVISIBLE)
    override val deckTopCardVisibilityLiveData = MutableLiveData(View.VISIBLE)
    override val breakButtonVisibilityLiveData = MutableLiveData(View.VISIBLE)
    override val breakButtonClickableLiveData = MutableLiveData(true)
    override val underDeckTextLiveData = MutableLiveData<String>()
    override val cardLeftTextLiveData = MutableLiveData("52")
    override val cardSquanderedTextLiveData = MutableLiveData("0")
    override val clubNumbersLeftTextLiveData = MutableLiveData(8.toString())
    override val spadeNumbersLeftTextLiveData = MutableLiveData(8.toString())
    override val diamondNumbersLeftTextLiveData = MutableLiveData(8.toString())
    override val heartNumbersLeftTextLiveData = MutableLiveData(8.toString())
    override val deckTopCardLiveData = MutableLiveData<Card?>()
    override val redCardLiveData = MutableLiveData<Card?>()
    override val blackCardLiveData = MutableLiveData<Card?>()
    override val scoreTextLiveData = MutableLiveData(0.toString())
    override val levelTextLiveData = MutableLiveData<String>()
    override val multiplierTextLiveData = MutableLiveData(1.toString())
    override val pileScoreTextLiveData = MutableLiveData(0.toString())
    override val clearedFaceCardsLiveData = MutableLiveData<List<CardValue>>()
    override val currentLevelLiveData = MutableLiveData<CardValue?>()
    override val brokenFaceValueLiveData = MutableLiveData<CardValue?>()
    override val tenZoneLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val jackZoneLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val queenZoneLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val kingZoneLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val aceZoneLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val collectedCardsLiveData = MutableLiveData<Map<CardSuit, Card?>>()
    override val loadingSpinnerVisibilityLiveData = MutableLiveData<Int>()
    private val cardBitmapMap = mutableMapOf<Card, Bitmap>()

    private var gameState: GameState = GameState()
    private var combinedAllTimeScore = 0L
    private var numberOfGamesPlayed = 0L

    init {
        updateLiveData(application.applicationContext)
    }

    override fun getCardImageBitmap(card: Card): Bitmap? {
        return cardBitmapMap[card]
    }

    override fun testAchievements(activity: Activity) {
        GoogleSignIn.getLastSignedInAccount(activity)?.let {
            Games.getAchievementsClient(activity, it).apply {
                if (gameState.getFinalScore() > 500) {
                    unlock(activity.getString(R.string.achievement_500_points))
                }
                if (gameState.getFinalScore() > 750) {
                    unlock(activity.getString(R.string.achievement_750_points))
                }
                if (gameState.getFinalScore() > 900) {
                    unlock(activity.getString(R.string.achievement_900_points))
                }
                if (gameState.getFinalScore() > 1000) {
                    unlock(activity.getString(R.string.achievement_1000_points))
                }
                if (gameState.getFinalScore() > 1100) {
                    unlock(activity.getString(R.string.achievement_1100_points))
                }
                if (gameState.currentLevel == CardValue.QUEEN && gameState.brokenFaceValue == null) {
                    unlock(activity.getString(R.string.achievement_figured_it_out))
                }
                if (gameState.currentLevel == CardValue.KING) {
                    unlock(activity.getString(R.string.achievement_moving_on_up))
                }
                if (gameState.currentLevel == CardValue.ACE) {
                    unlock(activity.getString(R.string.achievement_officially_a_real_player))
                }
                if (gameState.currentLevel == null) {
                    unlock(activity.getString(R.string.achievement_finisher))
                    if (gameState.brokenFaceValue == null) {
                        unlock(activity.getString(R.string.achievement_improbable_but_not_impossible))
                    }
                }
            }
        }
    }

    override fun loadCardBitmapMap(activity: Activity) {
        loadingSpinnerVisibilityLiveData.value = View.VISIBLE
        runBlocking {
            withContext(Dispatchers.IO) {
                // withContext waits for all children coroutines
                val a = async {
                    CardValue.values().forEach { value ->
                        CardSuit.values().forEach { suit ->
                            GlideApp.with(activity)
                                .asBitmap()
                                .load(
                                    Firebase.storage.reference.child(
                                        String.format(
                                            "pretty_cards/%s_%s.png",
                                            value.identity.lowercase(),
                                            suit.identity
                                        )
                                    )
                                )
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .listener(object : RequestListener<Bitmap> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Bitmap>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        Log.e(javaClass.name, e?.message ?: "", e)
                                        return true
                                    }

                                    override fun onResourceReady(
                                        resource: Bitmap?,
                                        model: Any?,
                                        target: Target<Bitmap>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        cardBitmapMap[Card(value, suit)] = resource!!
                                        if (cardBitmapMap.size == 52) {
                                            loadingSpinnerVisibilityLiveData.postValue(View.GONE)
                                        }
                                        return true
                                    }

                                })
                                .preload()
                        }
                    }
                }
                val b = async {
                    signInSilently(activity)
                }
                listOf(
                    a, b
                ).awaitAll()

            }


        }
    }

    private fun signInSilently(activity: Activity) {
        val signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        val account = GoogleSignIn.getLastSignedInAccount(activity as Activity)
        if (GoogleSignIn.hasPermissions(account, *signInOptions.scopeArray)) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            val signedInAccount = account
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            val signInClient = GoogleSignIn.getClient(activity as Activity, signInOptions)
            signInClient
                .silentSignIn()
                .addOnCompleteListener(
                    activity as Activity,
                    OnCompleteListener<GoogleSignInAccount?> { task ->
                        if (task.isSuccessful) {
                            // The signed in account is stored in the task's result.
                            val signedInAccount = task.result
                        } else {
                            // Player will need to sign-in explicitly using via UI.
                            startSignInIntent(activity)
                        }
                    })
        }
    }

    private fun startSignInIntent(activity: Activity) {
        val signInClient = GoogleSignIn.getClient(
            activity as Activity,
            GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        )
        val intent = signInClient.signInIntent
        startActivityForResult(activity, intent, GameActivity.RC_SIGN_IN, null)
    }

    private fun updateLiveData(context: Context) {
        deckTopCardLiveData.value = gameState.deckTopCard.also { deckTopCard ->
            (deckTopCard?.let {
                if (it.cardSuit.isRed) {
                    redDiscardTextVisibilityLiveData.value = View.VISIBLE
                    blackDiscardTextVisibilityLiveData.value = View.INVISIBLE
                } else {
                    redDiscardTextVisibilityLiveData.value = View.INVISIBLE
                    blackDiscardTextVisibilityLiveData.value = View.VISIBLE
                }
                underDeckTextLiveData.value = context.getString(R.string.press_to_discard)
                View.VISIBLE
            } ?: run {
                blackDiscardTextVisibilityLiveData.value = View.INVISIBLE
                redDiscardTextVisibilityLiveData.value = View.INVISIBLE
                if (gameState.deck.size == 0) {
                    deckTopCardVisibilityLiveData.value = View.INVISIBLE
                    underDeckTextLiveData.value = ""
                } else {
                    underDeckTextLiveData.value = context.getString(R.string.press_to_draw)
                }
                View.INVISIBLE
            })
        }
        if (gameState.isBreakButtonEligible()) {
            breakButtonVisibilityLiveData.value = View.VISIBLE
            breakButtonClickableLiveData.value = true
        } else {
            breakButtonVisibilityLiveData.value = View.INVISIBLE
            breakButtonClickableLiveData.value = false
        }

        cardLeftTextLiveData.value = gameState.getCardsLeftText()
        cardSquanderedTextLiveData.value = gameState.getCardSquanderedText()
        levelTextLiveData.value = gameState.getLevelText()
        scoreTextLiveData.value = gameState.getFinalScoreString()
        multiplierTextLiveData.value = gameState.getMultiplierString()
        pileScoreTextLiveData.value = gameState.getPileScoreString()
        spadeNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeftString(CardSuit.SPADE)
        clubNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeftString(CardSuit.CLUB)
        heartNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeftString(CardSuit.HEART)
        diamondNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeftString(CardSuit.DIAMOND)

        currentLevelLiveData.value = gameState.currentLevel
        redCardLiveData.value = gameState.redCard
        blackCardLiveData.value = gameState.blackCard

        //update Face Card state based on
        clearedFaceCardsLiveData.value = gameState.clearedFaceValues
        brokenFaceValueLiveData.value = gameState.brokenFaceValue
        tenZoneLiveData.value = gameState.tenZone
        jackZoneLiveData.value = gameState.jackZone
        queenZoneLiveData.value = gameState.queenZone
        kingZoneLiveData.value = gameState.kingZone
        aceZoneLiveData.value = gameState.aceZone
        collectedCardsLiveData.value = gameState.pileCards
    }

    override fun onDeckFrameClick(view: View) {
        gameState.onDeckTouched()
        updateLiveData(view.context)
    }

    override fun onRedFrameClick(view: View) {
        gameState.discardRedCardIfAble()
        updateLiveData(view.context)
    }

    override fun onBlackFrameClick(view: View) {
        gameState.discardBlackCardIfAble()
        updateLiveData(view.context)
    }

    override fun onFaceCardClick(cardValue: CardValue?, cardSuit: CardSuit): View.OnClickListener {
        return View.OnClickListener { view ->
            cardValue?.let {
                gameState.onFaceCardTouched(cardValue, cardSuit)
                updateLiveData(view.context)
            }
        }
    }

    override fun enableCompleteMode(context: Context) {
        if (BuildConfig.DEBUG) {
            gameState.enableCompleteMode()
            updateLiveData(context)
        }
    }

    override fun onBreakClick(view: View) {
        gameState.breakLevel()
        updateLiveData(view.context)

        GoogleSignIn.getLastSignedInAccount(view.context as Activity)?.let {
            Games.getAchievementsClient(view.context as Activity, it).apply {
                unlock(view.context.getString(R.string.achievement_hmm_whats_this))
                if (gameState.pileCards.values.filterNotNull().size == 3) {
                    unlock(view.context.getString(R.string.achievement_well_played))
                }
            }
        }
    }

    override fun onToPlayClick(view: View) {
        (view.context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.add(
            R.id.fragment_container_view, ToPlayFragment::class.java, null
        )?.addToBackStack(ToPlayFragment::class.simpleName)?.commit()
    }

    override fun onNewGameClick(view: View) {
        gameState = GameState()
        (view.context as? Activity)?.let {
            it.startActivity(Intent(it, GameActivity::class.java))
            it.finish()
        }
    }

    override fun onHighScoreClick(view: View) {
        if (GoogleSignIn.getLastSignedInAccount(view.context) != null) {
            Games.getAchievementsClient(view.context, GoogleSignIn.getLastSignedInAccount(view.context)!!)
                .achievementsIntent
                .addOnSuccessListener { intent: Intent? ->
                    (view.context as Activity).startActivityForResult(intent, 0) }
        } else {
            signInSilently(view.context as Activity)
        }

    }

    override fun onOptionsClick(view: View) {
        (view.context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.add(
            R.id.fragment_container_view, PreferencesFragment::class.java, null
        )?.addToBackStack(PreferencesFragment::class.simpleName)?.commit()
    }

    override fun onThemeChanged(context: Context) {
        gameState = GameState()
        updateLiveData(context)
    }
}