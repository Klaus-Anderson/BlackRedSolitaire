package gms.angus.brsoli.model;

import java.util.HashMap;

public class Card {

    public static final int COLOR_BLACK = 0;
    public static final int COLOR_RED = 1;

    public static final int CLUB_SUIT = 0;
    public static final int DIAMOND_SUIT = 1;
    public static final int SPADE_SUIT = 2;
    public static final int HEART_SUIT = 3;

    public static final int TWO_VALUE=2;
    public static final int THREE_VALUE=3;
    public static final int FOUR_VALUE=4;
    public static final int FIVE_VALUE=5;
    public static final int SIX_VALUE=6;
    public static final int SEVEN_VALUE=7;
    public static final int EIGHT_VALUE=8;
    public static final int NINE_VALUE=9;
    public static final int TEN_VALUE=10;
    public static final int JACK_VALUE=11;
    public static final int QUEEN_VALUE=12;
    public static final int KING_VALUE=13;
    public static final int ACE_VALUE=14;


    private int value, suit;
    private HashMap<Integer, String> valueMap = new HashMap<>();
    private HashMap<Integer, String> suitMap = new HashMap<>();

    public Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
        createHashMaps();
    }

    private void createHashMaps() {
        suitMap.put(CLUB_SUIT, "clubs");
        suitMap.put(DIAMOND_SUIT, "diamonds");
        suitMap.put(SPADE_SUIT, "spades");
        suitMap.put(HEART_SUIT, "hearts");

        valueMap.put(TWO_VALUE, "two");
        valueMap.put(THREE_VALUE, "three");
        valueMap.put(FOUR_VALUE, "four");
        valueMap.put(FIVE_VALUE, "five");
        valueMap.put(SIX_VALUE, "six");
        valueMap.put(SEVEN_VALUE, "seven");
        valueMap.put(EIGHT_VALUE, "eight");
        valueMap.put(NINE_VALUE, "nine");
        valueMap.put(TEN_VALUE, "ten");
        valueMap.put(JACK_VALUE, "jack");
        valueMap.put(QUEEN_VALUE, "queen");
        valueMap.put(KING_VALUE, "king");
        valueMap.put(ACE_VALUE, "ace");
    }

    public int getValue() {
        return value;
    }

    public int getSuit() {
        return suit;
    }

    public String getImageName() {
        return valueMap.get(value) + "_" + suitMap.get(suit);
    }

    @Override
    public String toString() {
        return valueMap.get(value) + " of " + suitMap.get(suit);
    }

    public boolean isClub() {
        return suit == CLUB_SUIT;
    }

    public boolean isDiamond() {
        return suit == DIAMOND_SUIT;
    }

    public boolean isSpade() {
        return suit == SPADE_SUIT;
    }

    public boolean isHeart() {
        return suit == HEART_SUIT;
    }

    public boolean isBlack() {
        return isClub() || isSpade();
    }

    public boolean isRed() {
        return isDiamond() || isHeart();
    }
}