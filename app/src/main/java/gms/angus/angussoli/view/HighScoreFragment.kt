package gms.angus.angussoli.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.games.Games
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FragmentHighScoreBinding

/**
 * Created by Harry Cliff on 11/28/17.
 */
class HighScoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return FragmentHighScoreBinding.inflate(inflater, container, false).run {
            highScore.setOnClickListener{
                onHighScoreClick()
            }
            totalScore.setOnClickListener{
                onHighScoreClick()
            }
            numberOfGames.setOnClickListener{
                onHighScoreClick()
            }
            averageGameProRank.setOnClickListener{
                onAvgScoreClick()
            }
            achievements.setOnClickListener{
                onAchievementsClick()
            }
            root
        }
    }

    fun onHighScoreClick() {
        Games.getLeaderboardsClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!).getLeaderboardIntent(
            getString(R.string.leaderboard_highest_score)
        ).addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
    }

    fun onTotalScoreClick() {
        Games.getLeaderboardsClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!).getLeaderboardIntent(
            getString(R.string.leaderboard_total_score)
        ).addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
    }

    fun onNumGamesClick() {
        Games.getLeaderboardsClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!).getLeaderboardIntent(
            getString(R.string.leaderboard_number_of_games)
        ).addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
    }

    fun onAvgScoreClick() {
//        activity.fragmentManager.beginTransaction()
//            .replace(
//                R.id.container, ProScoreFragment(),
//                ProScoreFragment::class.java.simpleName
//            )
//            .addToBackStack(ToPlayFragment::class.java.simpleName).commit()
        Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT)
    }

    fun onAchievementsClick() {
        Games.getAchievementsClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!)
            .achievementsIntent
            .addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
    }
}