package gms.angus.angussoli.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gms.angus.angussoli.R;
import gms.angus.angussoli.model.RankedPlayer;

/**
 * Created by Harry Cliff on 11/29/17.
 */

public class ProScoreFragment extends Fragment {

    @BindView(R.id.proRecyclerView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_score, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ProScoreAdapter());
        // Inflate the layout for this fragment
        return view;
    }

    private GoogleSignInAccount getUserAccount() {
        return ((GameActivity)getActivity()).userAccount;
    }

    private List<RankedPlayer> getRankedList(){
        return ((GameActivity)getActivity()).rankingLeaderboardScoreList;
    }

    class ProScoreAdapter extends RecyclerView.Adapter<ProScoreAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.pro_score_view_holder, parent, false);
            return new ProScoreAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.playerName.setText(getRankedList().get(position).getUserName());
            holder.playerRank.setText((position+1)+"");
            DecimalFormat numberFormat = new DecimalFormat("#.###");
            holder.playerAverage.setText(numberFormat.format(
                    getRankedList().get(position).getAvgGame())+"");

            if(getRankedList().get(position).getUserName().equals(getUserAccount().getDisplayName())){
                holder.wrapper.setBackgroundResource(R.drawable.current_level_shape);
            }
        }

        @Override
        public int getItemCount() {
            return getRankedList().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.wrapper)
            RelativeLayout wrapper;

            @BindView(R.id.playerName)
            TextView playerName;

            @BindView(R.id.playerRank)
            TextView playerRank;

            @BindView(R.id.playerAverage)
            TextView playerAverage;


            public ViewHolder(View view) {
                super(view);

                ButterKnife.bind(this, view);
            }
        }
    }
}
