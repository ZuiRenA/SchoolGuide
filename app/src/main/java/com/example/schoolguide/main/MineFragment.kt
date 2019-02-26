package com.example.schoolguide.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.schoolguide.App
import com.example.schoolguide.view.BaseFragment
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.mine.PersonDataActivity
import com.example.schoolguide.mine.SettingActivity
import com.example.schoolguide.model.Mine
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_mine.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MineFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MineFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MineFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var item: List<Mine>
    private lateinit var mAdapter: MineAdapter
    private lateinit var mineRecycler: RecyclerView

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()

        private const val PersonData = 0
        private const val Setting =1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        item = listOf(Mine(R.drawable.mine_person_data, resources.getString(R.string.person_data)),
            Mine(R.drawable.mine_setting, resources.getString(R.string.setting)))

        initAdapter()
    }

    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mineRecycler.layoutManager = mLayoutManager

        mAdapter = MineAdapter(R.layout.item_mine, item)
        mineRecycler.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            when(position) {
                PersonData -> {
                    context?.intent(PersonDataActivity::class.java)
                }

                Setting -> {
                    context?.intent(SettingActivity::class.java)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        mineRecycler = view.mineRecycler
        Glide.with(this).load(R.drawable.test_avatar_icon)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(view.mine_avatar)

        Glide.with(this).load(R.drawable.test_avatar_icon)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
            .into(view.mine_avatar_bg)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}

class MineAdapter(resId: Int, data: List<Mine>): BaseQuickAdapter<Mine, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: Mine?) {
        helper?.setText(R.id.mine_item_title, item?.name)
        Glide.with(mContext).load(item?.icon)
            .into(helper?.getView(R.id.mine_item_icon) as ImageView)
    }
}

