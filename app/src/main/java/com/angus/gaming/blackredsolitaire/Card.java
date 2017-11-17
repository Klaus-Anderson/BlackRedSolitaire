package com.angus.gaming.blackredsolitaire;

import java.util.HashMap;

public class Card {

    private final int CLUB_SUIT = 0;
    private final int DIAMOND_SUIT = 1;
    private final int SPADE_SUIT = 2;
    private final int HEART_SUIT = 3;

    private final int TWO_VALUE=2;
    private final int THREE_VALUE=3;
    private final int FOUR_VALUE=4;
    private final int FIVE_VALUE=5;
    private final int SIX_VALUE=6;
    private final int SEVEN_VALUE=7;
    private final int EIGHT_VALUE=8;
    private final int NINE_VALUE=9;
    private final int TEN_VALUE=10;
    private final int JACK_VALUE=11;
    private final int QUEEN_VALUE=12;
    private final int KING_VALUE=13;
    private final int ACE_VALUE=14;


    private int value, suit;
    private HashMap<Integer, String> valueMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> suitMap = new HashMap<Integer, String>();

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
        String name = valueMap.get(value) + "_" + suitMap.get(suit);
        return name;
    }

    @Override
    public String toString() {
        String output = valueMap.get(value) + " of " + suitMap.get(suit);
        return output;
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