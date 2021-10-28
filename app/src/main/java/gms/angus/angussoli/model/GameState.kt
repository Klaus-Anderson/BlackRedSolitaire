package gms.angus.angussoli.model

class GameState() {
    private val discardedCards = ArrayDeque<Card>()
    private val levels = listOf(CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE)
    private var rawScore = 0
    private var multiplierScore = 1

    private var pileScores: MutableMap<CardSuit, Int> = mutableMapOf<CardSuit, Int>().apply {
        CardSuit.values().forEach {
            put(it, 0)
        }
    }

    val deck = ArrayDeque<Card>()
    var currentLevel: CardValue? = CardValue.JACK
        private set
    var brokenFaceValue: CardValue? = null
        private set
    var clearedFaceValues = mutableListOf(CardValue.TEN)
        private set
    var blackCard: Card? = null
        private set
    var redCard: Card? = null
        private set
    var deckTopCard: Card? = null
        private set
    var tenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
        private set
    var jackPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
        private set
    var queenPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
        private set
    var kingPool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
        private set
    var acePool: MutableMap<CardSuit, FaceCardState> = createEmptyPool()
        private set
    var pileCards: MutableMap<CardSuit, Card?> = mutableMapOf<CardSuit, Card?>().apply {
        CardSuit.values().forEach {
            put(it, null)
        }
    }
        private set

    private var poolMap: MutableMap<CardValue, MutableMap<CardSuit, FaceCardState>> =
        mutableMapOf(
            CardValue.TEN to tenPool, CardValue.JACK to jackPool, CardValue.QUEEN to queenPool,
            CardValue.KING to kingPool, CardValue.ACE to acePool
        )


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
        if (brokenFaceValue == null) {
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
        if (deckTopCard?.cardSuit?.isRed == true) {
            deckTopCard?.let {
                discardedCards.add(redCard ?: throw IllegalStateException())
                redCard = deckTopCard
                deckTopCard = null
                deck.removeFirst()
            }
            updateFaceCardStates()
        }
    }

    fun discardBlackCardIfAble() {
        if (deckTopCard?.cardSuit?.isRed == false) {
            deckTopCard?.let {
                discardedCards.add(blackCard ?: throw IllegalStateException())
                blackCard = deckTopCard
                deckTopCard = null
                deck.removeFirst()
            }
            updateFaceCardStates()
        }
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
        deckTopCard ?: run {
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
                                    multiplierScore += 2
                                    rawScore += pileScores.values.sum()
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
                                    currentLevel = currentLevel?.let {
                                        levels.elementAtOrNull(levels.indexOf(it) + 1)
                                    }
                                }
                            }
                            updateFaceCardStates()
                        }
                        FaceCardState.USABLE_AS_COLOR -> run {
                            if (cardSuit.isRed) {
                                redCard?.let {
                                    discardedCards.add(it)
                                }
                                redCard = Card(cardValue, cardSuit)
                            } else {
                                blackCard?.let {
                                    discardedCards.add(it)
                                }
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
    }

    fun getNumberOfSuitNumberCardsLeftString(cardSuit: CardSuit): String {
        return (deck.filter {
            it.cardSuit == cardSuit && it.cardValue.value < 10
        }.size - (if (deckTopCard?.cardSuit == cardSuit) {
            1
        } else {
            0
        })).toString()
    }

    fun enableCompleteMode() {
        currentLevel = CardValue.KING
        clearedFaceValues = levels.toMutableList().apply {
            removeAll {
                it.value > 12
            }
        }
        multiplierScore = 5
        rawScore = 1
    }

    fun getFinalScoreString(): String {
        return (rawScore * multiplierScore).toString()
    }

    fun getMultiplierString(): String {
        return multiplierScore.toString()
    }

    fun getLevelText(): String {
        return when (currentLevel) {
            CardValue.JACK -> "J"
            CardValue.QUEEN -> "Q"
            CardValue.KING -> "K"
            CardValue.ACE -> "A"
            else -> "\u2713"
        } + when (currentLevel) {
            CardValue.ACE, null -> if(brokenFaceValue == null){
                "+"
            } else {
                ""
            }
            else -> ""
        } + if (multiplierScore == 11){
            "+"
        } else {
            ""
        }
    }

    fun getPileScoreString(): String {
        return pileScores.values.sum().toString()
    }

    fun getCardSquanderedText(): String {
        return discardedCards.size.toString()
    }

    fun isBreakButtonEligible(): Boolean {
        return brokenFaceValue?.let {
            false
        } ?: run {
            currentLevel?.let {
                it != CardValue.ACE
            } ?: true
        }
    }

    fun getCardsLeftText(): String {
        return deck.size.toString()
    }

}
