package gms.angus.angussoli.model

enum class CardSuit(val identity: String) {
    CLUB("clubs"),
    DIAMOND("diamonds"),
    SPADE("spades"),
    HEART("hearts");

    @Suppress("unused")
    val isBlack: Boolean
        get() = this == CLUB || this == SPADE
    val isRed: Boolean
        get() = this == DIAMOND || this == HEART
}