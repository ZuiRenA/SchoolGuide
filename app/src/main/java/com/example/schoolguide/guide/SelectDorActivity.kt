package com.example.schoolguide.guide

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.schoolguide.AppContext
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.main.MineAdapter
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.view.BaseActivity
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory
import kotlinx.android.synthetic.main.activity_select_dor.*
import kotlinx.android.synthetic.main.base_toolbar.*
import java.util.LinkedHashSet

class SelectDorActivity : BaseActivity() {
    private lateinit var mAdapter: DorAdapter
    private var data: MutableList<Dormitory> = mutableListOf()
    private lateinit var viewModel: SelectDorViewModel
    private var id = -1
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_dor)
        viewModel = viewModelProvider()

        baseToolbarTitle.text = getString(R.string.select_dor)
        baseToolbar.setNavigationOnClickListener { finish() }
        initAdapter()
        initNetwork()
        initClick()
        viewModel.dormitory(id = 1)
    }

    private fun initClick() {
        textViewSelectDor.onClick {
            if (id or index == -1) {
                toast(getString(R.string.please_select_dor))
            } else {
                viewModel.select(LoginUtil.user?.phone_number ?: -1, id, index)
            }
        }
    }

    private fun initNetwork() {
        observerAction(viewModel.dormitoryLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    data = response.respond as MutableList<Dormitory>
                    mAdapter.addData(data)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

        observerAction(viewModel.selectDorLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    toast(response.respond)
                }.otherwise {
                    response.errorReason?.let { error ->
                        toast(error)
                    } }
            }
        }
    }

    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerSelectDor.layoutManager = mLayoutManager

        mAdapter = DorAdapter(R.layout.item_select_dor, data)
        recyclerSelectDor.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, view, position ->
            mAdapter.helperList.forEach {
                it.allChildView<TextView> { childView ->
                    childView?.setBackgroundResource(R.drawable.item_select_bg)
                }
            }

            view.setBackgroundResource(R.drawable.change_password_btn)
            val indexBtn = switch(view.id)
            id = data[position].dormitory_id
            index = indexBtn
        }
    }

    private fun switch(id: Int): Int =
        when(id) {
            R.id.textViewDorOne -> { 0 }
            R.id.textViewDorTwo -> { 1 }
            R.id.textViewDorThree -> { 2 }
            R.id.textViewDorFour -> { 3 }
            else -> { -1 }
        }

}

class DorAdapter(resId: Int, data: List<Dormitory>): BaseQuickAdapter<Dormitory, BaseViewHolder>(resId, data) {
    var helperList: MutableList<BaseViewHolder?> = mutableListOf()

    override fun convert(helper: BaseViewHolder?, item: Dormitory?) {
        val temp = textSwitch(item?.dormitory_student_list)
        val id = listOf(R.id.textViewDorOne, R.id.textViewDorTwo, R.id.textViewDorThree, R.id.textViewDorFour)
        helper?.setText(R.id.textViewItemSelect, item?.dormitory_name)
            ?.setText(R.id.textViewDorOne, temp[0])
            ?.setText(R.id.textViewDorTwo, temp[1])
            ?.setText(R.id.textViewDorThree, temp[2])
            ?.setText(R.id.textViewDorFour, temp[3])
        temp.forEachIndexed { index, s ->
            if (s != AppContext.getString(R.string.yes_people))
                helper?.addOnClickListener(id[index])
        }

        helperList.add(helper)
    }

    private fun textSwitch(list: List<String>?): List<String> {
        val temp = mutableListOf<String>()
        list?.forEach {
            it.isNotNullAndEmpty().yes {
                temp.add(AppContext.getString(R.string.yes_people))
            }.otherwise {
                temp.add(AppContext.getString(R.string.no_people))
            }
        }
        return temp
    }
}

fun <T: View> BaseViewHolder?.allChildView(block:(T?) -> Unit) {
    val childId = this?.childClickViewIds
    childId?.forEach {
        val view = this?.getView<T>(it)
        block(view)
    }
}


