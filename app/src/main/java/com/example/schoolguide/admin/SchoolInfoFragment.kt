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
import com.example.schoolguide.extUtil.viewModelProvider
import com.example.schoolguide.model.SchoolInfo

class SchoolInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_school_info, container, false)
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
