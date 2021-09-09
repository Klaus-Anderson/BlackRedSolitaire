package gms.angus.angussoli.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.FaceCardState

interface GameViewModel {
    val topCardVisibilityLiveData: LiveData<Int>
    val deckFrameVisibilityLiveData: LiveData<Int>
    val deckFrameClickableLiveData: LiveData<Boolean>
    val deckTextLiveData: LiveData<String>

    fun onDeckFrameClick(view: View)
    fun onBreakClick(view: View)
    fun onToPlayClick(view: View)
    fun onNewGameClick(view: View)
    val redDiscardTextVisibilityLiveData: LiveData<Int>
    val blackDiscardTextVisibilityLiveData: LiveData<Int>
    val cardLeftTextLiveData: LiveData<String>
    val deckTopCardLiveData: LiveData<Card?>
    val redCardLiveData: LiveData<Card?>
    val blackCardLiveData: LiveData<Card?>
    val tenPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val jackPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val queenPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val kingPoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val acePoolLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val collectedCardsLiveData: LiveData<Map<CardSuit, Card?>>
}