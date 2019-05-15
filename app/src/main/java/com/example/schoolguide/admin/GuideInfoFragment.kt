package com.example.schoolguide.admin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.example.schoolguide.R
import com.example.schoolguide.extUtil.viewModelProvider
import com.example.schoolguide.mineView.BaseDialog
import com.example.schoolguide.model.SchoolGuideTime

class GuideInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_guide_info, container, false)
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideInfoFragment()
    }
}

class GuideTimeTableAdapter(resId: Int, data: List<SchoolGuideTime>): BaseQuickAdapter<SchoolGuideTime, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: SchoolGuideTime?) {

    }
}
