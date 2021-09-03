package gms.angus.angussoli.model

object GameState {
    val deck = ArrayDeque<Card>()

    val levels = listOf(CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE)

    var blackCard: Card? = null
    var redCard: Card? = null
    var deckTopCard: Card? = null
    var currentLevel: CardValue = CardValue.JACK
    var brokenFaceValue: CardValue? = null

    var tenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var jackPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var queenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var kingPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var acePool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()

    private fun createEmptyPool(): MutableMap<CardSuit, FaceCardState> {
        return mutableMapOf<CardSuit, FaceCardState>().apply{
            CardSuit.values().forEach {
                put(it, FaceCardState.NOT_DRAWN)
            }
        }
    }

    var collectedCards: MutableMap<CardSuit, Card?> = mutableMapOf<CardSuit, Card?>().apply {
        CardSuit.values().forEach {
            put(it, null)
        }
    }
    var scoredCards = mutableListOf<Card>()

    init {
        CardValue.values().forEach { cardValue ->
            CardSuit.values().forEach {
                deck.add(Card(cardValue, it))
            }
        }
        deck.shuffle()
    }

    fun drawACard(): Card {
        return deck.removeFirst().also {
            if (it.isFaceCard()) {
                handleDrawnFaceCard(it)
            } else {
                handleDrawnNumberCard(it)
            }
        }
    }

    private fun handleDrawnFaceCard(faceCard: Card) {
        when (faceCard.cardValue) {
            CardValue.TEN -> tenPool
            CardValue.JACK -> jackPool
            CardValue.QUEEN -> queenPool
            CardValue.KING -> kingPool
            CardValue.ACE -> acePool
            else -> throw IllegalArgumentException()
        }.also {
            it[faceCard.cardSuit] =
                if (brokenFaceValue == faceCard.cardValue) {
                    FaceCardState.BROKEN
                } else if (currentLevel.value == faceCard.cardValue.value) {
                    FaceCardState.USABLE_AS_FACE
                } else if (currentLevel.value > faceCard.cardValue.value) {
                    FaceCardState.USABLE_AS_COLOR
                } else {
                    FaceCardState.NOT_USABLE
                }
        }
    }

    private fun handleDrawnNumberCard(numberCard: Card) {
        when (numberCard.cardSuit) {
            CardSuit.CLUB, CardSuit.SPADE -> blackCard
            CardSuit.DIAMOND, CardSuit.HEART -> redCard
        }.also {
            if (it == null) {
                when (numberCard.cardSuit) {
                    CardSuit.CLUB, CardSuit.SPADE -> also {
                        blackCard = numberCard
                    }
                    CardSuit.DIAMOND, CardSuit.HEART -> also {
                        redCard = numberCard
                    }
                }
            } else {
                deckTopCard = numberCard
            }
        }
    }

    fun canDraw(): Boolean {
        return deckTopCard?.let {
            false
        } ?: (deck.size > 0)
    }

    fun gameScore(): Int {
        return scoredCards.sumOf {
            it.cardValue.value
        }
    }

    fun collectedScore(): Int {
        return collectedCards.map {
            it.value?.cardValue?.value ?: 0
        }.sum()
    }

    fun breakLevel() {
        if(!hasBroken()){
            brokenFaceValue = currentLevel
            incrementLevel()
        }
    }

    private fun incrementLevel() {
        TODO("Not yet implemented")
    }

    fun hasBroken(): Boolean {
        return brokenFaceValue?.let {
            true
        } ?: false
    }
}
