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
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.SchoolInfo
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_school_info.*
import kotlinx.android.synthetic.main.fragment_school_info.schoolTableName
import kotlinx.android.synthetic.main.fragment_school_info.view.*
import org.greenrobot.eventbus.EventBus

class SchoolInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel
    private lateinit var viewSchool: SchoolInfoViewModel
    private var canEnable = false
    private var schoolInfo: SchoolInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        viewSchool = viewModelProvider()
        return inflater.inflate(R.layout.fragment_school_info, container, false)
    }

    override fun onStart() {
        super.onStart()

        baseToolbar.setNavigationOnClickListener { EventBus.getDefault().post(AdminFinishEvent()) }
        baseToolbarTitle.text = getString(R.string.school_table)
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTV.text = getString(R.string.change_table)
        baseToolbarTV.setOnClickListener {
            if (canEnable) {
                enabledSet(false)
                baseToolbarTV.text = getString(R.string.change_table)
            } else {
                enabledSet(true)
                baseToolbarTV.text = getString(R.string.finish_change)
            }
        }

        initNetwork()
        viewModel.getSchoolInfo(1)

        schoolTableAssign.setOnClickListener {
            schoolInfo?.let { school ->
                val temp = SchoolInfo(
                    school_id = school.school_id,
                    school_name = schoolTableName.text.toString(),
                    school_address = schoolTableAddress.text.toString(),
                    school_introduce = schoolTableIntroduce.text.toString(),
                    image_show_list = school.image_show_list
                )

                viewSchool.updateSchool(temp)
            }
        }
    }

    private fun enabledSet(b: Boolean) {
        schoolTableName.isEnabled = b
        schoolTableAddress.isEnabled = b
        schoolTableIntroduce.isEnabled = b
        canEnable = b
    }

    private fun initNetwork() {
        observerAction(viewModel.schoolTableInfoLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    schoolInfo = it.respond
                    schoolTableName.setText(schoolInfo?.school_name)
                    schoolTableAddress.setText(schoolInfo?.school_address)
                    schoolTableIntroduce.setText(schoolInfo?.school_introduce)
                }
            }
        }

        observerAction(viewSchool.updateSchoolLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.update_success))

                    schoolInfo = it.respond
                    schoolTableName.setText(schoolInfo?.school_name)
                    schoolTableAddress.setText(schoolInfo?.school_address)
                    schoolTableIntroduce.setText(schoolInfo?.school_introduce)
                }
            }
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
