package com.example.schoolguide.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mineView.BaseDialog
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.SchoolGuideTime
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_guide_info.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class GuideInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel
    private lateinit var item: MutableList<SchoolGuideTime>
    private lateinit var mAdapter: GuideTimeTableAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_guide_info, container, false)
    }

    override fun onStart() {
        super.onStart()

        initRecycler()

        baseToolbar.setNavigationOnClickListener { EventBus.getDefault().post(AdminFinishEvent()) }
        baseToolbarTitle.text = getString(R.string.guide_table)

        initNetwork()
        viewModel.getGuideTime(1)
    }

    private fun initRecycler() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        guideRecycler.layoutManager = mLayoutManager

        item = mutableListOf()
        mAdapter = GuideTimeTableAdapter(R.layout.item_guide_table, item)
        guideRecycler.adapter = mAdapter
    }


    private fun initNetwork() {
        observerAction(viewModel.guideTimeLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    item = response.respond as MutableList<SchoolGuideTime>
                    mAdapter.removeDataToNew(item)
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideInfoFragment()
    }
}

class GuideTimeTableAdapter(resId: Int, data: List<SchoolGuideTime>): BaseQuickAdapter<SchoolGuideTime, BaseViewHolder>(resId, data) {
    var helperList: MutableList<BaseViewHolder?> = mutableListOf()

    override fun convert(helper: BaseViewHolder?, item: SchoolGuideTime?) {
        helper?.apply {
            setText(R.id.guideCollegeName, item?.guide_college)
            setText(R.id.guideCollegeTime, "${item?.guide_time_one} - ${item?.guide_time_two}")
        }

        helperList.add(helper)
    }
}
