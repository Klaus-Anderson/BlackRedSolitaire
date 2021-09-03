package gms.angus.angussoli.model

enum class CardSuit(val identity: String) {
    CLUB("club"),
    DIAMOND("diamond"),
    SPADE("spade"),
    HEART("heart");

    val isBlack: Boolean
        get() = this == CLUB || this == SPADE
    val isRed: Boolean
        get() = this == DIAMOND || this == HEART
}