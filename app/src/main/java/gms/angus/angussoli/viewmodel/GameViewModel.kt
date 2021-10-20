package gms.angus.angussoli.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.CardValue
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl

interface GameViewModel {
    fun onDeckFrameClick(view: View)
    fun onRedFrameClick(view: View)
    fun onBlackFrameClick(view: View)
    fun onBreakClick(view: View)
    fun onToPlayClick(view: View)
    fun onNewGameClick(view: View)
    fun onFaceCardClick(cardValue: CardValue, cardSuit: CardSuit)

    val redDiscardTextVisibilityLiveData: LiveData<Int>
    val blackDiscardTextVisibilityLiveData: LiveData<Int>
    val deckTextVisibilityLiveData: LiveData<Int>
    val deckTextResIdLiveData: LiveData<Int>
    val deckTopCardVisibilityLiveData: LiveData<Int>
    val cardLeftTextLiveData: LiveData<String>
    val deckTopCardLiveData: LiveData<Card?>
    val redCardLiveData: LiveData<Card?>
    val blackCardLiveData: LiveData<Card?>
    val scoreTextLiveData: LiveData<String>
    val clearedFaceCardsLiveData: LiveData<List<CardValue>>
    val currentLevelLiveData: LiveData<CardValue?>
    val brokenFaceValueLiveData: LiveData<CardValue?>
    val tenPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val jackPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val queenPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val kingPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val acePoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val collectedCardsLiveData: LiveData<Map<CardSuit, Card?>>

    class GameViewModelFactory(val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModelImpl(application) as T
        }
    }
}