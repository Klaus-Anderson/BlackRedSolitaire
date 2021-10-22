package gms.angus.angussoli.viewmodel.impl

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gms.angus.angussoli.R
import gms.angus.angussoli.model.*
import gms.angus.angussoli.viewmodel.GameViewModel

class GameViewModelImpl(application: Application) : GameViewModel, AndroidViewModel(application) {
    override val redDiscardTextVisibilityLiveData = MutableLiveData<Int>(View.INVISIBLE)
    override val blackDiscardTextVisibilityLiveData = MutableLiveData<Int>(View.INVISIBLE)
    override val deckTopCardVisibilityLiveData = MutableLiveData<Int>(View.VISIBLE)
    override val underDeckTextLiveData = MutableLiveData<String>()
    override val cardLeftTextLiveData = MutableLiveData<String>("52")
    override val cardSquanderedTextLiveData = MutableLiveData<String>("0")
    override val clubNumbersLeftTextLiveData = MutableLiveData<String>(8.toString())
    override val spadeNumbersLeftTextLiveData = MutableLiveData<String>(8.toString())
    override val diamondNumbersLeftTextLiveData = MutableLiveData<String>(8.toString())
    override val heartNumbersLeftTextLiveData = MutableLiveData<String>(8.toString())
    override val deckTopCardLiveData = MutableLiveData<Card?>()
    override val redCardLiveData = MutableLiveData<Card?>()
    override val blackCardLiveData = MutableLiveData<Card?>()
    override val scoreTextLiveData = MutableLiveData<String>(0.toString())
    override val multiplierTextLiveData = MutableLiveData<String>(1.toString())
    override val pileScoreTextLiveData = MutableLiveData<String>(0.toString())
    override val clearedFaceCardsLiveData = MutableLiveData<List<CardValue>>()
    override val currentLevelLiveData = MutableLiveData<CardValue?>()
    override val brokenFaceValueLiveData = MutableLiveData<CardValue?>()
    override val tenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val jackPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val queenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val kingPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val acePoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val collectedCardsLiveData = MutableLiveData<Map<CardSuit, Card?>>()

    private var gameState: GameState = GameState()

    init {
        updateLiveData(application.applicationContext)
    }

    private fun updateLiveData(context: Context) {
        deckTopCardLiveData.value = gameState.deckTopCard.also { deckTopCard ->
            (deckTopCard?.let {
                if (it.cardSuit.isRed) {
                    redDiscardTextVisibilityLiveData.value = View.VISIBLE
                    blackDiscardTextVisibilityLiveData.value = View.INVISIBLE
                } else {
                    redDiscardTextVisibilityLiveData.value = View.INVISIBLE
                    blackDiscardTextVisibilityLiveData.value = View.VISIBLE
                }
                underDeckTextLiveData.value = context.getString(R.string.press_to_discard)
                View.VISIBLE
            } ?: run {
                blackDiscardTextVisibilityLiveData.value = View.INVISIBLE
                redDiscardTextVisibilityLiveData.value = View.INVISIBLE
                if(gameState.deck.size == 0){
                    deckTopCardVisibilityLiveData.value = View.INVISIBLE
                    underDeckTextLiveData.value =""
                } else {
                    underDeckTextLiveData.value = context.getString(R.string.press_to_draw)
                }
                View.INVISIBLE
            })
        }
        cardLeftTextLiveData.value = gameState.deck.size.toString()
        cardSquanderedTextLiveData.value = gameState.discardedCards.size.toString()
        currentLevelLiveData.value = gameState.currentLevel
        scoreTextLiveData.value = gameState.getFinalScore().toString()
        multiplierTextLiveData.value = gameState.getMultiplier().toString()
        pileScoreTextLiveData.value = gameState.pileScores.values.sum().toString()
        spadeNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeft(CardSuit.SPADE).toString()
        clubNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeft(CardSuit.CLUB).toString()
        heartNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeft(CardSuit.HEART).toString()
        diamondNumbersLeftTextLiveData.value = gameState.getNumberOfSuitNumberCardsLeft(CardSuit.DIAMOND).toString()


        redCardLiveData.value = gameState.redCard
        blackCardLiveData.value = gameState.blackCard

        //update Face Card state based on
        clearedFaceCardsLiveData.value = gameState.clearedFaceValues
        brokenFaceValueLiveData.value = gameState.brokenFaceValue
        tenPoolLiveData.value = gameState.tenPool
        jackPoolLiveData.value = gameState.jackPool
        queenPoolLiveData.value = gameState.queenPool
        kingPoolLiveData.value = gameState.kingPool
        acePoolLiveData.value = gameState.acePool
        collectedCardsLiveData.value = gameState.pileCards

    }

    override fun onDeckFrameClick(view: View) {
        gameState.onDeckTouched()
        updateLiveData(view.context)
    }

    override fun onRedFrameClick(view: View) {
        gameState.discardRedCardIfAble()
        updateLiveData(view.context)

    }

    override fun onBlackFrameClick(view: View) {
        gameState.discardBlackCardIfAble()
        updateLiveData(view.context)

    }

    override fun onFaceCardClick(cardValue: CardValue, cardSuit: CardSuit, context: Context) {
        gameState.onFaceCardTouched(cardValue, cardSuit)
        updateLiveData(context)

    }

    override fun endGame() {
        gameState = GameState()
    }

    override fun enableCompleteMode(context: Context) {
        gameState.enableCompleteMode()
        updateLiveData(context)

    }


    override fun onBreakClick(view: View) {
        gameState.breakLevel()
        updateLiveData(view.context)

//        unlockAchievement(getString(R.string.achievement_break))
    }

    override fun onToPlayClick(view: View) {
//        fragmentManager.beginTransaction()
//            .add(R.id.container, ToPlayFragment(), ToPlayFragment::class.java.simpleName)
//            .addToBackStack(ToPlayFragment::class.java.simpleName).commit()
    }

    override fun onNewGameClick(view: View) {
//        val i = Intent(this@GameActivity, GameActivity::class.java)
//        startActivity(i)
//        finish()
    }


}