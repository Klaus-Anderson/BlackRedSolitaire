package com.angus.gaming.blackredsolitaire;

import java.util.HashMap;

public class Card {

    private int value, suit;
    private HashMap<Integer, String> valueMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> suitMap = new HashMap<Integer, String>();

    public Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
        createHashMaps();
    }

    private void createHashMaps() {
        suitMap.put(0, "clubs");
        suitMap.put(1, "diamonds");
        suitMap.put(2, "spades");
        suitMap.put(3, "hearts");

        
        valueMap.put(2, "two");
        valueMap.put(3, "three");
        valueMap.put(4, "four");
        valueMap.put(5, "five");
        valueMap.put(6, "six");
        valueMap.put(7, "seven");
        valueMap.put(8, "eight");
        valueMap.put(9, "nine");
        valueMap.put(10, "ten");
        valueMap.put(11, "jack");
        valueMap.put(12, "queen");
        valueMap.put(13, "king");
        valueMap.put(14, "ace");
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
}