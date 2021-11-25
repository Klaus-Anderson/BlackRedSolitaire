package gms.angus.angussoli.view

import android.app.Fragment

/**
 * Created by Harry Cliff on 11/29/17.
 */
class ProScoreFragment : Fragment() {
//    @JvmField
//    @BindView(R.id.proRecyclerView)
//    var recyclerView: RecyclerView? = null
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_pro_score, container, false)
//        ButterKnife.bind(this, view)
//        recyclerView!!.layoutManager = LinearLayoutManager(activity)
//        recyclerView!!.adapter = ProScoreAdapter()
//        // Inflate the layout for this fragment
//        return view
//    }
//
//    private val rankedList: List<RankedPlayer>
//        private get() = (activity as GameActivity).rankingLeaderboardScoreList
//
//    internal inner class ProScoreAdapter : RecyclerView.Adapter<ProScoreAdapter.ViewHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(
//                R.layout.pro_score_view_holder, parent, false
//            )
//            return ViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.playerName!!.text = rankedList[position].userName
//            holder.playerRank!!.text = (position + 1).toString() + ""
//            val numberFormat = DecimalFormat("#.###")
//            holder.playerAverage!!.text = numberFormat.format(
//                rankedList[position].avgGame
//            ) + ""
////            if (rankedList[position].userName == userDisplayName) {
////                holder.wrapper!!.setBackgroundResource(R.drawable.current_level_shape)
////            }
//        }
//
//        override fun getItemCount(): Int {
//            return rankedList.size
//        }
//
//        internal inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
//            @JvmField
//            @BindView(R.id.wrapper)
//            var wrapper: RelativeLayout? = null
//
//            @JvmField
//            @BindView(R.id.playerName)
//            var playerName: TextView? = null
//
//            @JvmField
//            @BindView(R.id.playerRank)
//            var playerRank: TextView? = null
//
//            @JvmField
//            @BindView(R.id.playerAverage)
//            var playerAverage: TextView? = null
//
//            init {
//                ButterKnife.bind(this, view!!)
//            }
//        }
//    }
}