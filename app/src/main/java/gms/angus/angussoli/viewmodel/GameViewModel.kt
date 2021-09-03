package gms.angus.angussoli.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface GameViewModel {
    val topCardVisibilityLiveData: LiveData<Int>
    val deckFrameVisibilityLiveData: LiveData<Int>
    val deckFrameClickableLiveData: LiveData<Boolean>
    val deckTextLiveData: LiveData<String>

    fun onDeckFrameClick(view: View)
    fun onBreakClick(view: View)
    fun onToPlayClick(view: View)
    fun onNewGameClick(view: View)
    fun onColorFrameClick(view: View)
    fun onFaceFrameClick(view: View)
    val redDiscardTextVisibilityLiveData: MutableLiveData<Int>
    val blackDiscardTextVisibilityLiveData: MutableLiveData<Int>
    val cardLeftTextLiveData: MutableLiveData<String>
}