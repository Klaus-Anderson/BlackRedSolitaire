package gms.angus.angussoli.view

import android.app.Fragment
import butterknife.BindView
import gms.angus.angussoli.R
import android.widget.TextView
import android.widget.RelativeLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import butterknife.ButterKnife
import butterknife.OnClick

/**
 * A simple [Fragment] subclass.
 */
class ToPlayFragment : Fragment() {
//    @JvmField
//    @BindView(R.id.hint_textview)
//    var hintTextview: TextView? = null
//
//    @JvmField
//    @BindView(R.id.hint_imageview)
//    var hintImageview: ImageView? = null
//
//    @JvmField
//    @BindView(R.id.previousButton)
//    var previousButton: Button? = null
//
//    @JvmField
//    @BindView(R.id.nextButton)
//    var nextButton: Button? = null
//
//    @JvmField
//    @BindView(R.id.toPlayFragmentLayout)
//    var toPlayFragmentLayout: RelativeLayout? = null
//    private var hintNumber = 1
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_to_play, container, false)
//        ButterKnife.bind(this, view)
//
//        // Inflate the layout for this fragment
//        return view
//    }
//
//    @OnClick(R.id.nextButton)
//    fun onNextButtonClick(v: View?) {
//        if (hintNumber != 9) {
//            hintNumber++
//            onButtonClick()
//        }
//    }
//
//    @OnClick(R.id.previousButton)
//    fun onPreviousButton() {
//        if (hintNumber != 1) {
//            hintNumber--
//            onButtonClick()
//        }
//    }
//
//    private fun onButtonClick() {
//        when (hintNumber) {
//            1 -> {
//                hintTextview!!.setText(R.string.hintOne)
//                previousButton!!.visibility = View.INVISIBLE
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.bottom_row_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_one, null
//                    )
//                )
//            }
//            2 -> {
//                hintTextview!!.setText(R.string.hintTwo)
//                previousButton!!.visibility = View.VISIBLE
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.locked_level_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(R.drawable.hint_two, null)
//                )
//            }
//            3 -> {
//                hintTextview!!.setText(R.string.hintThree)
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.bottom_row_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_three, null
//                    )
//                )
//            }
//            4 -> {
//                hintTextview!!.setText(R.string.hintFour)
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.bottom_row_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_four, null
//                    )
//                )
//            }
//            5 -> {
//                hintTextview!!.setText(R.string.hintFive)
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.current_level_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_five, null
//                    )
//                )
//            }
//            6 -> {
//                hintTextview!!.setText(R.string.hintSix)
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.face_number_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_six
//                    )
//                )
//            }
//            7 -> {
//                hintTextview!!.setText(R.string.hintSeven)
//                toPlayFragmentLayout!!.setBackgroundResource(R.drawable.face_number_shape)
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_seven, null
//                    )
//                )
//            }
//            8 -> {
//                hintTextview!!.setText(R.string.hintEight)
//                nextButton!!.visibility = View.VISIBLE
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_eight, null
//                    )
//                )
//            }
//            9 -> {
//                hintTextview!!.setText(R.string.hintNine)
//                nextButton!!.visibility = View.GONE
//                hintImageview!!.setImageDrawable(
//                    resources.getDrawable(
//                        R.drawable.hint_nine, null
//                    )
//                )
//            }
//        }
//    }
}