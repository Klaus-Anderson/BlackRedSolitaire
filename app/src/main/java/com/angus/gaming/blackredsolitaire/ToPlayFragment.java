package com.angus.gaming.blackredsolitaire;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToPlayFragment extends Fragment {

    @BindView(R.id.hint_textview)
    TextView hintTextview;

    @BindView(R.id.hint_imageview)
    ImageView hintImageview;

    @BindView(R.id.previousButton)
    Button previousButton;

    @BindView(R.id.nextButton)
    Button nextButton;

    private int hintNumber = 1;

    public ToPlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_play, container, false);
        ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        return view;
    }

    @OnClick(R.id.nextButton)
    void onNextButtonClick(View v){
        if(hintNumber!=9) {
            hintNumber++;
            onButtonClick();
        }
    }

    @OnClick(R.id.previousButton)
    void onPreviousButton(){
        if(hintNumber!=1) {
            hintNumber--;
            onButtonClick();
        }
    }

    private void onButtonClick(){
        //@Todo: put in images for hints
        switch (hintNumber) {
            case 1:
                hintTextview.setText(R.string.hintOne);
                previousButton.setVisibility(View.INVISIBLE);
                break;
            case 2:
                hintTextview.setText(R.string.hintTwo);
                previousButton.setVisibility(View.VISIBLE);
                break;
            case 3:
                hintTextview.setText(R.string.hintThree);
                break;
            case 4:
                hintTextview.setText(R.string.hintFour);
                break;
            case 5:
                hintTextview.setText(R.string.hintFive);
                break;
            case 6:
                hintTextview.setText(R.string.hintSix);
                break;
            case 7:
                hintTextview.setText(R.string.hintSeven);
                break;
            case 8:
                hintTextview.setText(R.string.hintEight);
                nextButton.setVisibility(View.VISIBLE);
                break;
            case 9:
                hintTextview.setText(R.string.hintNine);
                nextButton.setVisibility(View.GONE);
                break;
        }
    }
}
