package gms.angus.angussoli.view

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import com.google.android.gms.tasks.OnCompleteListener
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FaceZoneLayoutBinding
import gms.angus.angussoli.databinding.FragmentGameBinding
import gms.angus.angussoli.model.Card
import gms.angus.angussoli.model.CardSuit
import gms.angus.angussoli.model.CardValue
import gms.angus.angussoli.model.FaceCardState
import gms.angus.angussoli.viewmodel.GameViewModel
import gms.angus.angussoli.viewmodel.impl.GameViewModelImpl


@Suppress("unused")
class GameFragment : Fragment() {

    lateinit var gameViewModel: GameViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        gameViewModel = ViewModelProvider(
            viewModelStore,
            GameViewModel.GameViewModelFactory(activity!!.application)
        )[GameViewModelImpl::class.java]
        gameViewModel.loadCardBitmapMap(activity as Activity)

        binding.viewModel = gameViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.jacksZone.apply {
            faceZonePool.background = activity?.getDrawable(R.drawable.zone_current)
            zoneTitleTextView.text = getString(R.string.jack)
        }
        binding.queensZone.apply {
            faceZonePool.background = activity?.getDrawable(R.drawable.zone_locked)
            zoneTitleTextView.text = getString(R.string.queen)
        }
        binding.kingsZone.apply {
            faceZonePool.background = activity?.getDrawable(R.drawable.zone_locked)
            zoneTitleTextView.text = getString(R.string.king)
        }
        binding.acesZone.apply {
            faceZonePool.background = activity?.getDrawable(R.drawable.zone_locked)
            zoneTitleTextView.text = getString(R.string.ace)
        }
        binding.pileZone.apply {
            faceZonePool.background = activity?.getDrawable(R.drawable.zone_current)
            zoneTitleTextView.text = getString(R.string.pile)
        }

        binding.newGameButton.setOnLongClickListener {
            Games.getAchievementsClient(activity as Activity,
                GoogleSignIn.getLastSignedInAccount((activity as Activity))!!)
                .achievementsIntent
                .addOnSuccessListener { intent -> startActivityForResult(intent, GameActivity.RC_ACHIEVEMENT_UI) }
            true
        }

        gameViewModel.deckTopCardLiveData.observe(viewLifecycleOwner) { card ->
            card?.let {
                addCardImageToImageView(binding.topCard, it, gameViewModel)
            } ?: binding.topCard.setImageResource(TypedValue().let {
                activity?.theme?.resolveAttribute(R.attr.cardBackDrawable, it, true)
                it.resourceId
            })
            gameViewModel.testAchievements(activity as Activity)
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
                        CardSuit.CLUB -> faceZoneClubsImageview
                        CardSuit.DIAMOND -> faceZoneDiamondsImageview
                        CardSuit.SPADE -> faceZoneSpadesImageview
                        CardSuit.HEART -> faceZoneHeartImageview
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
            }.forEach { faceZoneLayoutBinding ->
                faceZoneLayoutBinding.faceZonePool.setBackgroundResource(R.drawable.zone_cleared)
            }
        }

        gameViewModel.currentLevelLiveData.observe(viewLifecycleOwner) { cardValue ->
            cardValue?.let {
                when (it) {
                    CardValue.TEN -> binding.tensZone
                    CardValue.JACK -> binding.jacksZone
                    CardValue.QUEEN -> binding.queensZone
                    CardValue.KING -> binding.kingsZone
                    CardValue.ACE -> binding.acesZone
                    else -> throw IllegalStateException()
                }
            }?.run {
                faceZonePool.setBackgroundResource(R.drawable.zone_current)
            }
        }

        gameViewModel.brokenFaceValueLiveData.observe(viewLifecycleOwner) { cardValue ->
            cardValue?.let {
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
                faceZonePool.setBackgroundResource(R.drawable.zone_broken)
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
        binding: FaceZoneLayoutBinding,
        mapEntry: Map.Entry<CardSuit, FaceCardState>,
        cardValue: CardValue,
        gameViewModel: GameViewModel
    ) {
        binding.let {
            when (mapEntry.key) {
                CardSuit.SPADE -> it.faceZoneSpadesImageview
                CardSuit.CLUB -> it.faceZoneClubsImageview
                CardSuit.DIAMOND -> it.faceZoneDiamondsImageview
                CardSuit.HEART -> it.faceZoneHeartImageview
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
                        setImageResource(TypedValue().let {
                            activity?.theme?.resolveAttribute(R.attr.cardBackDrawable, it, true)
                            it.resourceId
                        })
                        visibility = View.VISIBLE
                    }
                    setBackgroundResource(0)
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


    override fun onDestroy() {
        super.onDestroy()
    }
}
