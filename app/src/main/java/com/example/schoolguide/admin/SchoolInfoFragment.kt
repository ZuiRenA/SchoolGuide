package com.example.schoolguide.admin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.example.schoolguide.R
import com.example.schoolguide.extUtil.onClick
import com.example.schoolguide.extUtil.viewModelProvider
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.SchoolInfo
import kotlinx.android.synthetic.main.base_toolbar.*
import org.greenrobot.eventbus.EventBus

class SchoolInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_school_info, container, false)
    }

    override fun onStart() {
        super.onStart()

        baseToolbar.setNavigationOnClickListener { EventBus.getDefault().post(AdminFinishEvent()) }
        baseToolbarTitle.text = getString(R.string.school_table)
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTV.text = getString(R.string.change_table)
        baseToolbarTV.onClick {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SchoolInfoFragment()
    }
}


class SchoolTableAdapter(resId: Int, data: List<SchoolInfo>): BaseQuickAdapter<SchoolInfo, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: SchoolInfo?) {

    }
}
