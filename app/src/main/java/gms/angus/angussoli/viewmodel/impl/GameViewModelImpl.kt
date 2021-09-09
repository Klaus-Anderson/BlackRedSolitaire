package gms.angus.angussoli.viewmodel.impl

import android.app.Application
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.model.GameState
import gms.angus.angussoli.model.GameState.deck
import gms.angus.angussoli.view.GameActivity
import gms.angus.angussoli.view.ToPlayFragment
import gms.angus.angussoli.viewmodel.GameViewModel
import java.util.*

class GameViewModelImpl(application: Application) : GameViewModel, AndroidViewModel(application) {
    override val topCardVisibilityLiveData = MutableLiveData<Int>()
    override val deckFrameVisibilityLiveData = MutableLiveData<Int>()
    override val deckFrameClickableLiveData = MutableLiveData<Boolean>()
    override val deckTextLiveData = MutableLiveData<String>()
    override val redDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val blackDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val cardLeftTextLiveData = MutableLiveData<String>()
    override val deckTopCardLiveData = MutableLiveData<Card?>()
    override val redCardLiveData = MutableLiveData<Card?>()
    override val blackCardLiveData = MutableLiveData<Card?>()
    override val tenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val jackPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val queenPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val kingPoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val acePoolLiveData = MutableLiveData<Map<CardSuit, FaceCardState>>()
    override val collectedCardsLiveData = MutableLiveData<Map<CardSuit, Card?>>()
    private var eligibleIndexes= mutableListOf<Int>()
    private var gameState : GameState = GameState

    init {
    }

    override fun onDeckFrameClick(view: View) {
        if (gameState.canDraw()) {
            gameState.drawACard()
        } else if (gameState.deckTopCard?.isNumberCard() == true){
            gameState.discardDeckTopCard()
        }
        emptyDeckCheck()
        updateLiveData()
    }

    private fun updateLiveData() {
        gameState.deckTopCard.let{deckTopCard->
            topCardVisibilityLiveData.value = deckTopCard?.let { View.VISIBLE } ?: View.GONE
            deckTopCardLiveData.value = deckTopCard
            gameState.redCard.let {
                redDiscardTextVisibilityLiveData.value = deckTopCard?.let {
                    View.VISIBLE
                } ?: View.GONE
                redCardLiveData.value = it
            }
            gameState.blackCard.let {
                blackDiscardTextVisibilityLiveData.value = deckTopCard?.let {
                    View.VISIBLE
                } ?: View.GONE
                blackCardLiveData.value = it
            }
        }

        tenPoolLiveData.value = gameState.tenPool
        jackPoolLiveData.value = gameState.jackPool
        queenPoolLiveData.value = gameState.queenPool
        kingPoolLiveData.value = gameState.kingPool
        acePoolLiveData.value = gameState.acePool
        collectedCardsLiveData.value = gameState.collectedCards
        cardLeftTextLiveData.value = gameState.deck.size.toString()
    }

    private fun emptyDeckCheck() {
        if (deck.size == 0) {
            topCardVisibilityLiveData.value = View.INVISIBLE
            deckFrameClickableLiveData.value = false
            deckFrameVisibilityLiveData.value = View.GONE
            deckTextLiveData.value = "Press New Game or close app to submit your score"
        }
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