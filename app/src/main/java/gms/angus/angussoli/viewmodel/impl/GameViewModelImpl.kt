package gms.angus.angussoli.viewmodel.impl

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gms.angus.angussoli.R
import gms.angus.angussoli.model.*
import gms.angus.angussoli.viewmodel.GameViewModel

class GameViewModelImpl(application: Application) : GameViewModel, AndroidViewModel(application) {
    override val redDiscardTextVisibilityLiveData = MutableLiveData<Int>(View.INVISIBLE)
    override val blackDiscardTextVisibilityLiveData = MutableLiveData<Int>(View.INVISIBLE)
    override val deckTextVisibilityLiveData = MutableLiveData<Int>(View.VISIBLE)
    override val deckTextResIdLiveData = MutableLiveData<Int>(R.string.press_to_draw)
    override val deckTopCardVisibilityLiveData = MutableLiveData<Int>(View.VISIBLE)
    override val cardLeftTextLiveData = MutableLiveData<String>("52")
    override val deckTopCardLiveData = MutableLiveData<Card?>()
    override val redCardLiveData = MutableLiveData<Card?>()
    override val blackCardLiveData = MutableLiveData<Card?>()
    override val scoreTextLiveData = MutableLiveData<String>(0.toString())
    override val clearedFaceCardsLiveData = MutableLiveData<List<CardValue>>()
    override val currentLevelLiveData = MutableLiveData<CardValue?>()
    override val brokenFaceValueLiveData = MutableLiveData<CardValue?>()
    override val tenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val jackPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val queenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val kingPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val acePoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val collectedCardsLiveData = MutableLiveData<Map<CardSuit, Card?>>()

    private var gameState: GameState = GameState

    init {
    }

    private fun updateLiveData() {
        deckTopCardLiveData.value = gameState.deckTopCard.also { deckTopCard ->
            (deckTopCard?.let {
                if (it.cardSuit.isRed) {
                    redDiscardTextVisibilityLiveData.postValue(View.VISIBLE)
                    blackDiscardTextVisibilityLiveData.postValue(View.INVISIBLE)
                } else {
                    redDiscardTextVisibilityLiveData.postValue(View.INVISIBLE)
                    blackDiscardTextVisibilityLiveData.postValue(View.VISIBLE)
                }
                deckTextResIdLiveData.postValue(R.string.press_to_discard)
                View.VISIBLE
            } ?: run {
                blackDiscardTextVisibilityLiveData.postValue(View.INVISIBLE)
                redDiscardTextVisibilityLiveData.postValue(View.INVISIBLE)
                if(gameState.deck.size == 0){
                    deckTextVisibilityLiveData.postValue(View.INVISIBLE)
                    deckTopCardVisibilityLiveData.postValue(View.INVISIBLE)
                }
                deckTextResIdLiveData.postValue(R.string.press_to_draw)
                View.INVISIBLE
            })
        }
        clearedFaceCardsLiveData.value = gameState.clearedFaceValues
        currentLevelLiveData.value = gameState.currentLevel
        redCardLiveData.value = gameState.redCard
        blackCardLiveData.value = gameState.blackCard
        scoreTextLiveData.value = gameState.score.toString()
        brokenFaceValueLiveData.value = gameState.brokenFaceValue

        //update Face Card state based on
        tenPoolLiveData.value = gameState.tenPool
        jackPoolLiveData.value = gameState.jackPool
        queenPoolLiveData.value = gameState.queenPool
        kingPoolLiveData.value = gameState.kingPool
        acePoolLiveData.value = gameState.acePool
        collectedCardsLiveData.value = gameState.pileCards
        cardLeftTextLiveData.value = gameState.deck.size.toString()
    }

    override fun onDeckFrameClick(view: View) {
        gameState.onDeckTouched()
        updateLiveData()
    }

    override fun onRedFrameClick(view: View) {
        gameState.discardRedCardIfAble()
        updateLiveData()
    }

    override fun onBlackFrameClick(view: View) {
        gameState.discardBlackCardIfAble()
        updateLiveData()
    }

    override fun onFaceCardClick(cardValue: CardValue, cardSuit: CardSuit) {
        gameState.onFaceCardTouched(cardValue, cardSuit)
        updateLiveData()
    }


    override fun onBreakClick(view: View) {
        gameState.breakLevel()
        updateLiveData()
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