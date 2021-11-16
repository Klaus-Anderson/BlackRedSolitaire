package gms.angus.angussoli.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.CardValue
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.view.GameActivity
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl

interface GameViewModel {
    val loadingSpinnerVisibilityLiveData: LiveData<Int>
    val redDiscardTextVisibilityLiveData: LiveData<Int>
    val blackDiscardTextVisibilityLiveData: LiveData<Int>
    val deckTopCardVisibilityLiveData: LiveData<Int>
    val breakButtonVisibilityLiveData: LiveData<Int>
    val breakButtonClickableLiveData: LiveData<Boolean>
    val underDeckTextLiveData: LiveData<String>
    val cardLeftTextLiveData: LiveData<String>
    val cardSquanderedTextLiveData: LiveData<String>
    val clubNumbersLeftTextLiveData: LiveData<String>
    val spadeNumbersLeftTextLiveData: LiveData<String>
    val diamondNumbersLeftTextLiveData: LiveData<String>
    val heartNumbersLeftTextLiveData: LiveData<String>
    val scoreTextLiveData: LiveData<String>
    val multiplierTextLiveData: LiveData<String>
    val levelTextLiveData: LiveData<String>
    val pileScoreTextLiveData: LiveData<String>

    val deckTopCardLiveData: LiveData<Card?>
    val redCardLiveData: LiveData<Card?>
    val blackCardLiveData: LiveData<Card?>
    val clearedFaceCardsLiveData: LiveData<List<CardValue>>
    val currentLevelLiveData: LiveData<CardValue?>
    val brokenFaceValueLiveData: LiveData<CardValue?>
    val tenZoneLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val jackZoneLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val queenZoneLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val kingZoneLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val aceZoneLiveData: LiveData<Map<CardSuit, FaceCardState>>
    val collectedCardsLiveData: LiveData<Map<CardSuit, Card?>>

    fun onDeckFrameClick(view: View)
    fun onRedFrameClick(view: View)
    fun onBlackFrameClick(view: View)
    fun onBreakClick(view: View)
    fun onToPlayClick(view: View)
    fun onNewGameClick(view: View)
    fun onHighScoreClick(view: View)
    fun onThemeChanged(context: Context)
    fun onOptionsClick(view: View)

    fun enableCompleteMode(context: Context)
    fun onFaceCardClick(cardValue: CardValue?, cardSuit: CardSuit): View.OnClickListener
    fun getCardImageBitmap(card: Card): Bitmap?
    fun testAchievements(activity: Activity)

    class GameViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModelImpl(application) as T
        }
    }

    fun loadCardBitmapMap(activity: Activity)
}