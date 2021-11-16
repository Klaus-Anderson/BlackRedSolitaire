package gms.angus.angussoli.view

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.games.Game
import com.google.android.gms.games.Games
import gms.angus.angussoli.R
import gms.angus.angussoli.viewmodel.GameViewModel
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl

class GameActivity : AppCompatActivity(R.layout.activity_game), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        const val RC_SIGN_IN = 87654
        const val RC_ACHIEVEMENT_UI = 87653
    }
    lateinit var gameViewModel: GameViewModel
//    private var apiClient: GoogleApiClient? = null

    //    @JvmField
//    var userAccount: GoogleSignInAccount? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        gameViewModel = ViewModelProvider(
            viewModelStore,
            GameViewModel.GameViewModelFactory(application)
        )[GameViewModelImpl::class.java]
        // get and display preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        if (sharedPreferences.getBoolean(getString(R.string.dark_theme_preference_key), false)) {
            setTheme(R.style.AngusTheme)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.dark_theme_preference_key)) {
            setTheme(
                if (sharedPreferences?.getBoolean(key, false) == true) {
                    R.style.AngusTheme
                } else {
                    R.style.LightTheme
                }
            )
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, GameFragment()).commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
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
}