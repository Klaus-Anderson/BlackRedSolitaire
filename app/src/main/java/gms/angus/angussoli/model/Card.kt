package gms.angus.angussoli.model

data class Card(val cardValue: CardValue, val cardSuit: CardSuit) {
    val imageName: String
        get() = cardValue.identity + "_" + cardSuit.identity

    override fun toString(): String {
        return cardValue.identity + " of " + cardSuit.identity
    }

    fun isFaceCard(): Boolean {
        return cardValue.value >= 10
    }
}