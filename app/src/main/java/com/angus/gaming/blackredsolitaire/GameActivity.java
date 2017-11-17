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
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends Activity implements ToPlayFragment.OnValuesSetListener {
    @BindView(R.id.deckFrame)
    FrameLayout deckFrame;
    @OnClick(R.id.deckFrame)
    void onClick(View v){
        if (deck.size() != 0 && !hasDrawn) {
            hasDrawn = true;
            remainingCardUpdate();
            ImageView drawnCard = new ImageView(GameActivity.this);
            imageSetup(drawnCard);
            drawCheck(drawnCard);
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
    @BindView(R.id.newGameButton)
    Button newGame;
    @BindView(R.id.gameOverButton)
    Button gameOver;

    @BindView(R.id.topCard)
    ImageView topCard;

    private static Stack<Card> deck;
    private Card black, red;
    private ArrayList<FrameLayout> faceFrames;
    private ArrayList<TextView> remainingCards;
    private Boolean hasDrawn, pileClubs, pileSpades, pileHearts, pileDiamonds;
    private int level, pileTotal, scoreTotal;
    private ToPlayFragment toPlayFragment;
    private View adFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        deck = new Stack<Card>();
        topCard = new ImageView(this);
        red = null;
        black = null;

        toPlayFragment = new ToPlayFragment();

        faceFrames = new ArrayList<FrameLayout>();
        faceFrames.add(ten_clubs);
        faceFrames.add(ten_diamonds);
        faceFrames.add(ten_spades);
        faceFrames.add(ten_hearts);
        faceFrames.add(jack_clubs);
        faceFrames.add(jack_diamonds);
        faceFrames.add(jack_spades);
        faceFrames.add(jack_hearts);
        faceFrames.add(queen_clubs);
        faceFrames.add(queen_diamonds);
        faceFrames.add(queen_spades);
        faceFrames.add(queen_hearts);
        faceFrames.add(king_clubs);
        faceFrames.add(king_diamonds);
        faceFrames.add(king_spades);
        faceFrames.add(king_hearts);
        faceFrames.add(ace_clubs);
        faceFrames.add(ace_diamonds);
        faceFrames.add(ace_spades);
        faceFrames.add(ace_hearts);

        remainingCards = new ArrayList<>();
        remainingCards.add(clubsText);
        remainingCards.add(diamondsText);
        remainingCards.add(spadesText);
        remainingCards.add(heartsText);

        deckDiscardText.setVisibility(View.INVISIBLE);
        gameOver.setVisibility(View.INVISIBLE);
//        adFragment = this.findViewById(R.id.adFragment);

        hasDrawn = false;
        pileClubs = false;
        pileSpades = false;
        pileHearts = false;
        pileDiamonds = false;

        pileTotal = 0;
        scoreTotal = 0;
        level = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 15; j++) {
                deck.push(new Card(j, i));
            }
        }
        Collections.shuffle(deck);

        blackFrame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasDrawn && deck.peek().isBlack()) {
                    colorFrameDiscardClick(blackFrame);
                }
                emptyDeckCheck();
            }

        });

        redFrame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasDrawn && deck.peek().isRed()) {
                    colorFrameDiscardClick(blackFrame);
                }
                emptyDeckCheck();
            }
        });

        ten_clubs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 0 && red != null
                        && !pileClubs && !hasDrawn) {
                    ImageView dummy = (ImageView) ten_clubs.getChildAt(0);
                    ten_clubs.removeView(dummy);
                    pile_clubs.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    ten_clubs.setClickable(false);
                    pileClubs = true;
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    ten_clubs.addView(suit);
                    pileCheck();
                } else if (!hasDrawn && ten_clubs.getChildAt(0) != null) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) ten_clubs.getChildAt(0);
                    ten_clubs.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 0);
                    ten_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    ten_clubs.addView(suit);
                }
            }

        });

        ten_diamonds.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 1 && black != null
                        && !pileDiamonds && !hasDrawn) {
                    ImageView dummy = (ImageView) ten_diamonds.getChildAt(0);
                    ten_diamonds.removeView(dummy);
                    pile_diamonds.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    ten_diamonds.setClickable(false);
                    pileDiamonds = true;
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    ten_diamonds.addView(suit);
                    pileCheck();
                } else if (!hasDrawn && ten_diamonds.getChildAt(0) != null) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) ten_diamonds.getChildAt(0);
                    ten_diamonds.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 1);
                    ten_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    ten_diamonds.addView(suit);
                }
            }

        });

        ten_spades.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 2 && red != null
                        && !pileSpades && !hasDrawn) {
                    ImageView dummy = (ImageView) ten_spades.getChildAt(0);
                    ten_spades.removeView(dummy);
                    pile_spades.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    ten_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    ten_spades.addView(suit);
                    pileSpades = true;
                    pileCheck();
                } else if (!hasDrawn && ten_spades.getChildAt(0) != null) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) ten_spades.getChildAt(0);
                    ten_spades.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 2);
                    ten_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    ten_spades.addView(suit);
                }
            }

        });

        ten_hearts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 3 && black != null
                        && !pileHearts && !hasDrawn) {
                    ImageView dummy = (ImageView) ten_hearts.getChildAt(0);
                    ten_hearts.removeView(dummy);
                    pile_hearts.addView(dummy);
                    redFrame.removeAllViews();
                    blackFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    ten_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    ten_hearts.addView(suit);
                    pileHearts = true;
                    pileCheck();
                } else if (!hasDrawn && ten_hearts.getChildAt(0) != null) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) ten_hearts.getChildAt(0);
                    ten_hearts.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 3);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ten_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ten_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    ten_hearts.addView(suit);
                    ten_hearts.setClickable(false);
                }
            }

        });

        jack_clubs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 0 && red != null
                        && !pileClubs && !hasDrawn && level == 0) {
                    ImageView dummy = (ImageView) jack_clubs.getChildAt(0);
                    jack_clubs.removeView(dummy);
                    pile_clubs.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    jack_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    jack_clubs.addView(suit);
                    pileClubs = true;
                    pileCheck();
                } else if (!hasDrawn && jack_clubs.getChildAt(0) != null && level > 0) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) jack_clubs.getChildAt(0);
                    jack_clubs.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 0);
                    jack_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    jack_clubs.addView(suit);
                }
            }

        });

        jack_diamonds.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 1 && black != null
                        && !pileDiamonds && !hasDrawn && level == 0) {
                    ImageView dummy = (ImageView) jack_diamonds.getChildAt(0);
                    jack_diamonds.removeView(dummy);
                    pile_diamonds.addView(dummy);
                    redFrame.removeAllViews();
                    blackFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    jack_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    jack_diamonds.addView(suit);
                    pileDiamonds = true;
                    pileCheck();
                } else if (!hasDrawn && jack_diamonds.getChildAt(0) != null && level > 0) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) jack_diamonds.getChildAt(0);
                    jack_diamonds.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 1);
                    jack_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    jack_diamonds.addView(suit);
                }
            }

        });

        jack_spades.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 2 && red != null
                        && !pileSpades && !hasDrawn && level==0) {
                    ImageView dummy = (ImageView) jack_spades.getChildAt(0);
                    jack_spades.removeView(dummy);
                    pile_spades.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    jack_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    jack_spades.addView(suit);
                    pileSpades = true;
                    pileCheck();
                } else if (!hasDrawn && jack_spades.getChildAt(0) != null && level > 0) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) jack_spades.getChildAt(0);
                    jack_spades.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 2);
                    jack_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    jack_spades.addView(suit);
                }
            }

        });

        jack_hearts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 3 && black != null
                        && !pileHearts && !hasDrawn && level == 0) {
                    ImageView dummy = (ImageView) jack_hearts.getChildAt(0);
                    jack_hearts.removeView(dummy);
                    pile_hearts.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    jack_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    jack_hearts.addView(suit);
                    pileHearts = true;
                    pileCheck();
                } else if (!hasDrawn && jack_hearts.getChildAt(0) != null && level > 0) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) jack_hearts.getChildAt(0);
                    jack_hearts.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 3);
                    jack_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        jack_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        jack_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    jack_hearts.addView(suit);
                }
            }

        });

        queen_clubs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 0 && red != null
                        && !pileClubs && !hasDrawn && level== 1) {
                    ImageView dummy = (ImageView) queen_clubs.getChildAt(0);
                    queen_clubs.removeView(dummy);
                    pile_clubs.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    queen_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    queen_clubs.addView(suit);
                    pileClubs = true;
                    pileCheck();
                } else if (!hasDrawn && queen_clubs.getChildAt(0) != null && level > 1) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) queen_clubs.getChildAt(0);
                    queen_clubs.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 0);
                    queen_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    queen_clubs.addView(suit);
                }
            }

        });

        queen_diamonds.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 1 && black != null
                        && !pileDiamonds && !hasDrawn && level == 1) {
                    ImageView dummy = (ImageView) queen_diamonds.getChildAt(0);
                    queen_diamonds.removeView(dummy);
                    pile_diamonds.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    queen_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    queen_diamonds.addView(suit);
                    pileDiamonds = true;
                    pileCheck();
                } else if (!hasDrawn && queen_diamonds.getChildAt(0) != null && level > 1 ) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) queen_diamonds.getChildAt(0);
                    queen_diamonds.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 1);
                    queen_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    queen_diamonds.addView(suit);
                }
            }

        });

        queen_spades.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 2 && red != null
                        && !pileSpades && !hasDrawn && level ==1) {
                    ImageView dummy = (ImageView) queen_spades.getChildAt(0);
                    queen_spades.removeView(dummy);
                    pile_spades.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    queen_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    queen_spades.addView(suit);
                    pileSpades = true;
                    pileCheck();
                } else if (!hasDrawn && queen_spades.getChildAt(0) != null && level > 1) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) queen_spades.getChildAt(0);
                    queen_spades.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 2);
                    queen_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    queen_spades.addView(suit);
                }
            }

        });

        queen_hearts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 3 && black != null
                        && !pileHearts && !hasDrawn && level==1) {
                    ImageView dummy = (ImageView) queen_hearts.getChildAt(0);
                    queen_hearts.removeView(dummy);
                    pile_hearts.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    queen_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    queen_hearts.addView(suit);
                    pileHearts = true;
                    pileCheck();
                } else if (!hasDrawn && queen_hearts.getChildAt(0) != null && level > 1) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) queen_hearts.getChildAt(0);
                    queen_hearts.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 3);
                    queen_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        queen_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        queen_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    queen_hearts.addView(suit);
                }
            }

        });

        king_clubs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 0 && red != null
                        && !pileClubs && !hasDrawn && level == 2) {
                    ImageView dummy = (ImageView) king_clubs.getChildAt(0);
                    king_clubs.removeView(dummy);
                    pile_clubs.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    king_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    king_clubs.addView(suit);
                    pileClubs = true;
                    pileCheck();
                } else if (!hasDrawn && king_clubs.getChildAt(0) != null && level > 2) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) king_clubs.getChildAt(0);
                    king_clubs.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 0);
                    king_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    king_clubs.addView(suit);
                }
            }

        });

        king_diamonds.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 1 && black != null
                        && !pileDiamonds && !hasDrawn && level == 2) {
                    ImageView dummy = (ImageView) king_diamonds.getChildAt(0);
                    king_diamonds.removeView(dummy);
                    pile_diamonds.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    king_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    king_diamonds.addView(suit);
                    pileDiamonds = true;
                    pileCheck();
                } else if (!hasDrawn && king_diamonds.getChildAt(0) != null && level > 2) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) king_diamonds.getChildAt(0);
                    king_diamonds.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 1);
                    king_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    king_diamonds.addView(suit);
                }
            }

        });

        king_spades.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 2 && red != null
                        && !pileSpades && !hasDrawn && level == 2) {
                    ImageView dummy = (ImageView) king_spades.getChildAt(0);
                    king_spades.removeView(dummy);
                    pile_spades.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    king_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    king_spades.addView(suit);
                    pileSpades = true;
                    pileCheck();
                } else if (!hasDrawn && king_spades.getChildAt(0) != null && level > 2) {
                    ImageView dummy = (ImageView) blackFrame.getChildAt(0);
                    if (dummy != null) {
                        blackFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) king_spades.getChildAt(0);
                    king_spades.removeView(dummy);
                    blackFrame.addView(dummy);
                    black = new Card(10, 2);
                    king_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    king_spades.addView(suit);
                }
            }

        });

        king_hearts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 3 && black != null
                        && !pileHearts && !hasDrawn && level == 2) {
                    ImageView dummy = (ImageView) king_hearts.getChildAt(0);
                    king_hearts.removeView(dummy);
                    pile_hearts.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    king_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    king_hearts.addView(suit);
                    pileHearts = true;
                    pileCheck();
                } else if (!hasDrawn && king_hearts.getChildAt(0) != null && level > 2) {
                    ImageView dummy = (ImageView) redFrame.getChildAt(0);
                    if (dummy != null) {
                        redFrame.removeView(dummy);
                        discardFrame.addView(dummy);
                    }
                    dummy = (ImageView) king_hearts.getChildAt(0);
                    king_hearts.removeView(dummy);
                    redFrame.addView(dummy);
                    red = new Card(10, 3);
                    king_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        king_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        king_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    king_hearts.addView(suit);
                }
            }

        });

        ace_clubs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 0 && red != null
                        && !pileClubs && !hasDrawn && level==3) {
                    ImageView dummy = (ImageView) ace_clubs.getChildAt(0);
                    ace_clubs.removeView(dummy);
                    pile_clubs.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    ace_clubs.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ace_clubs.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ace_clubs.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.club);
                    ace_clubs.addView(suit);
                    pileClubs = true;
                    pileCheck();
                }
            }

        });

        ace_diamonds.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 1 && black != null
                        && !pileDiamonds && !hasDrawn && level ==3) {
                    ImageView dummy = (ImageView) ace_diamonds.getChildAt(0);
                    ace_diamonds.removeView(dummy);
                    pile_diamonds.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    ace_diamonds.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ace_diamonds.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ace_diamonds.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.diamond);
                    ace_diamonds.addView(suit);
                    pileDiamonds = true;
                    pileCheck();
                }
            }

        });

        ace_spades.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (black != null && black.getSuit() == 2 && red != null
                        && !pileSpades && !hasDrawn && level == 3) {
                    ImageView dummy = (ImageView) ace_spades.getChildAt(0);
                    ace_spades.removeView(dummy);
                    pile_spades.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + black.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    black = null;
                    red = null;
                    ace_spades.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ace_spades.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ace_spades.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.spades);
                    ace_spades.addView(suit);
                    pileSpades = true;
                    pileCheck();
                }
            }

        });

        ace_hearts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (red != null && red.getSuit() == 3 && black != null
                        && !pileHearts && !hasDrawn && level == 3) {
                    ImageView dummy = (ImageView) ace_hearts.getChildAt(0);
                    ace_hearts.removeView(dummy);
                    pile_hearts.addView(dummy);
                    blackFrame.removeAllViews();
                    redFrame.removeAllViews();
                    pileTotal = pileTotal + red.getValue() + red.getValue();
                    pileText.setText(pileTotal + "");
                    red = null;
                    black = null;
                    ace_hearts.setClickable(false);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ace_hearts.setBackgroundDrawable( getResources().getDrawable(R.drawable.shape2) );
                    } else {
                        ace_hearts.setBackground( getResources().getDrawable(R.drawable.shape2));
                    }
                    ImageView suit = new ImageView(getApplicationContext());
                    suit.setImageResource(R.drawable.heart);
                    ace_hearts.addView(suit);
                    pileHearts = true;
                    pileCheck();
                }
            }

        });

        newGame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, GameActivity.class);
                startActivity(i);
                finish();

            }

        });

        toPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                adFragment.setVisibility(View.GONE);
                getFragmentManager().beginTransaction()
                        .add(R.id.container, toPlayFragment)
                        .addToBackStack("aFrag").commit();

            }

        });

        gameOver.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, GameActivity.class);
                startActivity(i);
                finish();
            }

        });

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

    private void remainingCardUpdate() {
        int newCount;
        newCount = Integer.parseInt(remainingCards.get(
                deck.peek().getSuit()).getText()
                                            + "") - 1;
        remainingCards.get(deck.peek().getSuit()).setText(
                newCount + "");
        newCount = Integer.parseInt(cardsLeftText.getText() + "") - 1;
        cardsLeftText.setText(newCount + "");
    }

    private void imageSetup(ImageView drawnCard) {
        String drawableName = deck.peek().getImageName();
        int drawableId = getResources().getIdentifier(drawableName,
                                                      "drawable", getPackageName());
        drawnCard.setImageResource(drawableId);
        drawnCard.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void drawCheck(ImageView drawnCard) {
        if (deck.peek().getValue() > 9)
            this.addFaceCard(drawnCard);
        else
            this.addNumberCard(drawnCard);
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

    private void addNumberCard(ImageView drawnCard) {
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

    protected void pileCheck() {
        if (pileClubs && pileSpades && pileHearts && pileDiamonds) {
            scoreTotal = scoreTotal + pileTotal;
            scoreText.setText(scoreTotal + "");
            pileTotal = 0;
            pileText.setText(pileTotal + "");
            pile_clubs.removeAllViews();
            pile_spades.removeAllViews();
            pile_hearts.removeAllViews();
            pile_diamonds.removeAllViews();
            pileClubs = false;
            pileSpades = false;
            pileHearts = false;
            pileDiamonds = false;
            level++;
            if (level == 1) {
                levelText.setText("Queen");
            }
            if (level == 2) {
                levelText.setText("King");
            }
            if (level == 3) {
                levelText.setText("Ace");
            }
            if (level == 4) {
                levelText.setText("God-Tier");
            }
        }
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
