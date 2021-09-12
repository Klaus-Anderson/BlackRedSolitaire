package gms.angus.angussoli.view

import android.app.Fragment

/**
 * Created by Harry Cliff on 11/28/17.
 */
class HighScoreFragment : Fragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_high_score, container, false)
//        ButterKnife.bind(this, view)
//
//        // Inflate the layout for this fragment
//        return view
//    }
//
//    @OnClick(R.id.high_score)
//    fun onHighScoreClick() {
//        Games.getLeaderboardsClient(activity, userAccount).getLeaderboardIntent(
//            getString(R.string.highScore_board_id)
//        ).addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
//    }
//
//    @OnClick(R.id.total_score)
//    fun onTotalScoreClick() {
//        Games.getLeaderboardsClient(activity, userAccount).getLeaderboardIntent(
//            getString(R.string.totalScore_board_id)
//        ).addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
//    }
//
//    @OnClick(R.id.number_of_games)
//    fun onNumGamesClick() {
//        Games.getLeaderboardsClient(activity, userAccount).getLeaderboardIntent(
//            getString(R.string.numOfGame_board_id)
//        ).addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
//    }
//
//    @OnClick(R.id.average_game_pro_rank)
//    fun onAvgScoreClick() {
//        activity.fragmentManager.beginTransaction()
//            .replace(
//                R.id.container, ProScoreFragment(),
//                ProScoreFragment::class.java.simpleName
//            )
//            .addToBackStack(ToPlayFragment::class.java.simpleName).commit()
//    }
//
//    @OnClick(R.id.achievements)
//    fun onAchievementsClick() {
//        Games.getAchievementsClient(activity, userAccount)
//            .achievementsIntent
//            .addOnSuccessListener { intent: Intent? -> startActivityForResult(intent, 0) }
//    }
//
//    private val userAccount: GoogleSignInAccount
//        private get() = (activity as GameActivity).userAccount
}