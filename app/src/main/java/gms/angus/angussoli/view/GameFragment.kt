package gms.angus.angussoli.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FragmentGameBinding
import gms.angus.angussoli.databinding.ZoneLayoutBinding
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.CardValue
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.viewmodel.GameViewModel
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl


class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        val gameViewModel: GameViewModel = ViewModelProvider(
            viewModelStore,
            GameViewModel.GameViewModelFactory(activity!!.application)
        )[GameViewModelImpl::class.java]

        binding.viewModel = gameViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.jacksZone.apply {
            root.background = activity?.getDrawable(R.drawable.zone_current)
            zoneTextView.text = getString(R.string.jack)
        }
        binding.queensZone.apply {
            root.background = activity?.getDrawable(R.drawable.zone_locked)
            zoneTextView.text = getString(R.string.queen)
        }
        binding.kingsZone.apply {
            root.background = activity?.getDrawable(R.drawable.zone_locked)
            zoneTextView.text = getString(R.string.king)
        }
        binding.acesZone.apply {
            root.background = activity?.getDrawable(R.drawable.zone_locked)
            zoneTextView.text = getString(R.string.ace)
        }
        binding.pileZone.apply {
            root.background = activity?.getDrawable(R.drawable.zone_current)
            zoneTextView.text = getString(R.string.pile)
            zoneTextView.setTextColor(Color.BLACK)
        }

        binding.newGameButton.setOnLongClickListener {
            gameViewModel.enableCompleteMode(it.context)
            true
        }

        gameViewModel.deckTopCardLiveData.observe(viewLifecycleOwner) {
            it?.let {
                addCardImageToImageView(binding.topCard, it, gameViewModel)
            } ?: binding.topCard.setImageResource(R.drawable.card_back)
        }

        gameViewModel.loadingSpinnerVisibilityLiveData.observe(viewLifecycleOwner) {
            if (it == View.GONE) {
                binding.container.visibility = View.VISIBLE
            }
        }

        gameViewModel.redCardLiveData.observe(viewLifecycleOwner) { nullableCard ->
            nullableCard?.let { card ->
                addCardImageToImageView(binding.redFrame, card, gameViewModel)
            } ?: run {
                binding.redFrame.visibility = View.INVISIBLE
            }
        }

        gameViewModel.blackCardLiveData.observe(viewLifecycleOwner) { nullableCard ->
            nullableCard?.let { card ->
                addCardImageToImageView(binding.blackFrame, card, gameViewModel)
            } ?: run {
                binding.blackFrame.visibility = View.INVISIBLE
            }
        }

        gameViewModel.tenZoneLiveData.observe(viewLifecycleOwner) { zoneMap ->
            zoneMap.forEach { mapEntry ->
                setFaceCard(binding.tensZone, mapEntry, CardValue.TEN, gameViewModel)
            }
        }
        gameViewModel.jackZoneLiveData.observe(viewLifecycleOwner) { zoneMap ->
            zoneMap.forEach { mapEntry ->
                setFaceCard(binding.jacksZone, mapEntry, CardValue.JACK, gameViewModel)
            }
        }
        gameViewModel.queenZoneLiveData.observe(viewLifecycleOwner) { zoneMap ->
            zoneMap.forEach { mapEntry ->
                setFaceCard(binding.queensZone, mapEntry, CardValue.QUEEN, gameViewModel)
            }
        }
        gameViewModel.kingZoneLiveData.observe(viewLifecycleOwner) { zoneMap ->
            zoneMap.forEach { mapEntry ->
                setFaceCard(binding.kingsZone, mapEntry, CardValue.KING, gameViewModel)
            }
        }
        gameViewModel.aceZoneLiveData.observe(viewLifecycleOwner) { zoneMap ->
            zoneMap.forEach { mapEntry ->
                setFaceCard(binding.acesZone, mapEntry, CardValue.ACE, gameViewModel)
            }
        }

        gameViewModel.collectedCardsLiveData.observe(viewLifecycleOwner) { zoneMap ->
            zoneMap.forEach {
                binding.pileZone.run {
                    when (it.key) {
                        CardSuit.CLUB -> zoneClubs
                        CardSuit.DIAMOND -> zoneDiamonds
                        CardSuit.SPADE -> zoneSpades
                        CardSuit.HEART -> zoneHearts
                    }
                }.run {
                    it.value?.let { card ->
                        if (visibility == View.INVISIBLE) {
                            addCardImageToImageView(this, card, gameViewModel)
                        }
                    } ?: run {
                        visibility = View.INVISIBLE
                    }
                }
            }
        }

        gameViewModel.clearedFaceCardsLiveData.observe(viewLifecycleOwner) { list ->
            list.map {
                when (it) {
                    CardValue.TEN -> binding.tensZone
                    CardValue.JACK -> binding.jacksZone
                    CardValue.QUEEN -> binding.queensZone
                    CardValue.KING -> binding.kingsZone
                    CardValue.ACE -> binding.acesZone
                    else -> throw IllegalStateException()
                }
            }.forEach {
                it.root.setBackgroundResource(R.drawable.zone_cleared)
                it.zoneTextView.setTextColor(Color.BLACK)
            }
        }

        gameViewModel.currentLevelLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    CardValue.TEN -> binding.tensZone
                    CardValue.JACK -> binding.jacksZone
                    CardValue.QUEEN -> binding.queensZone
                    CardValue.KING -> binding.kingsZone
                    CardValue.ACE -> binding.acesZone
                    else -> throw IllegalStateException()
                }
            }?.run {
                root.setBackgroundResource(R.drawable.zone_current)
                zoneTextView.setTextColor(Color.BLACK)
            }
        }

        gameViewModel.brokenFaceValueLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.breakButton.visibility = View.INVISIBLE
                when (it) {
                    CardValue.TEN -> binding.tensZone
                    CardValue.JACK -> binding.jacksZone
                    CardValue.QUEEN -> binding.queensZone
                    CardValue.KING -> binding.kingsZone
                    CardValue.ACE -> binding.acesZone
                    else -> throw IllegalStateException()
                }
            }?.apply {
                root.setBackgroundResource(R.drawable.zone_broken)
                zoneTextView.setTextColor(Color.BLACK)
            }

        }
        return binding.root
    }

    private fun addCardImageToImageView(imageView: ImageView, card: Card, gameViewModel: GameViewModel) {
        gameViewModel.getCardImageBitmap(card)?.let {
            imageView.setImageBitmap(it.scale(imageView.width, imageView.height, false))
            imageView.visibility = View.VISIBLE
        }
    }

    private fun setFaceCard(
        binding: ZoneLayoutBinding,
        mapEntry: Map.Entry<CardSuit, FaceCardState>,
        cardValue: CardValue,
        gameViewModel: GameViewModel
    ) {
        binding.let {
            when (mapEntry.key) {
                CardSuit.SPADE -> it.zoneSpades
                CardSuit.CLUB -> it.zoneClubs
                CardSuit.DIAMOND -> it.zoneDiamonds
                CardSuit.HEART -> it.zoneHearts
            }
        }.run {
            when (mapEntry.value) {
                FaceCardState.NOT_DRAWN -> run {
                    visibility = View.INVISIBLE
                }
                FaceCardState.NOT_USABLE -> run {
                    addFaceCardIfNecessary(mapEntry.key, cardValue, this, gameViewModel)
                    setBackgroundResource(0)
                }
                FaceCardState.USABLE_AS_FACE -> run {
                    addFaceCardIfNecessary(mapEntry.key, cardValue, this, gameViewModel)
                    setBackgroundResource(R.drawable.outline_hint_face)
                }
                FaceCardState.USABLE_AS_COLOR -> run {
                    addFaceCardIfNecessary(mapEntry.key, cardValue, this, gameViewModel)
                    setBackgroundResource(R.drawable.outline_hint_number)
                }
                FaceCardState.BROKEN -> run {
                    if (visibility == View.INVISIBLE) {
                        setImageResource(R.drawable.card_back)
                        setBackgroundResource(0)
                        visibility = View.VISIBLE
                    }
                }
                FaceCardState.USED -> run {
                    setBackgroundResource(R.drawable.zone_cover)
                    setImageResource(
                        when (mapEntry.key) {
                            CardSuit.SPADE -> R.drawable.spades
                            CardSuit.CLUB -> R.drawable.club
                            CardSuit.DIAMOND -> R.drawable.diamond
                            CardSuit.HEART -> R.drawable.heart
                        }
                    )
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun addFaceCardIfNecessary(
        cardSuit: CardSuit,
        cardValue: CardValue,
        imageView: ImageView,
        gameViewModel: GameViewModel
    ) {
        (imageView.visibility == View.INVISIBLE).let { isInvisible ->
            if (isInvisible) {
                addCardImageToImageView(imageView, Card(cardValue, cardSuit), gameViewModel)
            }
        }
    }
}
