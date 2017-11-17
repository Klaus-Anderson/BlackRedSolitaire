package com.angus.gaming.blackredsolitaire;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends Activity implements ToPlayFragment.OnValuesSetListener {
    @BindView(R.id.deckFrame)
    FrameLayout deckFrame;
    @OnClick(R.id.deckFrame)
    void onDeckClick(View v){
        if (deck.size() != 0 && !hasDrawn) {
            hasDrawn = true;

            int newCount;
            newCount = Integer.parseInt(remainingCards.get(
                    deck.peek().getSuit()).getText()
                                                + "") - 1;
            remainingCards.get(deck.peek().getSuit()).setText(
                    newCount + "");
            newCount = Integer.parseInt(cardsLeftText.getText() + "") - 1;
            cardsLeftText.setText(newCount + "");

            ImageView drawnCard = new ImageView(GameActivity.this);

            String drawableName = deck.peek().getImageName();
            int drawableId = getResources().getIdentifier(drawableName,
                                                          "drawable", getPackageName());
            drawnCard.setImageResource(drawableId);
            drawnCard.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            if (deck.peek().getValue() > 9) {
                int frameIndex = (deck.peek().getValue() - 10) * 4
                        + deck.peek().getSuit();
                FrameLayout moveToFrame = faceFrames.get(frameIndex);
                moveToFrame.addView(drawnCard);
                moveToFrame.setClickable(true);
                deck.pop();
                hasDrawn = false;
            }
            else {
                if (deck.peek().isBlack()) {
                    if (black == null) {
                        blackFrame.addView(drawnCard);
                        black = deck.peek();
                        deck.pop();
                        hasDrawn = false;
                    } else {
                        deckFrame.addView(drawnCard);
                        blackDiscardText.setVisibility(View.VISIBLE);
                        deckDiscardText.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (red == null) {
                        redFrame.addView(drawnCard);
                        red = deck.peek();
                        deck.pop();
                        hasDrawn = false;
                    } else {
                        deckFrame.addView(drawnCard);
                        redDiscardText.setVisibility(View.VISIBLE);
                        deckDiscardText.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (deck.size() != 0 && hasDrawn) {
            ImageView dummy = (ImageView) deckFrame.getChildAt(1);
            deckFrame.removeView(dummy);
            discardFrame.addView(dummy);
            redDiscardText.setVisibility(View.INVISIBLE);
            blackDiscardText.setVisibility(View.INVISIBLE);
            deckDiscardText.setVisibility(View.INVISIBLE);
            deck.pop();
            hasDrawn = false;
        } else {
            emptyDeckCheck();
        }
    }

    @BindView(R.id.redFrame)
    FrameLayout redFrame;
    @BindView(R.id.blackFrame)
    FrameLayout blackFrame;
    @BindView(R.id.discardFrame)
    FrameLayout discardFrame;

    @BindView(R.id.ten_clubs)
    FrameLayout ten_clubs;
    @BindView(R.id.ten_diamonds)
    FrameLayout ten_diamonds;
    @BindView(R.id.ten_spades)
    FrameLayout ten_spades;
    @BindView(R.id.ten_hearts)
    FrameLayout ten_hearts;
    @BindView(R.id.jack_clubs)
    FrameLayout jack_clubs;
    @BindView(R.id.jack_diamonds)
    FrameLayout jack_diamonds;
    @BindView(R.id.jack_spades)
    FrameLayout jack_spades;
    @BindView(R.id.jack_hearts)
    FrameLayout jack_hearts;
    @BindView(R.id.queen_clubs)
    FrameLayout queen_clubs;
    @BindView(R.id.queen_diamonds)
    FrameLayout queen_diamonds;
    @BindView(R.id.queen_spades)
    FrameLayout queen_spades;
    @BindView(R.id.queen_hearts)
    FrameLayout queen_hearts;
    @BindView(R.id.king_clubs)
    FrameLayout king_clubs;
    @BindView(R.id.king_diamonds)
    FrameLayout king_diamonds;
    @BindView(R.id.king_spades)
    FrameLayout king_spades;
    @BindView(R.id.king_hearts)
    FrameLayout king_hearts;
    @BindView(R.id.ace_clubs)
    FrameLayout ace_clubs;
    @BindView(R.id.ace_diamonds)
    FrameLayout ace_diamonds;
    @BindView(R.id.ace_spades)
    FrameLayout ace_spades;
    @BindView(R.id.ace_hearts)
    FrameLayout ace_hearts;

    @BindView(R.id.pile_clubs)
    FrameLayout pile_clubs;
    @BindView(R.id.pile_diamonds)
    FrameLayout pile_diamonds;
    @BindView(R.id.pile_spades)
    FrameLayout pile_spades;
    @BindView(R.id.pile_hearts)
    FrameLayout pile_hearts;

    @BindView(R.id.deckDiscardText)
    TextView deckDiscardText;
    @BindView(R.id.redDiscardText)
    TextView redDiscardText;
    @BindView(R.id.blackDiscardText)
    TextView blackDiscardText;

    @BindView(R.id.scoreText)
    TextView scoreText;
    @BindView(R.id.pileText)
    TextView pileText;
    @BindView(R.id.levelText)
    TextView levelText;
    @BindView(R.id.cardsLeftText)
    TextView cardsLeftText;

    @BindView(R.id.clubsText)
    TextView clubsText;
    @BindView(R.id.diamondsText)
    TextView diamondsText;
    @BindView(R.id.spadesText)
    TextView spadesText;
    @BindView(R.id.heartsText)
    TextView heartsText;

    @BindView(R.id.toPlayButton)
    Button toPlay;
    @OnClick(R.id.toPlayButton)
    void onToPlayClick(){
        adFragment.setVisibility(View.GONE);
        getFragmentManager().beginTransaction()
                            .add(R.id.container, toPlayFragment)
                            .addToBackStack("aFrag").commit();
    }
    @BindView(R.id.newGameButton)
    Button newGame;
    @OnClick(R.id.newGameButton)
    void onNewGameClick(){
        Intent i = new Intent(GameActivity.this, GameActivity.class);
        startActivity(i);
        finish();
    }
    @BindView(R.id.gameOverButton)
    Button gameOver;
    @OnClick(R.id.gameOverButton)
    void onGameOverClick(){
        Intent i = new Intent(GameActivity.this, GameActivity.class);
        startActivity(i);
        finish();
    }
    @BindView(R.id.topCard)
    ImageView topCard;

    private static Stack<Card> deck;
    private Card black, red;
    private List<FrameLayout> colorFrames, pileFrames, faceFrames;
    private List<TextView> remainingCards;
    private List<Card> colorCards;
    private Boolean hasDrawn;
    private int level, pileTotal, scoreTotal;
    private ToPlayFragment toPlayFragment;
    private View adFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        deck = new Stack<>();

        colorCards = new ArrayList<>();
        colorCards.add(Card.COLOR_BLACK, black = null);
        colorCards.add(Card.COLOR_RED, red = null);

        toPlayFragment = new ToPlayFragment();

        colorFrames = new ArrayList<>();
        colorFrames.add(Card.COLOR_BLACK, blackFrame);
        colorFrames.add(Card.COLOR_RED, redFrame);

        pileFrames = new ArrayList<>();
        pileFrames.add(Card.CLUB_SUIT, pile_clubs);
        pileFrames.add(Card.DIAMOND_SUIT, pile_diamonds);
        pileFrames.add(Card.SPADE_SUIT, pile_spades);
        pileFrames.add(Card.HEART_SUIT, pile_hearts);

        faceFrames = new ArrayList<>();
        faceFrames.add(Card.CLUB_SUIT, ten_clubs);
        faceFrames.add(Card.DIAMOND_SUIT, ten_diamonds);
        faceFrames.add(Card.SPADE_SUIT, ten_spades);
        faceFrames.add(Card.HEART_SUIT, ten_hearts);
        faceFrames.add(1*4 + Card.CLUB_SUIT, jack_clubs);
        faceFrames.add(1*4 + Card.DIAMOND_SUIT, jack_diamonds);
        faceFrames.add(1*4 + Card.SPADE_SUIT, jack_spades);
        faceFrames.add(1*4 + Card.HEART_SUIT, jack_hearts);
        faceFrames.add(2*4 + Card.CLUB_SUIT, queen_clubs);
        faceFrames.add(2*4 + Card.DIAMOND_SUIT, queen_diamonds);
        faceFrames.add(2*4 + Card.SPADE_SUIT, queen_spades);
        faceFrames.add(2*4 + Card.HEART_SUIT, queen_hearts);
        faceFrames.add(3*4 + Card.CLUB_SUIT, king_clubs);
        faceFrames.add(3*4 + Card.DIAMOND_SUIT, king_diamonds);
        faceFrames.add(3*4 + Card.SPADE_SUIT, king_spades);
        faceFrames.add(3*4 + Card.HEART_SUIT, king_hearts);
        faceFrames.add(4*4 + Card.CLUB_SUIT, ace_clubs);
        faceFrames.add(4*4 + Card.DIAMOND_SUIT, ace_diamonds);
        faceFrames.add(4*4 + Card.SPADE_SUIT, ace_spades);
        faceFrames.add(4*4 + Card.HEART_SUIT, ace_hearts);

        remainingCards = new ArrayList<>();
        remainingCards.add(clubsText);
        remainingCards.add(diamondsText);
        remainingCards.add(spadesText);
        remainingCards.add(heartsText);

        deckDiscardText.setVisibility(View.INVISIBLE);
        gameOver.setVisibility(View.INVISIBLE);
//        adFragment = this.findViewById(R.id.adFragment);

        hasDrawn = false;

        pileTotal = 0;
        scoreTotal = 0;
        level = 1;

        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 15; j++) {
                deck.push(new Card(j, i));
            }
        }
        Collections.shuffle(deck);

        for(int i = 0; i<colorFrames.size(); i++){
            int finalI = i;
            colorFrames.get(i).setOnClickListener(v -> {
                // colorFrames.get(0) = blackFrame
                // colorFrames.get(1) = redFrame
                if(hasDrawn){
                    FrameLayout frame = (FrameLayout) v;
                    if(finalI == Card.COLOR_BLACK && deck.peek().isBlack()){
                        colorFrameDiscardClick(frame);
                    } else if(finalI ==1 && deck.peek().isRed()){
                        colorFrameDiscardClick(frame);
                    }
                }
                emptyDeckCheck();
            });
        }

        for(int i = 0; i<faceFrames.size(); i++){
            int index = i;
            faceFrames.get(i).setOnClickListener(v -> {
                if(!hasDrawn) {
                    int suit = index % 4;
                    FrameLayout faceFrame = (FrameLayout) v;
                    FrameLayout pileFrame = pileFrames.get(suit);
                    Card sameColorCard = colorCards.get(suit % 2);
                    Card otherColorCard = colorCards.get((suit+1) % 2);

                    if (sameColorCard != null && sameColorCard.getSuit() == suit
                            && otherColorCard != null && !isFrameCardSet(pileFrame)
                            && (index < 4 || index/4 >= level)) {
                        ImageView dummy = (ImageView) faceFrame.getChildAt(0);
                        faceFrame.removeView(dummy);
                        pileFrame.addView(dummy);

                        blackFrame.removeAllViews();
                        redFrame.removeAllViews();
                        pileTotal = pileTotal + black.getValue() + red.getValue();
                        pileText.setText(pileTotal + "");
                        black = null;
                        red = null;

                        faceFrame.setClickable(false);
                        faceFrame.setBackground(getResources().getDrawable(R.drawable.shape2));

                        if (isFrameCardSet(pile_clubs) && isFrameCardSet(pile_clubs) &&
                                isFrameCardSet(pile_clubs) && isFrameCardSet(pile_clubs)) {
                            scoreTotal = scoreTotal + pileTotal;
                            scoreText.setText(scoreTotal + "");
                            pileTotal = 0;
                            pileText.setText(pileTotal + "");
                            pile_clubs.removeAllViews();
                            pile_spades.removeAllViews();
                            pile_hearts.removeAllViews();
                            pile_diamonds.removeAllViews();
                            level++;
                            if (level == 2) {
                                levelText.setText("Queen");
                            }
                            if (level == 3) {
                                levelText.setText("King");
                            }
                            if (level == 4) {
                                levelText.setText("Ace");
                            }
                            if (level == 5) {
                                levelText.setText("God-Tier");
                            }
                        }
                        setUsedFaceCard(faceFrame, index);
                    } else if (isFrameCardSet(pileFrame) && (index < 4 || index/4 < level)) {
                        FrameLayout colorFrame = colorFrames.get(index % 2);
                        ImageView dummy = (ImageView) colorFrame.getChildAt(0);
                        if (dummy != null) {
                            colorFrame.removeView(dummy);
                            discardFrame.addView(dummy);
                        }
                        dummy = (ImageView) faceFrame.getChildAt(0);
                        faceFrame.removeView(dummy);
                        colorFrame.addView(dummy);
                        sameColorCard = new Card(Card.TEN_VALUE, suit);
                        faceFrame.setClickable(false);
                        faceFrame.setBackground( getResources().getDrawable(R.drawable.shape2));
                        setUsedFaceCard(faceFrame, suit);
                    }
                }
            });
        }
    }

    private boolean isFrameCardSet(FrameLayout frame) {
        return frame.getChildAt(0) != null;
    }

    private void colorFrameDiscardClick(FrameLayout colorFrame) {
        ImageView dummy = (ImageView) colorFrame.getChildAt(0);
        colorFrame.removeView(dummy);
        discardFrame.addView(dummy);
        dummy = (ImageView) deckFrame.getChildAt(1);
        deckFrame.removeView(dummy);
        colorFrame.addView(dummy);
        redDiscardText.setVisibility(View.INVISIBLE);
        blackDiscardText.setVisibility(View.INVISIBLE);
        deckDiscardText.setVisibility(View.INVISIBLE);
        if(deck.peek().isBlack()) {
            black = deck.pop();
        } else {
            red = deck.pop();
        }
        hasDrawn = false;
    }

    private void emptyDeckCheck() {
        if (deck.size() == 0) {
            topCard.setVisibility(View.INVISIBLE);
            deckFrame.setClickable(false);
            gameOver.setVisibility(View.VISIBLE);
        }
    }

    private void setUsedFaceCard(FrameLayout frame, int suit) {
        ImageView suitImage = new ImageView(getApplicationContext());
        if(suit == Card.CLUB_SUIT)
            suitImage.setImageResource(R.drawable.club);
        else if (suit == Card.DIAMOND_SUIT)
            suitImage.setImageResource(R.drawable.diamond);
        else if (suit == Card.SPADE_SUIT)
            suitImage.setImageResource(R.drawable.spades);
        else
            suitImage.setImageResource(R.drawable.heart);
        frame.addView(suitImage);
    }

    private void addFaceCard(ImageView drawnCard) {
        int frameIndex = (deck.peek().getValue() - 10) * 4
                + deck.peek().getSuit();
        FrameLayout moveToFrame = faceFrames.get(frameIndex);
        moveToFrame.addView(drawnCard);
        moveToFrame.setClickable(true);
        deck.pop();
        hasDrawn = false;
    }

    @Override
    public void onValuesSet() {

    }

    @Override
    public void finishFragment() {
        getFragmentManager().beginTransaction().remove(toPlayFragment).commit();

    }

    @Override
    public void fragmentManager() {
        adFragment.setVisibility(View.VISIBLE);

    }

//    /**
//     * This class makes the ad request and loads the ad.
//     */
//    public static class AdFragment extends Fragment {
//
//        private AdView mAdView;
//
//        public AdFragment() {
//        }
//
//        @Override
//        public void onActivityCreated(Bundle bundle) {
//            super.onActivityCreated(bundle);
//
//            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
//            // values/strings.xml.
//            mAdView = (AdView) getView().findViewById(R.id.adView);
//
//            // Create an ad request. Check logcat output for the hashed device ID to
//            // get test ads on a physical device. e.g.
//            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .build();
//
//            // Start loading the ad in the background.
//            mAdView.loadAd(adRequest);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.fragment_ad, container, false);
//        }
//
//        /** Called when leaving the activity */
//        @Override
//        public void onPause() {
//            if (mAdView != null) {
//                mAdView.pause();
//            }
//            super.onPause();
//        }
//
//        /** Called when returning to the activity */
//        @Override
//        public void onResume() {
//            super.onResume();
//            if (mAdView != null) {
//                mAdView.resume();
//            }
//        }
//
//        /** Called before the activity is destroyed */
//        @Override
//        public void onDestroy() {
//            if (mAdView != null) {
//                mAdView.destroy();
//            }
//            super.onDestroy();
//        }
//
//    }
}
