package gms.angus.angussoli.model

enum class CardValue(val identity: String, val value: Int) {
    TWO("two", 2),
    THREE("three", 3),
    FOUR("four", 4),
    FIVE("five", 5),
    SIX("six", 6),
    SEVEN("seven", 7),
    EIGHT("eight", 8),
    NINE("nine", 9),
    TEN("Ten", 10),
    JACK("Jack", 11),
    QUEEN("Queen", 12),
    KING("King", 13),
    ACE("Ace", 14);

    fun getPointValue(): Int {
        return when {
            value > 10 -> 10
            else -> value
        }
    }
}