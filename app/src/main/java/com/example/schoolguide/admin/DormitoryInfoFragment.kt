package com.example.schoolguide.admin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mineView.BaseDialog
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.model.SchoolInfo
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_dormitory_info.*
import org.greenrobot.eventbus.EventBus


class DormitoryInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel
    private lateinit var mAdapter: DorTableAdapter
    private lateinit var item: MutableList<Dormitory>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_dormitory_info, container, false)
    }

    override fun onStart() {
        super.onStart()

        baseToolbar.setNavigationOnClickListener { EventBus.getDefault().post(AdminFinishEvent()) }
        baseToolbarTitle.text = getString(R.string.dormitory_table)
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTV.text = getString(R.string.add_table)
        baseToolbarTV.setOnClickListener {
            context?.let {
                val clickItemIdList = listOf(R.id.dialogDorAddFinish)
                BaseDialog(it, R.layout.dialog_dor_add)
                    .addItemClickList(clickItemIdList) { dialog, view ->
                        when(view?.id) {
                            R.id.dialogDorAddFinish -> { dialog.dismiss() }
                        }
                    }
                    .show()
            }
        }

        initNetwork()
        initAdapter()
        viewModel.getDormitory(1)
    }

    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dormitoryRecycler.layoutManager = mLayoutManager

        item = mutableListOf()
        mAdapter = DorTableAdapter(R.layout.item_dormitory_table, item)
        dormitoryRecycler.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view?.id) {
                R.id.dorTableMore -> {
                    view.showMorePopWindowMenu(listOf("修改", "删除")) { _, _, positionInner, _ ->
                        when(positionInner) {
                            0 -> {
                                context?.toast("修改")
                            }
                            1 -> {
                                context?.toast("删除")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initNetwork() {
        observerAction(viewModel.dormitoryTableLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    item = response.respond as MutableList<Dormitory>
                    mAdapter.addData(item)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = DormitoryInfoFragment()
    }
}

class DorTableAdapter(resId: Int, data: List<Dormitory>) : BaseQuickAdapter<Dormitory, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: Dormitory?) {
        val id = listOf(R.id.dorTableManOne, R.id.dorTableManTwo, R.id.dorTableManThree, R.id.dorTableManFour)
        val textList = item?.dormitory_student_list?.textSwitch()
        helper?.apply {
            setText(R.id.dorTableName, item?.dormitory_name)
            id.forEachIndexed { index, i ->
                setText(i, textList?.get(index) ?: "空")
            }
            addOnClickListener(R.id.dorTableMore)
        }
    }
}

fun List<String>.textSwitch(): List<String> {
    val temp = mutableListOf<String>()
    this.forEach {
        if (it.isNotNullAndEmpty()) {
            temp.add(it)
        } else {
            temp.add("空")
        }
    }
    return temp
}


