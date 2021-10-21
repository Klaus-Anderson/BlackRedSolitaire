package gms.angus.angussoli.model

class GameState() {
    val deck = ArrayDeque<Card>()
    val discardedCards = ArrayDeque<Card>()

    val levels = listOf(CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE)
    var currentLevel: CardValue? = CardValue.JACK
    var brokenFaceValue: CardValue? = null
    var clearedFaceValues = mutableListOf(CardValue.TEN)
    var score = 0

    var blackCard: Card? = null
    var redCard: Card? = null
    var deckTopCard: Card? = null
    var tenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var jackPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var queenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var kingPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var acePool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
    var pileScores: MutableMap<CardSuit, Int> = mutableMapOf<CardSuit, Int>().apply {
        CardSuit.values().forEach {
            put(it, 0)
        }
    }
    var poolMap: MutableMap<CardValue, MutableMap<CardSuit, FaceCardState>> =
        mutableMapOf(
            CardValue.TEN to tenPool, CardValue.JACK to jackPool, CardValue.QUEEN to queenPool,
            CardValue.KING to kingPool, CardValue.ACE to acePool
        )
    var pileCards: MutableMap<CardSuit, Card?> = mutableMapOf<CardSuit, Card?>().apply {
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

    private fun handleDrawnFaceCard(faceCard: Card) {
        poolMap[faceCard.cardValue]!!.also {
            it[faceCard.cardSuit] = FaceCardState.NOT_USABLE
        }
        deck.removeFirst()
        updateFaceCardStates()
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
                deck.removeFirst()
                updateFaceCardStates()
            } else {
                deckTopCard = numberCard
            }
        }
    }

    private fun updateFaceCardStates() {
        poolMap.flatMap { pool ->
            pool.value.mapNotNull {
                when (it.value) {
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
            val faceEligible = blackCard != null && redCard != null && pileCards[faceCardSuit] == null &&
                    (faceCardValue == currentLevel || clearedFaceValues.contains(faceCardValue))
            if (faceEligible) {
                listOf(blackCard, redCard).first {
                    it?.cardSuit?.isRed == faceCardSuit.isRed
                }?.let { colorCard ->
                    poolMap[faceCardValue]?.set(
                        faceCardSuit,
                        if (colorCard.cardSuit == faceCardSuit) {
                            if (currentLevel == faceCardValue && faceEligible) {
                                FaceCardState.USABLE_AS_FACE
                            } else if (clearedFaceValues.contains(faceCardValue)) {
                                if (faceEligible && faceCardSuit == colorCard.cardSuit) {
                                    FaceCardState.USABLE_AS_FACE
                                } else {
                                    FaceCardState.USABLE_AS_COLOR
                                }
                            } else {
                                FaceCardState.NOT_USABLE
                            }
                        } else {
                            if (clearedFaceValues.contains(faceCardValue)) {
                                FaceCardState.USABLE_AS_COLOR
                            } else {
                                FaceCardState.NOT_USABLE
                            }
                        }
                    )
                }
            } else if (brokenFaceValue == faceCardValue) {
                poolMap[faceCardValue]?.set(faceCardSuit, FaceCardState.BROKEN)
            } else if (clearedFaceValues.contains(faceCardValue)) {
                poolMap[faceCardValue]?.set(faceCardSuit, FaceCardState.USABLE_AS_COLOR)
            } else {
                poolMap[faceCardValue]?.set(faceCardSuit, FaceCardState.NOT_USABLE)
            }
        }
    }

    fun breakLevel() {
        if (brokenFaceValue==null) {
            poolMap[currentLevel]?.apply {
                forEach {
                    when (it.value) {
                        FaceCardState.USABLE_AS_FACE, FaceCardState.USABLE_AS_COLOR -> put(
                            it.key,
                            FaceCardState.NOT_USABLE
                        )
                        else -> run { }
                    }
                }
            }
            brokenFaceValue = currentLevel
            currentLevel = currentLevel?.let {
                levels.elementAtOrNull(levels.indexOf(it) + 1)
            }
            updateFaceCardStates()
        }
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

    fun onDeckTouched() {
        if (deckTopCard?.let {
                false
            } ?: (deck.size > 0)) {
            deck.first().also {
                if (it.isFaceCard()) {
                    handleDrawnFaceCard(it)
                } else {
                    handleDrawnNumberCard(it)
                }
            }
        } else deckTopCard?.let {
            discardedCards.add(deck.removeFirst())
            deckTopCard = null
        } ?: return
    }

    fun onFaceCardTouched(cardValue: CardValue, cardSuit: CardSuit) {
        poolMap[cardValue]?.let { mutableMap ->
            mutableMap[cardSuit]?.let { faceCardState ->
                when (faceCardState) {
                    FaceCardState.USABLE_AS_FACE -> run {
                        pileScores[cardSuit] =
                            blackCard!!.cardValue.getPointValue() + redCard!!.cardValue.getPointValue()
                        poolMap[cardValue]?.let {
                            it[cardSuit] = FaceCardState.USED
                        }
                        pileCards[cardSuit] = Card(cardValue, cardSuit)
                        redCard = null
                        blackCard = null
                        pileScores.all {
                            it.value != 0
                        }.let { roundIsCompleted ->
                            if (roundIsCompleted) {
                                score += pileScores.values.sum()
                                pileScores = mutableMapOf<CardSuit, Int>().apply {
                                    CardSuit.values().forEach {
                                        put(it, 0)
                                    }
                                }
                                pileCards = mutableMapOf<CardSuit, Card?>().apply {
                                    CardSuit.values().forEach {
                                        put(it, null)
                                    }
                                }
                                currentLevel?.let {
                                    clearedFaceValues.add(it)
                                }
                                currentLevel = currentLevel?.let{
                                    levels.elementAtOrNull(levels.indexOf(it) + 1)
                                }
                            }
                        }
                        updateFaceCardStates()
                    }
                    FaceCardState.USABLE_AS_COLOR -> run {
                        if (cardSuit.isRed) {
                            redCard = Card(cardValue, cardSuit)
                        } else {
                            blackCard = Card(cardValue, cardSuit)
                        }
                        poolMap[cardValue]?.let {
                            it[cardSuit] = FaceCardState.USED
                        }
                        updateFaceCardStates()
                    }
                    else -> return
                }
            }
        }
    }

    fun getNumberOfSuitNumberCardsLeft(cardSuit: CardSuit): Int {
        return deck.filter {
            it.cardSuit == cardSuit && it.cardValue.value < 10
        }.size - (if (deckTopCard?.cardSuit == cardSuit) {
            1
        } else {
            0
        })
    }

    fun enableCompleteMode() {
        currentLevel = null
        clearedFaceValues = levels.toMutableList()
    }

}
