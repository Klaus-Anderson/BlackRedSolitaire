package com.angus.gaming.blackredsolitaire;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.angus.gaming.blackredsolitaire.Card.CLUB_SUIT;
import static com.angus.gaming.blackredsolitaire.Card.COLOR_BLACK;
import static com.angus.gaming.blackredsolitaire.Card.COLOR_RED;
import static com.angus.gaming.blackredsolitaire.Card.DIAMOND_SUIT;
import static com.angus.gaming.blackredsolitaire.Card.HEART_SUIT;
import static com.angus.gaming.blackredsolitaire.Card.SPADE_SUIT;

public class GameActivity extends Activity {
    @BindView(R.id.deckFrame)
    FrameLayout deckFrame;
    @BindView(R.id.redFrame)
    FrameLayout redFrame;
    @BindView(R.id.blackFrame)
    FrameLayout blackFrame;
    @BindView(R.id.discardFrame)
    FrameLayout discardFrame;

    @BindView(R.id.tens)
    LinearLayout tensLayout;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.jacks)
    LinearLayout jacksLayout;
    @BindView(R.id.textViewJ)
    TextView textViewJ;
    @BindView(R.id.queens)
    LinearLayout queensLayout;
    @BindView(R.id.textViewQ)
    TextView textViewQ;
    @BindView(R.id.kings)
    LinearLayout kingsLayout;
    @BindView(R.id.textViewK)
    TextView textViewK;
    @BindView(R.id.aces)
    LinearLayout acesLayout;
    @BindView(R.id.textViewA)
    TextView textViewA;

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

    @BindView(R.id.deckText)
    TextView deckText;
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
    @BindView(R.id.topCard)
    ImageView topCard;

    private Stack<Card> deck;
    private List<FrameLayout> colorFrames, pileFrames, faceFrames;
    private List<TextView> remainingCards;
    private List<Card> colorCards;
    private List<Integer> eligibleIndexes;
    private Boolean hasDrawn;
    private int level, pileTotal, scoreTotal, brokenLevel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        deck = new Stack<>();

        colorCards = new ArrayList<>();
        colorCards.add(Card.COLOR_BLACK, null);
        colorCards.add(Card.COLOR_RED, null);

        colorFrames = new ArrayList<>();
        colorFrames.add(COLOR_BLACK, blackFrame);
        colorFrames.add(COLOR_RED, redFrame);

        pileFrames = new ArrayList<>();
        pileFrames.add(CLUB_SUIT, pile_clubs);
        pileFrames.add(DIAMOND_SUIT, pile_diamonds);
        pileFrames.add(SPADE_SUIT, pile_spades);
        pileFrames.add(HEART_SUIT, pile_hearts);

        faceFrames = new ArrayList<>();
        faceFrames.add(CLUB_SUIT, ten_clubs);
        faceFrames.add(DIAMOND_SUIT, ten_diamonds);
        faceFrames.add(SPADE_SUIT, ten_spades);
        faceFrames.add(HEART_SUIT, ten_hearts);
        faceFrames.add(4 + CLUB_SUIT, jack_clubs);
        faceFrames.add(4 + DIAMOND_SUIT, jack_diamonds);
        faceFrames.add(4 + SPADE_SUIT, jack_spades);
        faceFrames.add(4 + HEART_SUIT, jack_hearts);
        faceFrames.add(2*4 + CLUB_SUIT, queen_clubs);
        faceFrames.add(2*4 + DIAMOND_SUIT, queen_diamonds);
        faceFrames.add(2*4 + SPADE_SUIT, queen_spades);
        faceFrames.add(2*4 + HEART_SUIT, queen_hearts);
        faceFrames.add(3*4 + CLUB_SUIT, king_clubs);
        faceFrames.add(3*4 + DIAMOND_SUIT, king_diamonds);
        faceFrames.add(3*4 + SPADE_SUIT, king_spades);
        faceFrames.add(3*4 + HEART_SUIT, king_hearts);
        faceFrames.add(4*4 + CLUB_SUIT, ace_clubs);
        faceFrames.add(4*4 + DIAMOND_SUIT, ace_diamonds);
        faceFrames.add(4*4 + SPADE_SUIT, ace_spades);
        faceFrames.add(4*4 + HEART_SUIT, ace_hearts);

        remainingCards = new ArrayList<>();
        remainingCards.add(clubsText);
        remainingCards.add(diamondsText);
        remainingCards.add(spadesText);
        remainingCards.add(heartsText);

        setHasDrawn(false);

        pileTotal = 0;
        scoreTotal = 0;
        level = 1;
        eligibleIndexes = new ArrayList<>();

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
                    if(finalI == COLOR_BLACK && deck.peek().isBlack()){
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
                    int localizedIndex = index;
                    int suit = localizedIndex % 4;
                    FrameLayout faceFrame = (FrameLayout) v;
                    FrameLayout pileFrame = pileFrames.get(suit);
                    Card sameColorCard = colorCards.get(suit % 2);
                    Card otherColorCard = colorCards.get((suit+1) % 2);

                    // use Face Card as Face Card
                    if (sameColorCard != null && sameColorCard.getSuit() == suit
                            && otherColorCard != null && !isFrameCardSet(pileFrame)
                            && (localizedIndex < 4 || localizedIndex/4 <= level)) {
                        ImageView dummy = (ImageView) faceFrame.getChildAt(0);
                        faceFrame.removeView(dummy);
                        pileFrame.addView(dummy);

                        blackFrame.removeAllViews();
                        redFrame.removeAllViews();

                        pileTotal = pileTotal + colorCards.get(COLOR_BLACK).getValue() +
                                colorCards.get(COLOR_RED).getValue();
                        pileText.setText(String.valueOf(pileTotal));

                        colorCards.set(COLOR_BLACK, null);
                        colorCards.set(COLOR_RED, null);

                        // pile check
                        if (isFrameCardSet(pile_clubs) && isFrameCardSet(pile_diamonds) &&
                                isFrameCardSet(pile_spades) && isFrameCardSet(pile_hearts)) {
                            scoreTotal = scoreTotal +
                                    pileTotal * (level - (brokenLevel != -1 ? 1 : 0));
                            scoreText.setText(String.valueOf(scoreTotal));
                            pileTotal = 0;
                            pileText.setText(String.valueOf(pileTotal));
                            pile_clubs.removeAllViews();
                            pile_spades.removeAllViews();
                            pile_hearts.removeAllViews();
                            pile_diamonds.removeAllViews();
                            increaseLevel(false);
                        }
                        findEligibleFaces();
                        setUsedFaceCard(faceFrame, suit, localizedIndex);
                    }
                    // use Face Card as number Card
                    else if (isFrameCardSet(faceFrame)
                            && (localizedIndex < 4 || localizedIndex/4 < level)) {
                        FrameLayout colorFrame = colorFrames.get(localizedIndex % 2);
                        ImageView dummy = (ImageView) colorFrame.getChildAt(0);
                        if (dummy != null) {
                            colorFrame.removeView(dummy);
                            discardFrame.addView(dummy);
                        }
                        dummy = (ImageView) faceFrame.getChildAt(0);
                        faceFrame.removeView(dummy);
                        colorFrame.addView(dummy);
                        colorCards.set(suit % 2, new Card(Card.TEN_VALUE, suit));
                        findEligibleFaces();
                        setUsedFaceCard(faceFrame, suit, localizedIndex);
                    }
                }
            });
        }
    }

    @OnClick(R.id.deckFrame)
    void onDeckClick(View v){
        if (deck.size() != 0 && !hasDrawn) {
            setHasDrawn(true);

            int newCount;
            newCount = Integer.parseInt(remainingCards.get(
                    deck.peek().getSuit()).getText() + "") - 1;

            ImageView drawnCard = new ImageView(GameActivity.this);

            String drawableName = deck.peek().getImageName();
            int drawableId = getResources().getIdentifier(
                    drawableName, "drawable", getPackageName());
            drawnCard.setImageResource(drawableId);
            drawnCard.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            if (deck.peek().getValue() >= Card.TEN_VALUE ) {
                int frameIndex = ( deck.peek().getValue() - 10 ) * 4
                        + deck.peek().getSuit();
                FrameLayout moveToFrame = faceFrames.get(frameIndex);
                if (deck.peek().getValue() != 10 + brokenLevel) {
                    moveToFrame.addView(drawnCard);
                    moveToFrame.setClickable(true);
                } else {
                    ImageView face_down = new ImageView(this);
                    face_down.setImageResource(R.drawable.card_back);
                    moveToFrame.addView(face_down);
                }
                deck.pop();
                cardsLeftText.setText(String.valueOf(deck.size()));
                checkIfFaceIsEligible(frameIndex);
                setHasDrawn(false);
            } else {
                remainingCards.get(deck.peek().getSuit()).setText(
                        String.valueOf(newCount));
                newCount = Integer.parseInt(cardsLeftText.getText() + "") - 1;
                cardsLeftText.setText(String.valueOf(newCount));

                if (deck.peek().isBlack()) {
                    if (colorCards.get(COLOR_BLACK) == null) {
                        blackFrame.addView(drawnCard);
                        colorCards.set(COLOR_BLACK, deck.pop());
                        cardsLeftText.setText(String.valueOf(deck.size()));
                        setHasDrawn(false);
                        findEligibleFaces();
                    } else {
                        deckFrame.addView(drawnCard);
                        blackDiscardText.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (colorCards.get(COLOR_RED) == null) {
                        redFrame.addView(drawnCard);
                        colorCards.set(COLOR_RED, deck.pop());
                        cardsLeftText.setText(String.valueOf(deck.size()));
                        setHasDrawn(false);
                        findEligibleFaces();
                    } else {
                        deckFrame.addView(drawnCard);
                        redDiscardText.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (deck.size() != 0 && hasDrawn) {
            ImageView dummy = (ImageView) deckFrame.getChildAt(1);
            deckFrame.removeView(dummy);
            discardFrame.addView(dummy);
            redDiscardText.setVisibility(View.INVISIBLE);
            blackDiscardText.setVisibility(View.INVISIBLE);
            deck.pop();
            cardsLeftText.setText(String.valueOf(deck.size()));
            setHasDrawn(false);
            findEligibleFaces();
        }
        emptyDeckCheck();
    }

    @OnClick(R.id.breakButton)
    void onBreakClick(View view){
        view.setClickable(false);
        view.setVisibility(View.INVISIBLE);
        for(int i = 0; i < 4; i++) {
            int index = ( level * 4 ) + i;
            FrameLayout frame = faceFrames.get(( level * 4 ) + i);
            frame.setClickable(false);
            faceFrames.set(index,frame);
        }
        brokenLevel = level;
        increaseLevel(true);
    }

    @OnClick(R.id.toPlayButton)
    void onToPlayClick(){
        getFragmentManager().beginTransaction().add(R.id.container, new ToPlayFragment(), ToPlayFragment.class.getSimpleName()).commit();
    }

    @OnClick(R.id.newGameButton)
    void onNewGameClick(){
        Intent i = new Intent(GameActivity.this, GameActivity.class);
        startActivity(i);
        finish();
    }

    private void checkIfFaceIsEligible(int frameIndex) {
        if(faceFrames.get(frameIndex) != null && faceFrames.get(frameIndex).getChildCount() != 0 &&
                faceFrames.get(frameIndex).isClickable()) {
            if (colorCards.get(COLOR_BLACK) != null && colorCards.get(COLOR_RED) != null &&
                    frameIndex / 4 <= level && ( frameIndex % 4 == colorCards.get(frameIndex % 2).getSuit() ) &&
                    pileFrames.get(frameIndex % 4).getChildCount() == 0) {
                eligibleIndexes.add(frameIndex);
                faceFrames.get(frameIndex).setBackgroundResource(R.drawable.face_eligible_shape);
            } else if (frameIndex / 4 < level && frameIndex / 4 != brokenLevel) {
                faceFrames.get(frameIndex).setBackgroundResource(R.drawable.number_eligible_shape);
            }
        }
    }

    private void findEligibleFaces(){
        for(Integer i : eligibleIndexes){
            faceFrames.get(i).setBackground(null);
        }
        eligibleIndexes.clear();

        if(colorCards.get(COLOR_RED) != null && colorCards.get(COLOR_BLACK) != null) {
            for (int i = 0; i < 4 * ( level + 1 ); i++) {
                checkIfFaceIsEligible(i);
            }
        }
    }
    private void increaseLevel(boolean isBroken) {
        if (level == 1) {
            levelText.setText(R.string.queen);
            if(!isBroken) {
                jacksLayout.setBackground(getResources().getDrawable(R.drawable.face_number_shape));
            } else {
                jacksLayout.setBackground(getResources().getDrawable(R.drawable.broken_shape));
            }
            queensLayout.setBackground(getResources().getDrawable(R.drawable.current_level_shape));
        }
        else if (level == 2) {
            levelText.setText(R.string.king);
            if(!isBroken) {
                queensLayout.setBackground(getResources().getDrawable(R.drawable.face_number_shape));
            } else {
                queensLayout.setBackground(getResources().getDrawable(R.drawable.broken_shape));
            }
            kingsLayout.setBackground(getResources().getDrawable(R.drawable.current_level_shape));

        }
        else if (level == 3) {
            levelText.setText(R.string.ace);
            if(!isBroken) {
                kingsLayout.setBackground(getResources().getDrawable(R.drawable.face_number_shape));
            } else {
                kingsLayout.setBackground(getResources().getDrawable(R.drawable.broken_shape));
            }
            acesLayout.setBackground(getResources().getDrawable(R.drawable.current_level_shape));
        }
        else if (level == 4) {
            levelText.setText(R.string.god_Tier);
            if(!isBroken) {
                acesLayout.setBackground(getResources().getDrawable(R.drawable.face_number_shape));
            } else {
                acesLayout.setBackground(getResources().getDrawable(R.drawable.broken_shape));
            }
        }
        if(isBroken) {
            for (int i = level * 4; i < level * 4 + 4; i++){
                if(faceFrames.get(i).getChildCount() != 0 &&
                        pileFrames.get(i % 4).getChildCount() == 0) {
                    faceFrames.get(i).removeAllViews();
                    ImageView face_down = new ImageView(this);
                    face_down.setImageResource(R.drawable.card_back);
                    faceFrames.get(i).addView(face_down);
                }
            }
        }
        level++;
        findEligibleFaces();
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
        if(deck.peek().isBlack()) {
            colorCards.set(COLOR_BLACK, deck.pop());
            cardsLeftText.setText(String.valueOf(deck.size()));
        } else {
            colorCards.set(COLOR_RED, deck.pop());
            cardsLeftText.setText(String.valueOf(deck.size()));
        }
        setHasDrawn(false);
        findEligibleFaces();
        emptyDeckCheck();
    }

    private void emptyDeckCheck() {
        if (deck.size() == 0) {
            topCard.setVisibility(View.INVISIBLE);
            deckFrame.setClickable(false);
        }
    }

    private void setUsedFaceCard(FrameLayout frame, int suit, int localizedIndex) {
        frame.setClickable(false);
        frame.setBackground(getResources().getDrawable(R.drawable.face_cover_shape));
        ImageView suitImage = new ImageView(getApplicationContext());
        if(suit == CLUB_SUIT)
            suitImage.setImageResource(R.drawable.club);
        else if (suit == DIAMOND_SUIT)
            suitImage.setImageResource(R.drawable.diamond);
        else if (suit == SPADE_SUIT)
            suitImage.setImageResource(R.drawable.spades);
        else
            suitImage.setImageResource(R.drawable.heart);
        frame.addView(suitImage);

        // set to null so that it won't be playable
        faceFrames.get(localizedIndex);

    }

    private void setHasDrawn(boolean hasDrawn) {
        if(hasDrawn){
            deckText.setText(R.string.press_to_discard);
        } else {
            deckText.setText(R.string.press_to_draw);
        }
        this.hasDrawn = hasDrawn;
    }
}
