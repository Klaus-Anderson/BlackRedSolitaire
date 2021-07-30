package gms.angus.angussoli.view

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.AnnotatedData
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.LeaderboardsClient.LeaderboardScores
import com.google.android.gms.games.PageDirection
import com.google.android.gms.games.leaderboard.LeaderboardScore
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import gms.angus.angussoli.R
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.RankedPlayer
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

class GameActivity : Activity() {

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
        setContentView(R.layout.activity_game)
        ButterKnife.bind(this)
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//            .requestEmail()
//            .build()
//        apiClient = GoogleApiClient.Builder(this)
//            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//            .addConnectionCallbacks(this)
//            .build()
//        apiClient.connect()
//        showLoadingDialog()
        deck = Stack()
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
        setHasDrawn(false)
        pileTotal = 0
        scoreTotal = 0
        level = 1
        eligibleIndexes = ArrayList()
        for (i in 0..3) {
            for (j in 2..14) {
                deck.push(Card(j, i))
            }
        }
        Collections.shuffle(deck)
        for (i in colorFrames.indices) {
            colorFrames.get(i).setOnClickListener { v: View ->
                // colorFrames.get(0) == blackFrame
                // colorFrames.get(1) == redFrame
                if (hasDrawn) {
                    val frame = v as FrameLayout
                    if (i == Card.COLOR_BLACK && deck.peek().isBlack) {
                        colorFrameDiscardClick(frame)
                    } else if (i == 1 && deck.peek().isRed) {
                        colorFrameDiscardClick(frame)
                    }
                }
                emptyDeckCheck()
            }
        }
        for (i in faceFrames.indices) {
            faceFrames.get(i)
                .setOnClickListener { v: View ->
                    if (!hasDrawn) {
                        val suit = i % 4
                        val faceFrame = v as FrameLayout
                        val pileFrame = pileFrames.get(suit)
                        val sameColorCard = colorCards.get(suit % 2)
                        val otherColorCard = colorCards.get((suit + 1) % 2)

                        // use Face Card as Face Card
                        if (sameColorCard != null && sameColorCard.suit == suit && otherColorCard != null && faceFrame.getChildAt(
                                0
                            ) != null && !isFrameCardSet(pileFrame) && (i < 4 || i / 4 <= level)
                        ) {
                            val dummy = faceFrame.getChildAt(0) as ImageView
                            faceFrame.removeView(dummy)
                            pileFrame.addView(dummy)
                            blackFrame.removeAllViews()
                            redFrame.removeAllViews()
                            pileTotal = pileTotal + colorCards.get(Card.COLOR_BLACK)!!.value +
                                    colorCards.get(Card.COLOR_RED)!!.value
                            pileText.text = pileTotal.toString()
                            colorCards.set(Card.COLOR_BLACK, null)
                            colorCards.set(Card.COLOR_RED, null)

                            // pile check
                            if (isFrameCardSet(pile_clubs) && isFrameCardSet(pile_diamonds) &&
                                isFrameCardSet(pile_spades) && isFrameCardSet(pile_hearts)
                            ) {
                                scoreTotal = scoreTotal +
                                        pileTotal * (level - if (brokenLevel != -1) 1 else 0)
//                                submitScore()
//                                if (scoreTotal >= 75) {
//                                    unlockAchievement(getString(R.string.achievement_75_points))
//                                    if (scoreTotal >= 100) {
//                                        unlockAchievement(getString(R.string.achievement_100_points))
//                                        if (scoreTotal >= 150) {
//                                            unlockAchievement(getString(R.string.achievement_150_points))
//                                            if (scoreTotal >= 200) {
//                                                unlockAchievement(getString(R.string.achievement_200_points))
//                                                if (scoreTotal >= 300) {
//                                                    unlockAchievement(getString(R.string.achievement_300_points))
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
                                scoreText.text = scoreTotal.toString()
                                pileTotal = 0
                                pileText.text = pileTotal.toString()
                                pile_clubs.removeAllViews()
                                pile_spades.removeAllViews()
                                pile_hearts.removeAllViews()
                                pile_diamonds.removeAllViews()
                                increaseLevel(false)
                            }
                            findEligibleFaces()
                            setUsedFaceCard(faceFrame, suit, i)
                        } else if (isFrameCardSet(faceFrame)
                            && (i < 4 || i / 4 < level)
                        ) {
                            val colorFrame = colorFrames.get(i % 2)
                            var dummy = colorFrame.getChildAt(0) as ImageView
                            if (dummy != null) {
                                colorFrame.removeView(dummy)
                                discardFrame.addView(dummy)
                            }
                            dummy = faceFrame.getChildAt(0) as ImageView
                            faceFrame.removeView(dummy)
                            colorFrame.addView(dummy)
                            colorCards.set(
                                suit % 2,
                                Card(Card.TEN_VALUE, suit)
                            )
                            findEligibleFaces()
                            setUsedFaceCard(faceFrame, suit, i)
                        }
                    }
                }
        }
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

    @OnClick(R.id.deckFrame)
    fun onDeckClick(v: View?) {
        if (deck.size != 0 && !hasDrawn) {
            setHasDrawn(true)
            var newCount: Int
            newCount = (remainingCards[deck.peek().suit].text.toString() + "").toInt() - 1
            val drawnCard = ImageView(this@GameActivity)
            val drawableName = deck.peek().imageName
            val drawableId = resources.getIdentifier(
                drawableName, "drawable", packageName
            )
            drawnCard.setImageResource(drawableId)
            drawnCard.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            if (deck.peek().value >= Card.TEN_VALUE) {
                val frameIndex = ((deck.peek().value - 10) * 4
                        + deck.peek().suit)
                val moveToFrame = faceFrames[frameIndex]
                if (deck.peek().value != 10 + brokenLevel) {
                    moveToFrame.addView(drawnCard)
                    moveToFrame.isClickable = true
                } else {
                    val face_down = ImageView(this)
                    face_down.setImageResource(R.drawable.card_back)
                    moveToFrame.addView(face_down)
                }
                deck.pop()
                cardsLeftText.text = deck.size.toString()
                if (level < 5) checkIfFaceIsEligible(frameIndex) else moveToFrame.isClickable = false
                setHasDrawn(false)
            } else {
                remainingCards[deck.peek().suit].text = newCount.toString()
                newCount = (cardsLeftText.text.toString() + "").toInt() - 1
                cardsLeftText.text = newCount.toString()
                if (deck.peek().isBlack) {
                    if (colorCards[Card.COLOR_BLACK] == null) {
                        blackFrame.addView(drawnCard)
                        colorCards[Card.COLOR_BLACK] = deck.pop()
                        cardsLeftText.text = deck.size.toString()
                        setHasDrawn(false)
                        findEligibleFaces()
                    } else {
                        deckFrame.addView(drawnCard)
                        blackDiscardText.visibility = View.VISIBLE
                    }
                } else {
                    if (colorCards[Card.COLOR_RED] == null) {
                        redFrame.addView(drawnCard)
                        colorCards[Card.COLOR_RED] = deck.pop()
                        cardsLeftText.text = deck.size.toString()
                        setHasDrawn(false)
                        findEligibleFaces()
                    } else {
                        deckFrame.addView(drawnCard)
                        redDiscardText.visibility = View.VISIBLE
                    }
                }
            }
        } else if (deck.size != 0 && hasDrawn) {
            val dummy = deckFrame.getChildAt(1) as ImageView
            deckFrame.removeView(dummy)
            discardFrame.addView(dummy)
            redDiscardText.visibility = View.INVISIBLE
            blackDiscardText.visibility = View.INVISIBLE
            deck.pop()
            cardsLeftText.text = deck.size.toString()
            setHasDrawn(false)
            findEligibleFaces()
        }
        emptyDeckCheck()
    }

    @OnClick(R.id.breakButton)
    fun onBreakClick(view: View) {
        view.isClickable = false
        view.visibility = View.INVISIBLE
        for (i in 0..3) {
            val index = level * 4 + i
            val frame = faceFrames[level * 4 + i]
            frame.isClickable = false
            faceFrames[index] = frame
        }
        brokenLevel = level
        increaseLevel(true)
//        unlockAchievement(getString(R.string.achievement_break))
    }

    @OnClick(R.id.toPlayButton)
    fun onToPlayClick() {
        fragmentManager.beginTransaction()
            .add(R.id.container, ToPlayFragment(), ToPlayFragment::class.java.simpleName)
            .addToBackStack(ToPlayFragment::class.java.simpleName).commit()
    }

    @OnClick(R.id.newGameButton)
    fun onNewGameClick() {
        val i = Intent(this@GameActivity, GameActivity::class.java)
        startActivity(i)
        finish()
    }

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

    private fun checkIfFaceIsEligible(frameIndex: Int) {
        if (faceFrames[frameIndex] != null && faceFrames[frameIndex].childCount != 0 &&
            faceFrames[frameIndex].isClickable
        ) {
            if (colorCards[Card.COLOR_BLACK] != null && colorCards[Card.COLOR_RED] != null && frameIndex / 4 <= level &&
                frameIndex % 4 == colorCards[frameIndex % 2]!!.suit &&
                pileFrames[frameIndex % 4].childCount == 0) {
                eligibleIndexes.add(frameIndex)
                faceFrames[frameIndex].setBackgroundResource(R.drawable.face_eligible_shape)
            } else if (frameIndex / 4 < level && frameIndex / 4 != brokenLevel) {
                faceFrames[frameIndex].setBackgroundResource(R.drawable.number_eligible_shape)
            }
        }
    }

    private fun findEligibleFaces() {
        if (level < 5) {
            for (i in eligibleIndexes) {
                faceFrames[i].background = null
            }
            eligibleIndexes.clear()
            for (i in 0 until 4 * (level + 1)) {
                checkIfFaceIsEligible(i)
            }
        } else {
            for (i in eligibleIndexes) {
                faceFrames[i].background = null
                faceFrames[i].isClickable = false
            }
            eligibleIndexes.clear()
        }
    }

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