package gms.angus.angussoli.model

import java.util.HashMap

class Card(val value: Int, val suit: Int) {
    private val valueMap = HashMap<Int, String>()
    private val suitMap = HashMap<Int, String>()
    private fun createHashMaps() {
        suitMap[CLUB_SUIT] = "clubs"
        suitMap[DIAMOND_SUIT] = "diamonds"
        suitMap[SPADE_SUIT] = "spades"
        suitMap[HEART_SUIT] = "hearts"
        valueMap[TWO_VALUE] = "two"
        valueMap[THREE_VALUE] = "three"
        valueMap[FOUR_VALUE] = "four"
        valueMap[FIVE_VALUE] = "five"
        valueMap[SIX_VALUE] = "six"
        valueMap[SEVEN_VALUE] = "seven"
        valueMap[EIGHT_VALUE] = "eight"
        valueMap[NINE_VALUE] = "nine"
        valueMap[TEN_VALUE] = "ten"
        valueMap[JACK_VALUE] = "jack"
        valueMap[QUEEN_VALUE] = "queen"
        valueMap[KING_VALUE] = "king"
        valueMap[ACE_VALUE] = "ace"
    }

    val imageName: String
        get() = valueMap[value].toString() + "_" + suitMap[suit]

    override fun toString(): String {
        return valueMap[value].toString() + " of " + suitMap[suit]
    }

    val isClub: Boolean
        get() = suit == CLUB_SUIT
    val isDiamond: Boolean
        get() = suit == DIAMOND_SUIT
    val isSpade: Boolean
        get() = suit == SPADE_SUIT
    val isHeart: Boolean
        get() = suit == HEART_SUIT
    val isBlack: Boolean
        get() = isClub || isSpade
    val isRed: Boolean
        get() = isDiamond || isHeart

    companion object {
        const val COLOR_BLACK = 0
        const val COLOR_RED = 1
        const val CLUB_SUIT = 0
        const val DIAMOND_SUIT = 1
        const val SPADE_SUIT = 2
        const val HEART_SUIT = 3
        const val TWO_VALUE = 2
        const val THREE_VALUE = 3
        const val FOUR_VALUE = 4
        const val FIVE_VALUE = 5
        const val SIX_VALUE = 6
        const val SEVEN_VALUE = 7
        const val EIGHT_VALUE = 8
        const val NINE_VALUE = 9
        const val TEN_VALUE = 10
        const val JACK_VALUE = 11
        const val QUEEN_VALUE = 12
        const val KING_VALUE = 13
        const val ACE_VALUE = 14
    }

    init {
        createHashMaps()
    }
}