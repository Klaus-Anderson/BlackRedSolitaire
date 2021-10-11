package gms.angus.angussoli.model

object GameState {
    val deck = ArrayDeque<Card>()
    val discardedCards = ArrayDeque<Card>()

    val levels = listOf(CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE)
    var currentLevel: CardValue = CardValue.JACK
    var brokenFaceValue: CardValue? = null
    var clearedFaceValues= listOf(CardValue.TEN)
    var scoredCards = mutableListOf<Card>()

    var blackCard: Card? = null
    var redCard: Card? = null
    var deckTopCard: Card? = null
    var tenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var jackPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var queenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var kingPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var acePool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var poolMap: MutableMap<CardValue, MutableMap<CardSuit, FaceCardState>> =
        mutableMapOf(CardValue.TEN to tenPool, CardValue.JACK to jackPool, CardValue.QUEEN to queenPool,
            CardValue.KING to kingPool, CardValue.ACE to acePool)
    var collectedCards: MutableMap<CardSuit, Card?> = mutableMapOf<CardSuit, Card?>().apply {
        CardSuit.values().forEach {
            put(it, null)
        }
    }

    init {
        CardValue.values().forEach { cardValue ->
            CardSuit.values().forEach {
                deck.add(Card(cardValue, it))
            }
        }
        deck.shuffle()
    }

    private fun createEmptyPool(): MutableMap<CardSuit, FaceCardState> {
        return mutableMapOf<CardSuit, FaceCardState>().apply {
            CardSuit.values().forEach {
                put(it, FaceCardState.NOT_DRAWN)
            }
        }
    }

    fun drawACard(): Card {
        return deck.first().also {
            if (it.isFaceCard()) {
                handleDrawnFaceCard(it)
            } else {
                handleDrawnNumberCard(it)
            }
        }
    }

    private fun handleDrawnFaceCard(faceCard: Card) {
        poolMap[faceCard.cardValue]!!.also {
            it[faceCard.cardSuit] =
                if (brokenFaceValue == faceCard.cardValue) {
                    FaceCardState.BROKEN
                } else if (currentLevel.value == faceCard.cardValue.value &&
                    (faceCard.cardSuit == redCard?.cardSuit || faceCard.cardSuit == blackCard?.cardSuit)
                ) {
                    FaceCardState.USABLE_AS_FACE
                } else if (currentLevel.value > faceCard.cardValue.value) {
                    FaceCardState.USABLE_AS_COLOR
                } else {
                    FaceCardState.NOT_USABLE
                }
        }
        deck.removeFirst()
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
                updateFaceCardStates()
                deck.removeFirst()
            } else {
                deckTopCard = numberCard
            }
        }
    }

    private fun updateFaceCardStates() {
        poolMap.flatMap{ pool ->
            pool.value.mapNotNull {
                when(it.value){
                    FaceCardState.NOT_DRAWN -> null
                    FaceCardState.NOT_USABLE -> pool.key to it.key
                    FaceCardState.USABLE_AS_FACE -> pool.key to it.key
                    FaceCardState.USABLE_AS_COLOR -> pool.key to it.key
                    FaceCardState.USED -> null
                    FaceCardState.BROKEN -> null
                }
            }
        }.forEach { pair ->
            val faceCardValue = pair.first
            val faceCardSuit = pair.second
            val faceEligible = blackCard != null && redCard!= null
            listOf(blackCard, redCard).mapNotNull { colorCard->
                if(colorCard?.cardSuit?.isRed == faceCardSuit.isRed){
                    if(colorCard.cardSuit == faceCardSuit){
                        if(currentLevel == faceCardValue && faceEligible){
                            FaceCardState.USABLE_AS_FACE
                        } else if (clearedFaceValues.contains(faceCardValue)){
                            if(faceEligible && faceCardSuit == colorCard.cardSuit){
                                FaceCardState.USABLE_AS_FACE
                            } else {
                                FaceCardState.USABLE_AS_COLOR
                            }
                        } else {
                            FaceCardState.NOT_USABLE
                        }
                    } else {
                        if (clearedFaceValues.contains(faceCardValue)){
                            FaceCardState.USABLE_AS_COLOR
                        } else {
                            FaceCardState.NOT_USABLE
                        }
                    }
                } else{
                    null
                }
            }.forEach {
                poolMap[faceCardValue]?.set(faceCardSuit, it)
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
        if (!hasBroken()) {
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

    fun discardDeckTopCard() {
        discardedCards.add(deck.removeFirst())
        deckTopCard = null
    }

    fun discardRedCardIfAble() {
        deckTopCard?.let {
            discardedCards.add(redCard ?: throw IllegalStateException())
            redCard = deckTopCard
            deckTopCard = null
            deck.removeFirst()
        }
        updateFaceCardStates()
    }

    fun discardBlackCardIfAble() {
        deckTopCard?.let {
            discardedCards.add(blackCard ?: throw IllegalStateException())
            blackCard = deckTopCard
            deckTopCard = null
            deck.removeFirst()
        }
        updateFaceCardStates()
    }
}
