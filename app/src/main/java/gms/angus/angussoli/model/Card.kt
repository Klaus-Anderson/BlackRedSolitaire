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

    fun getDrawableResourceId(): Int {
        return when (cardValue) {
            CardValue.TWO -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.two_clubs
                CardSuit.DIAMOND -> R.drawable.two_diamonds
                CardSuit.SPADE -> R.drawable.two_spades
                CardSuit.HEART -> R.drawable.two_hearts
            }
            CardValue.THREE -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.three_clubs
                CardSuit.DIAMOND -> R.drawable.three_diamonds
                CardSuit.SPADE -> R.drawable.three_spades
                CardSuit.HEART -> R.drawable.three_hearts
            }
            CardValue.FOUR -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.four_clubs
                CardSuit.DIAMOND -> R.drawable.four_diamonds
                CardSuit.SPADE -> R.drawable.four_spades
                CardSuit.HEART -> R.drawable.four_hearts
            }
            CardValue.FIVE -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.five_clubs
                CardSuit.DIAMOND -> R.drawable.five_diamonds
                CardSuit.SPADE -> R.drawable.five_spades
                CardSuit.HEART -> R.drawable.five_hearts
            }
            CardValue.SIX -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.six_clubs
                CardSuit.DIAMOND -> R.drawable.six_diamonds
                CardSuit.SPADE -> R.drawable.six_spades
                CardSuit.HEART -> R.drawable.six_hearts
            }
            CardValue.SEVEN -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.seven_clubs
                CardSuit.DIAMOND -> R.drawable.seven_diamonds
                CardSuit.SPADE -> R.drawable.seven_spades
                CardSuit.HEART -> R.drawable.seven_hearts
            }
            CardValue.EIGHT -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.eight_clubs
                CardSuit.DIAMOND -> R.drawable.eight_diamonds
                CardSuit.SPADE -> R.drawable.eight_spades
                CardSuit.HEART -> R.drawable.eight_hearts
            }
            CardValue.NINE -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.nine_clubs
                CardSuit.DIAMOND -> R.drawable.nine_diamonds
                CardSuit.SPADE -> R.drawable.nine_spades
                CardSuit.HEART -> R.drawable.nine_hearts
            }
            CardValue.TEN -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.ten_clubs
                CardSuit.DIAMOND -> R.drawable.ten_diamonds
                CardSuit.SPADE -> R.drawable.ten_spades
                CardSuit.HEART -> R.drawable.ten_hearts
            }
            CardValue.JACK -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.jack_clubs
                CardSuit.DIAMOND -> R.drawable.jack_diamonds
                CardSuit.SPADE -> R.drawable.jack_spades
                CardSuit.HEART -> R.drawable.jack_hearts
            }
            CardValue.QUEEN -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.queen_clubs
                CardSuit.DIAMOND -> R.drawable.queen_diamonds
                CardSuit.SPADE -> R.drawable.queen_spades
                CardSuit.HEART -> R.drawable.queen_hearts
            }
            CardValue.KING -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.king_clubs
                CardSuit.DIAMOND -> R.drawable.king_diamonds
                CardSuit.SPADE -> R.drawable.king_spades
                CardSuit.HEART -> R.drawable.king_hearts
            }
            CardValue.ACE -> when (cardSuit) {
                CardSuit.CLUB -> R.drawable.ace_clubs
                CardSuit.DIAMOND -> R.drawable.ace_diamonds
                CardSuit.SPADE -> R.drawable.ace_spades
                CardSuit.HEART -> R.drawable.ace_hearts
            }
        }
    }
}