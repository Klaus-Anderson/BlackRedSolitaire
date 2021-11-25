package gms.angus.angussoli.view

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gms.angus.angussoli.R
import gms.angus.angussoli.databinding.FragmentToPlayBinding

class ToPlayFragment : Fragment() {
    var hintNumber = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentToPlayBinding.inflate(inflater, container, false)
        binding.nextButton.setOnClickListener {
            if (hintNumber != 9) {
                hintNumber++
                onButtonClick(binding)
            }
        }
        binding.previousButton.setOnClickListener {
            if (hintNumber != 1) {
                hintNumber--
                onButtonClick(binding)
            }
        }
        return binding.root
    }

    private fun onButtonClick(binding: FragmentToPlayBinding) {
        val hintTextview = binding.hintTextview
        val titleTextview = binding.titleTextview
        val hintImageview = binding.hintImageview
        val previousButton = binding.previousButton
        val nextButton = binding.nextButton
        val toPlayFragmentLayout = binding.root
        when (hintNumber) {
            1 -> {
                hintTextview.setText(R.string.hintOne)
                titleTextview.setText(R.string.hintOneTitle)
                previousButton.visibility = View.INVISIBLE
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_deck)
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_one, null
                    )
                )
            }
            2 -> {
                hintTextview.setText(R.string.hintTwo)
                titleTextview.setText(R.string.hintTwoTitle)
                previousButton.visibility = View.VISIBLE
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_locked)
                hintImageview.setImageDrawable(
                    resources.getDrawable(R.drawable.hint_two, null)
                )
            }
            3 -> {
                hintTextview.setText(R.string.hintThree)
                titleTextview.setText(R.string.hintThreeTitle)
                TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.greyTextColor, it, true)
                    it.data
                }.let {
                    hintTextview.setTextColor(it)
                    titleTextview.setTextColor(it)
                }
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_deck)
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_three, null
                    )
                )
            }
            4 -> {
                hintTextview.setText(R.string.hintFour)
                titleTextview.setText(R.string.hintFourTitle)
                TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.negativeTextColor, it, true)
                    it.data
                }.let {
                    hintTextview.setTextColor(it)
                    titleTextview.setTextColor(it)
                }
                toPlayFragmentLayout.setBackgroundColor(TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.backgroundColor, it, true)
                    it.data
                })
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_four, null
                    )
                )
            }
            5 -> {
                hintTextview.setText(R.string.hintFive)
                titleTextview.setText(R.string.hintFiveTitle)
                TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.positiveTextColor, it, true)
                    it.data
                }.let{
                    hintTextview.setTextColor(it)
                    titleTextview.setTextColor(it)
                }
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_current)
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_five, null
                    )
                )
            }
            6 -> {
                hintTextview.setText(R.string.hintSix)
                titleTextview.setText(R.string.hintSixTitle)
                TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.positiveTextColor, it, true)
                    it.data
                }.let {
                    hintTextview.setTextColor(it)
                    titleTextview.setTextColor(it)
                }
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_cleared)
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_six, null
                    )
                )
            }
            7 -> {
                hintTextview.setText(R.string.hintSeven)
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_cleared)
                titleTextview.setText(R.string.hintSevenTitle)
                TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.positiveTextColor, it, true)
                    it.data
                }.let{
                    hintTextview.setTextColor(it)
                    titleTextview.setTextColor(it)
                }
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_seven, null
                    )
                )
            }
            8 -> {
                hintTextview.setText(R.string.hintEight)
                toPlayFragmentLayout.setBackgroundResource(R.drawable.zone_broken)
                titleTextview.setText(R.string.hintEightTitle)
                TypedValue().let{
                    activity?.theme?.resolveAttribute(R.attr.lockedZoneBackgroundColor, it, true)
                    it.data
                }.let{
                    hintTextview.setTextColor(it)
                    titleTextview.setTextColor(it)
                }
                nextButton.visibility = View.VISIBLE
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_eight, null
                    )
                )
            }
            9 -> {
                hintTextview.setText(R.string.hintNine)
                titleTextview.setText(R.string.hintNineTitle)
                nextButton.visibility = View.GONE
                hintImageview.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.hint_nine, null
                    )
                )
            }
        }
    }
}