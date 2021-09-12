package gms.angus.angussoli.viewmodel.impl

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.model.GameState
import gms.angus.angussoli.viewmodel.GameViewModel

class GameViewModelImpl(application: Application) : GameViewModel, AndroidViewModel(application) {
    override val topCardVisibilityLiveData = MutableLiveData<Int>()
    override val redDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val blackDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val deckDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val cardLeftTextLiveData = MutableLiveData<String>("52")
    override val deckTopCardLiveData = MutableLiveData<Card?>()
    override val redCardLiveData = MutableLiveData<Card?>()
    override val blackCardLiveData = MutableLiveData<Card?>()
    override val tenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val jackPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val queenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val kingPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val acePoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val collectedCardsLiveData = MutableLiveData<Map<CardSuit, Card?>>()
    private var gameState: GameState = GameState

    init {
    }

    override fun onDeckFrameClick(view: View) {
        if (gameState.canDraw()) {
            gameState.drawACard()
        } else gameState.deckTopCard?.let {
            gameState.discardDeckTopCard()
        } ?: return
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

    private fun updateLiveData() {
        deckTopCardLiveData.value = gameState.deckTopCard.also { deckTopCard ->
            (deckTopCard?.let {
                if (it.cardSuit.isRed) {
                    redDiscardTextVisibilityLiveData.value = View.VISIBLE
                    blackDiscardTextVisibilityLiveData.value = View.INVISIBLE
                } else {
                    redDiscardTextVisibilityLiveData.value = View.INVISIBLE
                    blackDiscardTextVisibilityLiveData.value = View.VISIBLE
                }
                View.VISIBLE
            } ?: run {
                View.INVISIBLE
            }).let {
                topCardVisibilityLiveData.value = it
                deckDiscardTextVisibilityLiveData.value = it
            }
        }

        redCardLiveData.value = gameState.redCard
        blackCardLiveData.value = gameState.blackCard

        tenPoolLiveData.value = gameState.tenPool
        jackPoolLiveData.value = gameState.jackPool
        queenPoolLiveData.value = gameState.queenPool
        kingPoolLiveData.value = gameState.kingPool
        acePoolLiveData.value = gameState.acePool
        collectedCardsLiveData.value = gameState.collectedCards
        cardLeftTextLiveData.value = gameState.deck.size.toString()
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