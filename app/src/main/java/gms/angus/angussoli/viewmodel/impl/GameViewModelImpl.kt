package gms.angus.angussoli.viewmodel.impl

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import gms.angus.angussoli.BuildConfig
import gms.angus.angussoli.R
import gms.angus.angussoli.model.*
import gms.angus.angussoli.module.GlideApp
import gms.angus.angussoli.view.GameActivity
import gms.angus.angussoli.viewmodel.GameViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    init {
        updateLiveData(application.applicationContext)
        loadCardBitmapMap(application.applicationContext)
    }

    override fun getCardImageBitmap(card: Card): Bitmap? {
        return cardBitmapMap[card]
    }

    private fun loadCardBitmapMap(context: Context) {
        loadingSpinnerVisibilityLiveData.value = View.VISIBLE

        runBlocking {
            CardValue.values().forEach { value ->
                CardSuit.values().forEach { suit ->
                    launch {
                        GlideApp.with(context)
                            .asBitmap()
                            .load(
                                Firebase.storage.reference.child(
                                    String.format("pretty_cards/%s_%s.png", value.identity.lowercase(), suit.identity)
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
                                    loadingSpinnerVisibilityLiveData.value = View.INVISIBLE
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
                                        loadingSpinnerVisibilityLiveData.value = View.GONE
                                    }
                                    return true
                                }

                            })
                            .preload()
                    }
                }
            }
        }
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

//        unlockAchievement(getString(R.string.achievement_break))
    }

    override fun onToPlayClick(view: View) {
//        fragmentManager.beginTransaction()
//            .add(R.id.container, ToPlayFragment(), ToPlayFragment::class.java.simpleName)
//            .addToBackStack(ToPlayFragment::class.java.simpleName).commit()
    }

    override fun onNewGameClick(view: View) {
        gameState = GameState()
        (view.context as? Activity)?.let {
            it.startActivity(Intent(it, GameActivity::class.java))
            it.finish()
        }
    }
}