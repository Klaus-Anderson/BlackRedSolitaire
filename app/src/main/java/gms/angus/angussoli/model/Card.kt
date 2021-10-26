package gms.angus.angussoli.model

import gms.angus.angussoli.R

data class Card(val cardValue: CardValue, val cardSuit: CardSuit) {
    val imageName: String
        get() = cardValue.identity + "_" + cardSuit.identity

    override fun toString(): String {
        return cardValue.identity + " of " + cardSuit.identity
    }

    fun isFaceCard(): Boolean {
        return cardValue.value >= 10
    }

    fun isNumberCard(): Boolean {
        return !isFaceCard()
    }

    fun getDrawableResourceName(): String {
        return when (cardValue) {
            CardValue.TWO -> when (cardSuit) {
                CardSuit.CLUB -> "two_clubs"
                CardSuit.DIAMOND -> "two_diamonds"
                CardSuit.SPADE -> "two_spades"
                CardSuit.HEART -> "two_hearts"
            }
            CardValue.THREE -> when (cardSuit) {
                CardSuit.CLUB -> "three_clubs"
                CardSuit.DIAMOND -> "three_diamonds"
                CardSuit.SPADE -> "three_spades"
                CardSuit.HEART -> "three_hearts"
            }
            CardValue.FOUR -> when (cardSuit) {
                CardSuit.CLUB -> "four_clubs"
                CardSuit.DIAMOND -> "four_diamonds"
                CardSuit.SPADE -> "four_spades"
                CardSuit.HEART -> "four_hearts"
            }
            CardValue.FIVE -> when (cardSuit) {
                CardSuit.CLUB -> "five_clubs"
                CardSuit.DIAMOND -> "five_diamonds"
                CardSuit.SPADE -> "five_spades"
                CardSuit.HEART -> "five_hearts"
            }
            CardValue.SIX -> when (cardSuit) {
                CardSuit.CLUB -> "six_clubs"
                CardSuit.DIAMOND -> "six_diamonds"
                CardSuit.SPADE -> "six_spades"
                CardSuit.HEART -> "six_hearts"
            }
            CardValue.SEVEN -> when (cardSuit) {
                CardSuit.CLUB -> "seven_clubs"
                CardSuit.DIAMOND -> "seven_diamonds"
                CardSuit.SPADE -> "seven_spades"
                CardSuit.HEART -> "seven_hearts"
            }
            CardValue.EIGHT -> when (cardSuit) {
                CardSuit.CLUB -> "eight_clubs"
                CardSuit.DIAMOND -> "eight_diamonds"
                CardSuit.SPADE -> "eight_spades"
                CardSuit.HEART -> "eight_hearts"
            }
            CardValue.NINE -> when (cardSuit) {
                CardSuit.CLUB -> "nine_clubs"
                CardSuit.DIAMOND -> "nine_diamonds"
                CardSuit.SPADE -> "nine_spades"
                CardSuit.HEART -> "nine_hearts"
            }
            CardValue.TEN -> when (cardSuit) {
                CardSuit.CLUB -> "ten_clubs"
                CardSuit.DIAMOND -> "ten_diamonds"
                CardSuit.SPADE -> "ten_spades"
                CardSuit.HEART -> "ten_hearts"
            }
            CardValue.JACK -> when (cardSuit) {
                CardSuit.CLUB -> "jack_clubs"
                CardSuit.DIAMOND -> "jack_diamonds"
                CardSuit.SPADE -> "jack_spades"
                CardSuit.HEART -> "jack_hearts"
            }
            CardValue.QUEEN -> when (cardSuit) {
                CardSuit.CLUB -> "queen_clubs"
                CardSuit.DIAMOND -> "queen_diamonds"
                CardSuit.SPADE -> "queen_spades"
                CardSuit.HEART -> "queen_hearts"
            }
            CardValue.KING -> when (cardSuit) {
                CardSuit.CLUB -> "king_clubs"
                CardSuit.DIAMOND -> "king_diamonds"
                CardSuit.SPADE -> "king_spades"
                CardSuit.HEART -> "king_hearts"
            }
            CardValue.ACE -> when (cardSuit) {
                CardSuit.CLUB -> "ace_clubs"
                CardSuit.DIAMOND -> "ace_diamonds"
                CardSuit.SPADE -> "ace_spades"
                CardSuit.HEART -> "ace_hearts"
            }
        }
    }
}