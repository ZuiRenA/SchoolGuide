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
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.model.SchoolInfo


class DormitoryInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_dormitory_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        initRecycler()
    }

    private fun initRecycler() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = DormitoryInfoFragment()
    }
}

class DorTableAdapter(resId: Int, data: List<Dormitory>): BaseQuickAdapter<Dormitory, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: Dormitory?) {

    }
}


