package gms.angus.brsoli.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;

import butterknife.ButterKnife;
import butterknife.OnClick;
import gms.angus.brsoli.R;

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
    }

    private GoogleSignInAccount getUserAccount() {
        return ((GameActivity)getActivity()).userAccount;
    }
}
