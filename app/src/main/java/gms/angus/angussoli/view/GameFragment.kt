package gms.angus.angussoli.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import gms.angus.angussoli.BuildConfig
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FragmentGameBinding
import gms.angus.angussoli.databinding.PoolLayoutBinding
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.CardValue
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.viewmodel.GameViewModel
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl


class GameFragment : Fragment() {

    companion object{
        const val CARD_IMAGES_INTENT_FLAG = "CARD_IMAGES_INTENT_FLAG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        val gameViewModel: GameViewModel = ViewModelProvider(
            viewModelStore,
            GameViewModel.GameViewModelFactory(activity!!.application)
        )[GameViewModelImpl::class.java]

        binding.viewModel = gameViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tensPool.apply {
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.TEN)
        }
        binding.jacksPool.apply {
            root.background = activity?.getDrawable(R.drawable.zone_current)
            poolTextView.text = getString(R.string.jack)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.JACK)
        }
        binding.queensPool.apply {
            root.background = activity?.getDrawable(R.drawable.zone_locked)
            poolTextView.text = getString(R.string.queen)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.QUEEN)
        }
        binding.kingsPool.apply {
            root.background = activity?.getDrawable(R.drawable.zone_locked)
            poolTextView.text = getString(R.string.king)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.KING)
        }
        binding.acesPool.apply {
            root.background = activity?.getDrawable(R.drawable.zone_locked)
            poolTextView.text = getString(R.string.ace)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.ACE)
        }
        binding.pilePool.apply {
            root.background = activity?.getDrawable(R.drawable.zone_current)
            poolTextView.text = getString(R.string.pile)
            poolTextView.setTextColor(Color.BLACK)
        }

        binding.newGameButton.setOnClickListener {
            gameViewModel.endGame()
            activity?.let {
                startActivity(Intent(it, GameActivity::class.java))
                it.finish()
            }
        }

        if (BuildConfig.DEBUG) {
            binding.newGameButton.setOnLongClickListener {
                gameViewModel.enableCompleteMode(it.context)
                true
            }
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

        gameViewModel.tenPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.tensPool as PoolLayoutBinding, mapEntry, CardValue.TEN, gameViewModel)
            }
        }
        gameViewModel.jackPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.jacksPool as PoolLayoutBinding, mapEntry, CardValue.JACK, gameViewModel)
            }
        }
        gameViewModel.queenPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.queensPool as PoolLayoutBinding, mapEntry, CardValue.QUEEN, gameViewModel)
            }
        }
        gameViewModel.kingPoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.kingsPool as PoolLayoutBinding, mapEntry, CardValue.KING, gameViewModel)
            }
        }
        gameViewModel.acePoolLiveData.observe(viewLifecycleOwner) { poolMap ->
            poolMap.forEach { mapEntry ->
                setFaceCard(binding.acesPool as PoolLayoutBinding, mapEntry, CardValue.ACE, gameViewModel)
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
                    CardValue.TEN -> binding.tensPool
                    CardValue.JACK -> binding.jacksPool
                    CardValue.QUEEN -> binding.queensPool
                    CardValue.KING -> binding.kingsPool
                    CardValue.ACE -> binding.acesPool
                    else -> throw IllegalStateException()
                }
            }.forEach {
                it.root.setBackgroundResource(R.drawable.zone_cleared)
                it.poolTextView.setTextColor(Color.BLACK)
            }
        }

        gameViewModel.currentLevelLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.levelText.text = it.identity.first().toString().uppercase()
                when (it) {
                    CardValue.TEN -> binding.tensPool
                    CardValue.JACK -> binding.jacksPool
                    CardValue.QUEEN -> binding.queensPool
                    CardValue.KING -> binding.kingsPool
                    CardValue.ACE -> run {
                        //todo: move this to ViewModel
                        if (binding.breakButton.visibility == View.VISIBLE) {
                            binding.breakButton.visibility = View.INVISIBLE
                            binding.breakButton.isClickable = false
                        }
                        binding.levelText.text = it.identity.first().toString().uppercase() +
                                if(gameViewModel.hasNotBroken()){
                            "+"
                        } else {
                            ""
                        } + if(gameViewModel.hasMultiplierBonus()){
                            "+"
                        } else {
                            ""
                        }
                        binding.acesPool
                    }
                    else -> throw IllegalStateException()
                }
            }?.run {
                root?.setBackgroundResource(R.drawable.zone_current)
                poolTextView.setTextColor(Color.BLACK)
            } ?: run {
                binding.levelText.text = getString(R.string.done) + if(gameViewModel.hasNotBroken()){
                    "+"
                } else {
                    ""
                }
            }
        }

        gameViewModel.brokenFaceValueLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.breakButton.visibility = View.INVISIBLE
                when (it) {
                    CardValue.TEN -> binding.tensPool
                    CardValue.JACK -> binding.jacksPool
                    CardValue.QUEEN -> binding.queensPool
                    CardValue.KING -> binding.kingsPool
                    CardValue.ACE -> binding.acesPool
                    else -> throw IllegalStateException()
                }
            }?.apply {
                root?.setBackgroundResource(R.drawable.zone_broken)
                poolTextView.setTextColor(Color.BLACK)
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

    private fun setFaceCardOnClickListeners(
        gameViewModel: GameViewModel,
        poolLayoutBinding: PoolLayoutBinding,
        cardValue: CardValue
    ) {
        poolLayoutBinding.apply {
            poolClubs.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.CLUB, activity!!)
            }
            poolSpades.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.SPADE, activity!!)
            }
            poolDiamonds.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.DIAMOND, activity!!)
            }
            poolHearts.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.HEART, activity!!)
            }
        }
    }

    private fun setFaceCard(
        binding: PoolLayoutBinding,
        mapEntry: Map.Entry<CardSuit, FaceCardState>,
        cardValue: CardValue,
        gameViewModel: GameViewModel
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
