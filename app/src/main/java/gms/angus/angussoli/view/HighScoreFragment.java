package gms.angus.angussoli.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import gms.angus.angussoli.R;
import gms.angus.angussoli.model.RankedPlayer;

/**
 * Created by Harry Cliff on 11/28/17.
 */

public class HighScoreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_score, container, false);
        ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        return view;
    }

    @OnClick(R.id.high_score)
    void onHighScoreClick(){
        Games.getLeaderboardsClient(getActivity(), getUserAccount()).getLeaderboardIntent(
                getString(R.string.highScore_board_id)).addOnSuccessListener(
                intent -> {
                    startActivityForResult(intent, 0 );
                });
    }

    @OnClick(R.id.total_score)
    void onTotalScoreClick(){
        Games.getLeaderboardsClient(getActivity(), getUserAccount()).getLeaderboardIntent(
                getString(R.string.totalScore_board_id)).addOnSuccessListener(
                intent -> {
                    startActivityForResult(intent, 0 );
                });
    }

    @OnClick(R.id.number_of_games)
    void onNumGamesClick(){
        Games.getLeaderboardsClient(getActivity(), getUserAccount()).getLeaderboardIntent(
                getString(R.string.numOfGame_board_id)).addOnSuccessListener(
                intent -> {
                    startActivityForResult(intent, 0 );
                });
    }

    @OnClick(R.id.average_game_pro_rank)
    void onAvgScoreClick(){
        getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.container, new ProScoreFragment(),
                                 ProScoreFragment.class.getSimpleName())
                            .addToBackStack(ToPlayFragment.class.getSimpleName()).commit();
    }

    @OnClick(R.id.achievements)
    void onAchievementsClick(){
        Games.getAchievementsClient(getActivity(), getUserAccount())
             .getAchievementsIntent()
             .addOnSuccessListener(
                     intent -> startActivityForResult(intent, 0));
    }

    private GoogleSignInAccount getUserAccount() {
        return ((GameActivity)getActivity()).userAccount;
    }

    private List<RankedPlayer> getRankedList(){
        return ((GameActivity)getActivity()).rankingLeaderboardScoreList;
    }
}
