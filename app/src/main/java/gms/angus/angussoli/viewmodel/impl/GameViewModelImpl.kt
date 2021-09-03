package gms.angus.angussoli.viewmodel.impl

import android.app.Application
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gms.angus.angussoli.R
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.GameState
import gms.angus.angussoli.view.GameActivity
import gms.angus.angussoli.view.ToPlayFragment
import gms.angus.angussoli.viewmodel.GameViewModel
import java.util.*

class GameViewModelImpl(val application: Application) : GameViewModel, AndroidViewModel(application) {
    override val topCardVisibilityLiveData = MutableLiveData<Int>()
    override val deckFrameVisibilityLiveData = MutableLiveData<Int>()
    override val deckFrameClickableLiveData = MutableLiveData<Boolean>()
    override val deckTextLiveData = MutableLiveData<String>()
    override val redDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val blackDiscardTextVisibilityLiveData = MutableLiveData<Int>()
    override val cardLeftTextLiveData = MutableLiveData<String>()
    private var eligibleIndexes= mutableListOf<Int>()


    init {
        val gameState = GameState
    }

    override fun onDeckFrameClick(view: View) {
        if (deck.size != 0
//            && !hasDrawn
        ) {
            // create an imageview that's of tyhe card drawn
            val drawnCard = ImageView(view.context).apply {
                deck.peek().imageName
                setImageResource(application.resources.getIdentifier(
                    drawableName, "drawable", application.packageName
                ))
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            if (deck.peek().cardValue >= Card.TEN_VALUE) {
                // it's a face card
                placeFaceCard(deck.pop());
            } else {
                placeColorCard(deck.peek())

            }
        } else if (deck.size != 0) {
            discardDrawnCard()

        }
        emptyDeckCheck()
    }

    private fun discardDrawnCard() {
        redDiscardTextVisibilityLiveData.value = View.INVISIBLE
        blackDiscardTextVisibilityLiveData.value = View.INVISIBLE
        deck.pop()
        findEligibleFaces()

    }

    private fun placeColorCard(card: Card) {
        remainingCards[deck.peek().cardSuit].text = newCount.toString()
        newCount = (cardsLeftText.text.toString() + "").toInt() - 1
        cardsLeftText.text = newCount.toString()
        if (deck.peek().isBlack) {
            if (colorCards[Card.COLOR_BLACK] == null) {
                blackFrame.addView(drawnCard)
                colorCards[Card.COLOR_BLACK] = deck.pop()
                cardsLeftText.text = deck.size.toString()
                findEligibleFaces()
            } else {
                deckFrame.addView(drawnCard)
                blackDiscardText.visibility = View.VISIBLE
            }
        } else {
            if (colorCards[Card.COLOR_RED] == null) {
                redFrame.addView(drawnCard)
                colorCards[Card.COLOR_RED] = deck.pop()
                cardsLeftText.text = deck.size.toString()
                findEligibleFaces()
            } else {
                deckFrame.addView(drawnCard)
                redDiscardText.visibility = View.VISIBLE
            }
        }
    }

    private fun placeFaceCard(card: Card) {
        val frameIndex = ((card.cardValue - 10) * 4
                + card.cardSuit)
        val moveToFrame = getFaceFrameLayoutId(frameIndex);
        faceFrames[frameIndex]
        if (card.cardValue != 10 + brokenLevel) {
            moveToFrame.addView(drawnCard)
            moveToFrame.isClickable = true
        } else {
            val face_down = ImageView(this)
            face_down.setImageResource(R.drawable.card_back)
            moveToFrame.addView(face_down)
        }
        cardLeftTextLiveData.value = deck.size.toString()
        if (level < 5) checkIfFaceIsEligible(frameIndex) else moveToFrame.isClickable = false
    }

    private fun emptyDeckCheck() {
        if (deck.size == 0) {
            topCardVisibilityLiveData.value = View.INVISIBLE
            deckFrameClickableLiveData.value = false
            deckFrameVisibilityLiveData.value = View.GONE
            deckTextLiveData.value = "Press New Game or close app to submit your score"
        }
    }

    private fun checkIfFaceIsEligible(frameIndex: Int) {
        if (faceFrames[frameIndex] != null && faceFrames[frameIndex].childCount != 0 &&
            faceFrames[frameIndex].isClickable
        ) {
            if (colorCards[Card.COLOR_BLACK] != null && colorCards[Card.COLOR_RED] != null && frameIndex / 4 <= level &&
                frameIndex % 4 == colorCards[frameIndex % 2]!!.suit &&
                pileFrames[frameIndex % 4].childCount == 0) {
                eligibleIndexes.add(frameIndex)
                faceFrames[frameIndex].setBackgroundResource(R.drawable.face_eligible_shape)
            } else if (frameIndex / 4 < level && frameIndex / 4 != brokenLevel) {
                faceFrames[frameIndex].setBackgroundResource(R.drawable.number_eligible_shape)
            }
        }
    }

    private fun findEligibleFaces() {
        if (level < 5) {
            for (i in eligibleIndexes) {
                faceFrames[i].background = null
            }
            eligibleIndexes.clear()
            for (i in 0 until 4 * (level + 1)) {
                checkIfFaceIsEligible(i)
            }
        } else {
            for (i in eligibleIndexes) {
                faceFrames[i].background = null
                faceFrames[i].isClickable = false
            }
            eligibleIndexes.clear()
        }
    }

    override fun onBreakClick(view: View) {
        view.isClickable = false
        view.visibility = View.INVISIBLE
        for (i in 0..3) {
            val index = level * 4 + i
            val frame = faceFrames[level * 4 + i]
            frame.isClickable = false
            faceFrames[index] = frame
        }
        brokenLevel = level
        increaseLevel(true)
//        unlockAchievement(getString(R.string.achievement_break))
    }

    override fun onToPlayClick(view: View) {
        fragmentManager.beginTransaction()
            .add(R.id.container, ToPlayFragment(), ToPlayFragment::class.java.simpleName)
            .addToBackStack(ToPlayFragment::class.java.simpleName).commit()
    }

    override fun onNewGameClick(view: View) {
        val i = Intent(this@GameActivity, GameActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onColorFrameClick(view: View) {
        if (hasDrawn) {
            val frame = v as FrameLayout
            if (i == Card.COLOR_BLACK && deck.peek().isBlack) {
                colorFrameDiscardClick(frame)
            } else if (i == 1 && deck.peek().isRed) {
                colorFrameDiscardClick(frame)
            }
        }
        emptyDeckCheck()
    }

    override fun onFaceFrameClick(view: View) {
        if (!hasDrawn) {
            val suit = i % 4
            val faceFrame = v as FrameLayout
            val pileFrame = pileFrames.get(suit)
            val sameColorCard = colorCards.get(suit % 2)
            val otherColorCard = colorCards.get((suit + 1) % 2)

            // use Face Card as Face Card
            if (sameColorCard != null && sameColorCard.suit == suit && otherColorCard != null && faceFrame.getChildAt(
                    0
                ) != null && !isFrameCardSet(pileFrame) && (i < 4 || i / 4 <= level)
            ) {
                val dummy = faceFrame.getChildAt(0) as ImageView
                faceFrame.removeView(dummy)
                pileFrame.addView(dummy)
                blackFrame.removeAllViews()
                redFrame.removeAllViews()
                pileTotal = pileTotal + colorCards.get(Card.COLOR_BLACK)!!.value +
                        colorCards.get(Card.COLOR_RED)!!.value
                pileText.text = pileTotal.toString()
                colorCards.set(Card.COLOR_BLACK, null)
                colorCards.set(Card.COLOR_RED, null)

                // pile check
                if (isFrameCardSet(pile_clubs) && isFrameCardSet(pile_diamonds) &&
                    isFrameCardSet(pile_spades) && isFrameCardSet(pile_hearts)
                ) {
                    scoreTotal = scoreTotal +
                            pileTotal * (level - if (brokenLevel != -1) 1 else 0)
//                                submitScore()
//                                if (scoreTotal >= 75) {
//                                    unlockAchievement(getString(R.string.achievement_75_points))
//                                    if (scoreTotal >= 100) {
//                                        unlockAchievement(getString(R.string.achievement_100_points))
//                                        if (scoreTotal >= 150) {
//                                            unlockAchievement(getString(R.string.achievement_150_points))
//                                            if (scoreTotal >= 200) {
//                                                unlockAchievement(getString(R.string.achievement_200_points))
//                                                if (scoreTotal >= 300) {
//                                                    unlockAchievement(getString(R.string.achievement_300_points))
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
                    scoreText.text = scoreTotal.toString()
                    pileTotal = 0
                    pileText.text = pileTotal.toString()
                    pile_clubs.removeAllViews()
                    pile_spades.removeAllViews()
                    pile_hearts.removeAllViews()
                    pile_diamonds.removeAllViews()
                    increaseLevel(false)
                }
                findEligibleFaces()
                setUsedFaceCard(faceFrame, suit, i)
            } else if (isFrameCardSet(faceFrame)
                && (i < 4 || i / 4 < level)
            ) {
                val colorFrame = colorFrames.get(i % 2)
                var dummy = colorFrame.getChildAt(0) as ImageView
                if (dummy != null) {
                    colorFrame.removeView(dummy)
                    discardFrame.addView(dummy)
                }
                dummy = faceFrame.getChildAt(0) as ImageView
                faceFrame.removeView(dummy)
                colorFrame.addView(dummy)
                colorCards.set(
                    suit % 2,
                    Card(Card.TEN_VALUE, suit)
                )
                findEligibleFaces()
                setUsedFaceCard(faceFrame, suit, i)
            }
        }
    }
}