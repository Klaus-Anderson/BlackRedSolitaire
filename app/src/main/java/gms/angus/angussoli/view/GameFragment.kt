package gms.angus.angussoli.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FragmentGameBinding
import gms.angus.angussoli.viewmodel.GameViewModel

class GameFragment : Fragment(R.layout.fragment_game) {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentGameBinding.bind(view).viewModel = gameViewModel
    }
}