package gms.angus.angussoli.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import gms.angus.angussoli.R
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.RankedPlayer
import java.util.*

class GameActivity : AppCompatActivity(R.layout.activity_game) {

    lateinit var deckFrame: FrameLayout
    lateinit var redFrame: FrameLayout
    lateinit var blackFrame: FrameLayout
    lateinit var discardFrame: FrameLayout
    lateinit var tensLayout: LinearLayout
    lateinit var textView10: TextView
    lateinit var jacksLayout: LinearLayout
    lateinit var textViewJ: TextView
    lateinit var queensLayout: LinearLayout
    lateinit var textViewQ: TextView
    lateinit var kingsLayout: LinearLayout
    lateinit var textViewK: TextView
    lateinit var acesLayout: LinearLayout
    lateinit var textViewA: TextView
    lateinit var ten_clubs: FrameLayout
    lateinit var ten_diamonds: FrameLayout
    lateinit var ten_spades: FrameLayout
    lateinit var ten_hearts: FrameLayout
    lateinit var jack_clubs: FrameLayout
    lateinit var jack_diamonds: FrameLayout
    lateinit var jack_spades: FrameLayout
    lateinit var jack_hearts: FrameLayout
    lateinit var queen_clubs: FrameLayout
    lateinit var queen_diamonds: FrameLayout
    lateinit var queen_spades: FrameLayout
    lateinit var queen_hearts: FrameLayout
    lateinit var king_clubs: FrameLayout
    lateinit var king_diamonds: FrameLayout
    lateinit var king_spades: FrameLayout
    lateinit var king_hearts: FrameLayout
    lateinit var ace_clubs: FrameLayout
    lateinit var ace_diamonds: FrameLayout
    lateinit var ace_spades: FrameLayout
    lateinit var ace_hearts: FrameLayout
    lateinit var pile_clubs: FrameLayout
    lateinit var pile_diamonds: FrameLayout
    lateinit var pile_spades: FrameLayout
    lateinit var pile_hearts: FrameLayout
    lateinit var deckText: TextView
    lateinit var redDiscardText: TextView
    lateinit var blackDiscardText: TextView
    lateinit var scoreText: TextView
    lateinit var pileText: TextView
    lateinit var levelText: TextView
    lateinit var rankRow: LinearLayout
    lateinit var averageText: TextView
    lateinit var cardsLeftText: TextView
    lateinit var clubsText: TextView
    lateinit var diamondsText: TextView
    lateinit var spadesText: TextView
    lateinit var heartsText: TextView
    lateinit var topCard: ImageView

    private val RC_SIGN_IN = 111111
//    private var apiClient: GoogleApiClient? = null

//    @JvmField
//    var userAccount: GoogleSignInAccount? = null
    private var progress: ProgressDialog? = null
    private var deck = Stack<Card>()
    private var colorFrames = mutableListOf<FrameLayout>()
    private var pileFrames = mutableListOf<FrameLayout>()
    private var faceFrames = mutableListOf<FrameLayout>()
    private var remainingCards = mutableListOf<TextView>()
    private var colorCards= mutableListOf<Card?>()
    private var eligibleIndexes= mutableListOf<Int>()

    @JvmField
    var rankingLeaderboardScoreList = mutableListOf<RankedPlayer>()
    private var hasDrawn = false
    private var level = 0
    private var pileTotal = 0
    private var scoreTotal = 0
    private var brokenLevel = -1
//
//    private var totalScore: Long = -1
//    private var numOfGames: Long = -1
//    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        blackFrame = findViewById(R.id.blackFrame)
        redFrame = findViewById(R.id.redFrame)
        colorCards.add(Card.COLOR_BLACK, null)
        colorCards.add(Card.COLOR_RED, null)
        colorFrames.add(Card.COLOR_BLACK, blackFrame)
        colorFrames.add(Card.COLOR_RED, redFrame)
        pileFrames.add(Card.CLUB_SUIT, pile_clubs)
        pileFrames.add(Card.DIAMOND_SUIT, pile_diamonds)
        pileFrames.add(Card.SPADE_SUIT, pile_spades)
        pileFrames.add(Card.HEART_SUIT, pile_hearts)
        faceFrames.add(Card.DIAMOND_SUIT, ten_diamonds)
        faceFrames.add(Card.SPADE_SUIT, ten_spades)
        faceFrames.add(Card.HEART_SUIT, ten_hearts)
        faceFrames.add(4 + Card.CLUB_SUIT, jack_clubs)
        faceFrames.add(4 + Card.DIAMOND_SUIT, jack_diamonds)
        faceFrames.add(4 + Card.SPADE_SUIT, jack_spades)
        faceFrames.add(4 + Card.HEART_SUIT, jack_hearts)
        faceFrames.add(2 * 4 + Card.CLUB_SUIT, queen_clubs)
        faceFrames.add(2 * 4 + Card.DIAMOND_SUIT, queen_diamonds)
        faceFrames.add(2 * 4 + Card.SPADE_SUIT, queen_spades)
        faceFrames.add(2 * 4 + Card.HEART_SUIT, queen_hearts)
        faceFrames.add(3 * 4 + Card.CLUB_SUIT, king_clubs)
        faceFrames.add(3 * 4 + Card.DIAMOND_SUIT, king_diamonds)
        faceFrames.add(3 * 4 + Card.SPADE_SUIT, king_spades)
        faceFrames.add(3 * 4 + Card.HEART_SUIT, king_hearts)
        faceFrames.add(4 * 4 + Card.CLUB_SUIT, ace_clubs)
        faceFrames.add(4 * 4 + Card.DIAMOND_SUIT, ace_diamonds)
        faceFrames.add(4 * 4 + Card.SPADE_SUIT, ace_spades)
        faceFrames.add(4 * 4 + Card.HEART_SUIT, ace_hearts)
        remainingCards.add(clubsText)
        remainingCards.add(diamondsText)
        remainingCards.add(spadesText)
        remainingCards.add(heartsText)
    }

//    private fun submitScore() {
//        if (userAccount != null) {
//            if (scoreTotal >= 20) {
//                val leaderboardsClient = Games.getLeaderboardsClient(
//                    this, userAccount
//                )
//                leaderboardsClient.submitScoreImmediate(
//                    getString(R.string.highScore_board_id), scoreTotal.toLong()
//                )
//                leaderboardsClient.loadCurrentPlayerLeaderboardScore(
//                    getString(R.string.totalScore_board_id),
//                    LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC
//                )
//                    .addOnSuccessListener { leaderboardScoreAnnotatedData: AnnotatedData<LeaderboardScore?> ->
//                        if (leaderboardScoreAnnotatedData.get() != null) {
//                            if (totalScore == -1L) {
//                                totalScore = leaderboardScoreAnnotatedData.get().rawScore
//                            }
//                        } else {
//                            totalScore = 0
//                        }
//                        leaderboardsClient.submitScoreImmediate(
//                            getString(R.string.totalScore_board_id), totalScore + scoreTotal
//                        )
//                    }.addOnFailureListener { e: Exception? ->
//                        totalScore = 0
//                        leaderboardsClient.submitScoreImmediate(
//                            getString(R.string.totalScore_board_id), totalScore + scoreTotal
//                        )
//                    }
//                leaderboardsClient.loadCurrentPlayerLeaderboardScore(
//                    getString(R.string.numOfGame_board_id),
//                    LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC
//                )
//                    .addOnSuccessListener { leaderboardScoreAnnotatedData: AnnotatedData<LeaderboardScore?> ->
//                        if (leaderboardScoreAnnotatedData.get() != null) {
//                            if (numOfGames == -1L) {
//                                numOfGames = leaderboardScoreAnnotatedData.get().rawScore
//                            }
//                        } else {
//                            numOfGames = 0
//                        }
//                        leaderboardsClient.submitScoreImmediate(
//                            getString(R.string.numOfGame_board_id), numOfGames + 1
//                        )
//                    }.addOnFailureListener { e: Exception? ->
//                        numOfGames = 0
//                        leaderboardsClient.submitScoreImmediate(
//                            getString(R.string.totalScore_board_id), numOfGames + 1
//                        )
//                    }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                userAccount = task.getResult(ApiException::class.java)
//            } catch (e: ApiException) {
//                // The ApiException status code indicates the detailed failure reason.
//                // Please refer to the GoogleSignInStatusCodes class reference for more information.
//                Log.w(GameActivity::class.java.simpleName, "signInResult:failed code=" + e.statusCode)
//            }
//            hideLoadingDialog()
//        }
//    }

//    @OnClick(R.id.high_scores_button)
//    fun onHighScoresClick() {
//        if (userAccount != null) {
//            showLoadingDialog()
//            val leaderboardsClient = Games.getLeaderboardsClient(this, userAccount)
//            leaderboardsClient.loadCurrentPlayerLeaderboardScore(
//                getString(R.string.numOfGame_board_id),
//                LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC
//            )
//                .addOnSuccessListener { leaderboardScoreAnnotatedData: AnnotatedData<LeaderboardScore?> ->
//                    if (leaderboardScoreAnnotatedData.get() != null &&
//                        leaderboardScoreAnnotatedData.get().rawScore >= 10
//                    ) {
//                        displayName = leaderboardScoreAnnotatedData.get().scoreHolder.displayName
//                        val numOfGamesAnnotatedObservable = getPlayerRanking(
//                            leaderboardsClient,
//                            getString(R.string.numOfGame_board_id)
//                        )
//                        val totalScoreAnnotatedObservable = getPlayerRanking(
//                            leaderboardsClient,
//                            getString(
//                                R.string.totalScore_board_id
//                            )
//                        )
//                        val obs = Observable.zip(
//                            numOfGamesAnnotatedObservable, totalScoreAnnotatedObservable
//                        ) { observable1: Observable<Pair<Map<String, Long>, String>>?, observable2: Observable<Pair<Map<String, Long>, String>>? ->
//                            Observable.zip(observable1, observable2,
//                                BiFunction<Pair<Map<String, Long>, String>, Pair<Map<String, Long>, String>, List<RankedPlayer>> { pair1: Pair<Map<String, Long>, String>, pair2: Pair<Map<String, Long>, String> ->
//                                    rankingLeaderboardScoreList = ArrayList()
//                                    for ((scoreHolderDisplayName, value) in pair1.first) {
//                                        var totalScore: Long
//                                        var totalGames: Long
//                                        if (pair1.second == getString(R.string.numOfGame_board_id)) {
//                                            totalGames = value
//                                            totalScore = pair2.first[scoreHolderDisplayName]
//                                        } else {
//                                            totalScore = value
//                                            totalGames = pair2.first[scoreHolderDisplayName]
//                                        }
//                                        if (totalGames > 9) {
//                                            rankingLeaderboardScoreList.add(
//                                                RankedPlayer(scoreHolderDisplayName, totalScore / totalGames)
//                                            )
//                                        }
//                                    }
//                                    rankingLeaderboardScoreList
//                                })
//                        }
//                        disposables.add(
//                            obs // Run on a background thread
//                                .subscribeOn(Schedulers.io()) // Be notified on the main thread
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribeWith(object : DisposableObserver<Observable<List<RankedPlayer?>?>?>() {
//                                    override fun onNext(listObservable: Observable<List<RankedPlayer?>?>) {
//                                        disposables.add(
//                                            listObservable // Run on a background thread
//                                                .subscribeOn(Schedulers.io()) // Be notified on the main thread
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribeWith(object : DisposableObserver<List<RankedPlayer?>?>() {
//                                                    override fun onNext(rankedPlayers: List<RankedPlayer?>) {
//                                                        Collections.sort(
//                                                            rankedPlayers
//                                                        ) { o1: RankedPlayer, o2: RankedPlayer -> (o2.avgGame * 10000 - o1.avgGame * 10000).toInt() }
//                                                        fragmentManager.beginTransaction()
//                                                            .add(
//                                                                R.id.container, HighScoreFragment(),
//                                                                HighScoreFragment::class.java.simpleName
//                                                            )
//                                                            .addToBackStack(HighScoreFragment::class.java.simpleName)
//                                                            .commit()
//                                                        hideLoadingDialog()
//                                                    }
//
//                                                    override fun onError(e: Throwable) {}
//                                                    override fun onComplete() {}
//                                                })
//                                        )
//                                    }
//
//                                    override fun onError(e: Throwable) {}
//                                    override fun onComplete() {}
//                                })
//                        )
//                    } else {
//                        hideLoadingDialog()
//                        AlertDialog.Builder(this)
//                            .setMessage(
//                                """
//                        Play 10 games to unlock High Scores!
//                        Number of games played: ${if (leaderboardScoreAnnotatedData.get() != null) leaderboardScoreAnnotatedData.get().rawScore else 0}
//                        """.trimIndent()
//                            )
//                            .setCancelable(true).show()
//                    }
//                }.addOnFailureListener { e: Exception ->
//                    hideLoadingDialog()
//                    AlertDialog.Builder(this)
//                        .setMessage(
//                            """
//                    ${e.message}
//                    cause:${e.cause}
//                    """.trimIndent()
//                        )
//                        .setCancelable(true).show()
//                }
//        }
//    }
//
//    private fun getPlayerRanking(
//        leaderboardsClient: LeaderboardsClient, leaderboardID: String
//    ): Observable<Observable<Pair<Map<String, Long>, String>>> {
//        return Observable.create { observableEmitter: ObservableEmitter<Observable<Pair<Map<String, Long>, String>>> ->
//            leaderboardsClient.loadTopScores(
//                leaderboardID, LeaderboardVariant.TIME_SPAN_ALL_TIME,
//                LeaderboardVariant.COLLECTION_PUBLIC, 25
//            ).addOnSuccessListener { leaderboardScoresAnnotatedData: AnnotatedData<LeaderboardScores> ->
//                observableEmitter.onNext(
//                    getMoreScores(
//                        leaderboardsClient, leaderboardID,
//                        leaderboardScoresAnnotatedData.get().scores, HashMap()
//                    )
//                )
//            }
//                .addOnFailureListener { e: Exception? -> observableEmitter.onError(e) }
//        }
//    }
//
//    private fun getMoreScores(
//        leaderboardsClient: LeaderboardsClient,
//        leaderboardID: String, buffer: LeaderboardScoreBuffer, leaderboardMap: MutableMap<String, Long>
//    ): Observable<Pair<Map<String, Long>, String>> {
//        return Observable.create { observableEmitter: ObservableEmitter<Pair<Map<String, Long>, String>> ->
//            var i = 0
//            var shouldLoop = true
//            try {
//                buffer[i]
//            } catch (e: IllegalStateException) {
//                shouldLoop = false
//            }
//            while (shouldLoop) {
//                val score = buffer[i]
//                leaderboardMap[score.scoreHolderDisplayName] = score.rawScore
//                i++
//                try {
//                    buffer[i]
//                } catch (e: IllegalStateException) {
//                    shouldLoop = false
//                }
//            }
//            if (i == 26) {
//                leaderboardsClient.loadMoreScores(buffer, 25, PageDirection.NEXT)
//                    .addOnSuccessListener { leaderboardScoresAnnotatedData: AnnotatedData<LeaderboardScores> ->
//                        val newBuffer = leaderboardScoresAnnotatedData.get().scores
//                        getMoreScores(leaderboardsClient, leaderboardID, newBuffer, leaderboardMap)
//                    }.addOnFailureListener { e: Exception -> Log.e(GameActivity::class.java.simpleName, e.message, e) }
//            } else {
//                observableEmitter.onNext(Pair.create(leaderboardMap, leaderboardID))
//            }
//        }
//    }
//
//    private fun unlockAchievement(achievementString: String) {
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                .unlock(achievementString)
//        }
//    }



    private fun increaseLevel(isBroken: Boolean) {
        if (level == 1) {
            levelText.setText(R.string.queen)
            if (!isBroken) {
                jacksLayout.background = resources.getDrawable(R.drawable.face_number_shape)
//                unlockAchievement(getString(R.string.achievement_jacks))
            } else {
                jacksLayout.background = resources.getDrawable(R.drawable.broken_shape)
            }
            queensLayout.background = resources.getDrawable(R.drawable.current_level_shape)
        } else if (level == 2) {
            levelText.setText(R.string.king)
            if (!isBroken) {
                queensLayout.background = resources.getDrawable(R.drawable.face_number_shape)
//                unlockAchievement(getString(R.string.achievement_queens))
            } else {
                queensLayout.background = resources.getDrawable(R.drawable.broken_shape)
            }
            kingsLayout.background = resources.getDrawable(R.drawable.current_level_shape)
        } else if (level == 3) {
            levelText.setText(R.string.ace)
            if (!isBroken) {
                kingsLayout.background = resources.getDrawable(R.drawable.face_number_shape)
//                unlockAchievement(getString(R.string.achievement_kings))
            } else {
                kingsLayout.background = resources.getDrawable(R.drawable.broken_shape)
            }
            acesLayout.background = resources.getDrawable(R.drawable.current_level_shape)
        } else if (level == 4) {
            levelText.setText(R.string.god_Tier)
            if (!isBroken) {
                acesLayout.background = resources.getDrawable(R.drawable.face_number_shape)
//                unlockAchievement(getString(R.string.achievement_aces))
                if (brokenLevel != -1) {
//                    unlockAchievement(getString(R.string.achievement_improbable))
                }
            } else {
                acesLayout.background = resources.getDrawable(R.drawable.broken_shape)
            }
        }
        if (isBroken) {
            var frameCount = 0
            for (i in level * 4 until level * 4 + 4) {
                if (faceFrames[i].childCount != 0 &&
                    pileFrames[i % 4].childCount == 0
                ) {
                    faceFrames[i].removeAllViews()
                    val face_down = ImageView(this)
                    face_down.setImageResource(R.drawable.card_back)
                    faceFrames[i].addView(face_down)
                }
                if (pileFrames[i % 4].childCount == 0) {
                    frameCount++
                }
            }
            if (frameCount == 3) {
//                unlockAchievement(getString(R.string.achievement_well_played))
            }
        }
        level++
        findEligibleFaces()
    }

    private fun isFrameCardSet(frame: FrameLayout?): Boolean {
        return frame!!.getChildAt(0) != null
    }

    private fun colorFrameDiscardClick(colorFrame: FrameLayout) {
        var dummy = colorFrame.getChildAt(0) as ImageView
        colorFrame.removeView(dummy)
        discardFrame.addView(dummy)
        dummy = deckFrame.getChildAt(1) as ImageView
        deckFrame.removeView(dummy)
        colorFrame.addView(dummy)
        redDiscardText.visibility = View.INVISIBLE
        blackDiscardText.visibility = View.INVISIBLE
        if (deck.peek().isBlack) {
            colorCards[Card.COLOR_BLACK] = deck.pop()
            cardsLeftText.text = deck.size.toString()
        } else {
            colorCards[Card.COLOR_RED] = deck.pop()
            cardsLeftText.text = deck.size.toString()
        }
        setHasDrawn(false)
        findEligibleFaces()
        emptyDeckCheck()
    }

    private fun emptyDeckCheck() {
        if (deck.size == 0) {
            topCard.visibility = View.INVISIBLE
            deckFrame.isClickable = false
            deckFrame.visibility = View.GONE
            deckText.text = "Press New Game or close app to submit your score"
        }
    }

    private fun setUsedFaceCard(frame: FrameLayout, suit: Int, localizedIndex: Int) {
        frame.isClickable = false
        frame.background = resources.getDrawable(R.drawable.face_cover_shape)
        val suitImage = ImageView(applicationContext)
        if (suit == Card.CLUB_SUIT) suitImage.setImageResource(R.drawable.club) else if (suit == Card.DIAMOND_SUIT) suitImage.setImageResource(
            R.drawable.diamond
        ) else if (suit == Card.SPADE_SUIT) suitImage.setImageResource(R.drawable.spades) else suitImage.setImageResource(
            R.drawable.heart
        )
        frame.addView(suitImage)

        // set to null so that it won't be playable
        faceFrames[localizedIndex]
    }

    private fun setHasDrawn(hasDrawn: Boolean) {
        if (hasDrawn) {
            deckText.setText(R.string.press_to_discard)
        } else {
            deckText.setText(R.string.press_to_draw)
        }
        this.hasDrawn = hasDrawn
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) fragmentManager.popBackStackImmediate() else super.onBackPressed()
    }

//    override fun onConnected(bundle: Bundle?) {
//        if (apiClient.isConnected) {
//            Auth.GoogleSignInApi.silentSignIn(apiClient).setResultCallback { result: GoogleSignInResult ->
//                if (result.isSuccess) {
//                    userAccount = result.signInAccount
//                    hideLoadingDialog()
//                } else {
//                    // Player will need to sign-in explicitly using via UI
//                    val intent = Auth.GoogleSignInApi.getSignInIntent(apiClient)
//                    startActivityForResult(intent, RC_SIGN_IN)
//                }
//            }
//        }
//    }
//
//    override fun onConnectionSuspended(i: Int) {
//        hideLoadingDialog()
//    }

//    private fun showLoadingDialog() {
//        progress = ProgressDialog(this)
//        progress.setTitle("Loading")
//        progress.setMessage("Wait while loading...")
//        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
//        progress.show()
//    }

//    private fun hideLoadingDialog() {
//        progress.hide()
//    }
}