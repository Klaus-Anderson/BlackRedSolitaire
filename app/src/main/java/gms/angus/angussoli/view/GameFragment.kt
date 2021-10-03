package gms.angus.angussoli.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FragmentGameBinding
import gms.angus.angussoli.databinding.PoolLayoutBinding
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.CardValue
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.viewmodel.GameViewModel
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl

class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        val gameViewModel : GameViewModel = ViewModelProvider(viewModelStore,
            GameViewModel.GameViewModelFactory(activity!!.application))[GameViewModelImpl::class.java]
        binding.viewModel = gameViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        gameViewModel.deckTopCardLiveData.observe(viewLifecycleOwner) {
            binding.topCard.setImageResource(it?.let {
                getDrawableResourceId(it.cardValue, it.cardSuit)
            } ?: R.drawable.card_back)
        }
        gameViewModel.redCardLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.redFrame.addView(ImageView(context).apply {
                    setImageResource(getDrawableResourceId(it.cardValue, it.cardSuit))
                })
            } ?: binding.redFrame.removeAllViews()
        }
        gameViewModel.blackCardLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.blackFrame.addView(ImageView(context).apply {
                    setImageResource(getDrawableResourceId(it.cardValue, it.cardSuit))
                })
            } ?: binding.blackFrame.removeAllViews()
        }

        gameViewModel.tenPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.tensPool as PoolLayoutBinding, mapEntry, CardValue.TEN)
            }
        }
        gameViewModel.jackPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.jacksPool as PoolLayoutBinding, mapEntry, CardValue.JACK)
            }
        }
        gameViewModel.queenPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.queensPool as PoolLayoutBinding, mapEntry, CardValue.QUEEN)
            }
        }
        gameViewModel.kingPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.kingsPool as PoolLayoutBinding, mapEntry, CardValue.KING)
            }
        }
        gameViewModel.acePoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.acesPool as PoolLayoutBinding, mapEntry, CardValue.ACE)
            }
        }

        gameViewModel.collectedCardsLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach {
                binding.pilePool.run {
                    when (it.key) {
                        CardSuit.CLUB -> poolClubs
                        CardSuit.DIAMOND -> poolDiamonds
                        CardSuit.SPADE -> poolSpades
                        CardSuit.HEART -> poolHearts
                    }
                }.run {
                    it.value?.let {
                        if (childCount == 0) {
                            addView(ImageView(context).apply {
                                setImageResource(getDrawableResourceId(it.cardValue, it.cardSuit))
                            })
                        }
                    } ?: removeAllViews()
                }
            }
        }

        return binding.root
    }

    private fun setFaceCard(
        binding: PoolLayoutBinding,
        mapEntry: Map.Entry<CardSuit, FaceCardState>,
        cardValue: CardValue
    ) {
        binding.let {
            when (mapEntry.key) {
                CardSuit.SPADE -> it.poolSpades
                CardSuit.CLUB -> it.poolClubs
                CardSuit.DIAMOND -> it.poolDiamonds
                CardSuit.HEART -> it.poolHearts
            }
        }.run {
            when (mapEntry.value) {
                FaceCardState.NOT_DRAWN -> removeAllViews()
                FaceCardState.NOT_USABLE -> run {
                    addFaceCardIfNecessary(mapEntry.key, cardValue, this)
                }
                FaceCardState.USABLE_AS_FACE -> run {
                    addFaceCardIfNecessary(mapEntry.key, cardValue, this)
                    setBackgroundResource(R.drawable.face_eligible_shape)
                }
                FaceCardState.USABLE_AS_COLOR -> run {
                    addFaceCardIfNecessary(mapEntry.key, cardValue, this)
                    setBackgroundResource(R.drawable.number_eligible_shape)
                }
                FaceCardState.BROKEN -> run {
                    if (childCount == 0) {
                        addView(ImageView(context).apply {
                            setImageResource(R.drawable.card_back)
                            setBackgroundResource(R.drawable.face_eligible_shape)
                        })
                    }
                }
                FaceCardState.USED -> run {
                    removeAllViews()
                    setBackgroundResource(R.drawable.face_cover_shape)
                    addView(ImageView(context).apply {
                        setImageResource(
                            when (mapEntry.key) {
                                CardSuit.SPADE -> R.drawable.spades
                                CardSuit.CLUB -> R.drawable.club
                                CardSuit.DIAMOND -> R.drawable.diamond
                                CardSuit.HEART -> R.drawable.heart
                            }
                        )
                    })
                }
            }
        }
    }

    private fun setAsUsed(cardSuit: CardSuit, frameLayout: FrameLayout) {

    }

    private fun addFaceCardIfNecessary(
        cardSuit: CardSuit,
        cardValue: CardValue,
        frameLayout: FrameLayout
    ) {
        (frameLayout.childCount == 0).let {
            if (it) {
                frameLayout.addView(ImageView(context).apply {
                    setImageResource(getDrawableResourceId(cardValue, cardSuit))
                })
            }
        }
    }
}

private fun getDrawableResourceId(cardValue: CardValue, cardSuit: CardSuit): Int {
    return when (cardValue) {
        CardValue.TWO -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.two_clubs
            CardSuit.DIAMOND -> R.drawable.two_diamonds
            CardSuit.SPADE -> R.drawable.two_spades
            CardSuit.HEART -> R.drawable.two_hearts
        }
        CardValue.THREE -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.three_clubs
            CardSuit.DIAMOND -> R.drawable.three_diamonds
            CardSuit.SPADE -> R.drawable.three_spades
            CardSuit.HEART -> R.drawable.three_hearts
        }
        CardValue.FOUR -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.four_clubs
            CardSuit.DIAMOND -> R.drawable.four_diamonds
            CardSuit.SPADE -> R.drawable.four_spades
            CardSuit.HEART -> R.drawable.four_hearts
        }
        CardValue.FIVE -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.five_clubs
            CardSuit.DIAMOND -> R.drawable.five_diamonds
            CardSuit.SPADE -> R.drawable.five_spades
            CardSuit.HEART -> R.drawable.five_hearts
        }
        CardValue.SIX -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.six_clubs
            CardSuit.DIAMOND -> R.drawable.six_diamonds
            CardSuit.SPADE -> R.drawable.six_spades
            CardSuit.HEART -> R.drawable.six_hearts
        }
        CardValue.SEVEN -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.seven_clubs
            CardSuit.DIAMOND -> R.drawable.seven_diamonds
            CardSuit.SPADE -> R.drawable.seven_spades
            CardSuit.HEART -> R.drawable.seven_hearts
        }
        CardValue.EIGHT -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.eight_clubs
            CardSuit.DIAMOND -> R.drawable.eight_diamonds
            CardSuit.SPADE -> R.drawable.eight_spades
            CardSuit.HEART -> R.drawable.eight_hearts
        }
        CardValue.NINE -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.nine_clubs
            CardSuit.DIAMOND -> R.drawable.nine_diamonds
            CardSuit.SPADE -> R.drawable.nine_spades
            CardSuit.HEART -> R.drawable.nine_hearts
        }
        CardValue.TEN -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.ten_clubs
            CardSuit.DIAMOND -> R.drawable.ten_diamonds
            CardSuit.SPADE -> R.drawable.ten_spades
            CardSuit.HEART -> R.drawable.ten_hearts
        }
        CardValue.JACK -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.jack_clubs
            CardSuit.DIAMOND -> R.drawable.jack_diamonds
            CardSuit.SPADE -> R.drawable.jack_spades
            CardSuit.HEART -> R.drawable.jack_hearts
        }
        CardValue.QUEEN -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.queen_clubs
            CardSuit.DIAMOND -> R.drawable.queen_diamonds
            CardSuit.SPADE -> R.drawable.queen_spades
            CardSuit.HEART -> R.drawable.queen_hearts
        }
        CardValue.KING -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.king_clubs
            CardSuit.DIAMOND -> R.drawable.king_diamonds
            CardSuit.SPADE -> R.drawable.king_spades
            CardSuit.HEART -> R.drawable.king_hearts
        }
        CardValue.ACE -> when (cardSuit) {
            CardSuit.CLUB -> R.drawable.ace_clubs
            CardSuit.DIAMOND -> R.drawable.ace_diamonds
            CardSuit.SPADE -> R.drawable.ace_spades
            CardSuit.HEART -> R.drawable.ace_hearts
        }
    }
}