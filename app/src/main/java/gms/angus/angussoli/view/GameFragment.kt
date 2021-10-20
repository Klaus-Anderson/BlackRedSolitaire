package gms.angus.angussoli.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        val gameViewModel: GameViewModel = ViewModelProvider(
            viewModelStore,
            GameViewModel.GameViewModelFactory(activity!!.application))[GameViewModelImpl::class.java]

        binding.viewModel = gameViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tensPool.apply {
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.TEN)
        }
        binding.jacksPool.apply {
            root.background = activity?.getDrawable(R.drawable.current_level_shape)
            poolTextView.text = getString(R.string.jack)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.JACK)
        }
        binding.queensPool.apply {
            root.background = activity?.getDrawable(R.drawable.locked_level_shape)
            poolTextView.text = getString(R.string.queen)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.QUEEN)
        }
        binding.kingsPool.apply {
            root.background = activity?.getDrawable(R.drawable.locked_level_shape)
            poolTextView.text = getString(R.string.king)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.KING)
        }
        binding.acesPool.apply {
            root.background = activity?.getDrawable(R.drawable.locked_level_shape)
            poolTextView.text = getString(R.string.ace)
            setFaceCardOnClickListeners(gameViewModel, this, CardValue.ACE)
        }
        binding.pilePool.apply {
            root.background = activity?.getDrawable(R.drawable.current_level_shape)
            poolTextView.text = getString(R.string.pile)
        }
        binding.newGameButton.setOnClickListener {
            gameViewModel.endGame()
            activity?.let{
                startActivity(Intent(it,GameActivity::class.java))
                it.finish()
            }
        }

        gameViewModel.deckTopCardLiveData.observe(viewLifecycleOwner) {
            binding.topCard.setImageResource(it?.getDrawableResourceId() ?: R.drawable.card_back)
        }
        gameViewModel.redCardLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.redFrame.addView(ImageView(context).apply {
                    setImageResource(
                        it.getDrawableResourceId()
                    )
                })
            } ?: binding.redFrame.removeAllViews()
        }
        gameViewModel.blackCardLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.blackFrame.addView(ImageView(context).apply {
                    setImageResource(it.getDrawableResourceId())
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
                                setImageResource(
                                    it.getDrawableResourceId()
                                )
                            })
                        }
                    } ?: removeAllViews()
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
                it.root.setBackgroundResource(R.drawable.face_number_shape)
            }
        }

        gameViewModel.currentLevelLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.levelText.text = it.identity
                when (it) {
                    CardValue.TEN -> binding.tensPool
                    CardValue.JACK -> binding.jacksPool
                    CardValue.QUEEN -> binding.queensPool
                    CardValue.KING -> binding.kingsPool
                    CardValue.ACE -> binding.acesPool
                    else -> throw IllegalStateException()
                }
            }?.root?.setBackgroundResource(R.drawable.current_level_shape) ?: run{
                //game won code
            }
        }

        gameViewModel.brokenFaceValueLiveData.observe(viewLifecycleOwner) {
            it?.let{
                binding.breakButton.visibility = View.INVISIBLE
                when (it) {
                    CardValue.TEN -> binding.tensPool
                    CardValue.JACK -> binding.jacksPool
                    CardValue.QUEEN -> binding.queensPool
                    CardValue.KING -> binding.kingsPool
                    CardValue.ACE -> binding.acesPool
                    else -> throw IllegalStateException()
                }
            }?.root?.setBackgroundResource(R.drawable.broken_shape)
        }

        return binding.root
    }

    private fun setFaceCardOnClickListeners(
        gameViewModel: GameViewModel,
        poolLayoutBinding: PoolLayoutBinding,
        cardValue: CardValue
    ) {
        poolLayoutBinding.apply {
            poolClubs.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.CLUB)
            }
            poolSpades.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.SPADE)
            }
            poolDiamonds.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.DIAMOND)
            }
            poolHearts.setOnClickListener {
                gameViewModel.onFaceCardClick(cardValue, CardSuit.HEART)
            }
        }
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
                    setBackgroundResource(0)
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
                            setBackgroundResource(0)
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
                    setImageResource(
                        Card(cardValue, cardSuit)
                            .getDrawableResourceId()
                    )
                })
            }
        }
    }
}
