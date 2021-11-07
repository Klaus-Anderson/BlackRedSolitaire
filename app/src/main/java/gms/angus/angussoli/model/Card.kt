package gms.angus.angussoli.model

data class Card(val cardValue: CardValue, val cardSuit: CardSuit) {
    override fun toString(): String {
        return cardValue.identity + " of " + cardSuit.identity
    }

    fun isFaceCard(): Boolean {
        return cardValue.value >= 10
    }

    @Suppress("unused")
    fun isNumberCard(): Boolean {
        return !isFaceCard()
    }

}