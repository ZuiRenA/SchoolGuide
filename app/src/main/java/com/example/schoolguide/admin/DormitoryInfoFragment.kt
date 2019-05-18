package com.example.schoolguide.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mineView.BaseDialog
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.Dormitory
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_dormitory_info.*
import org.greenrobot.eventbus.EventBus


class DormitoryInfoFragment : Fragment() {

    private lateinit var viewModel: AdminViewModel
    private lateinit var viewDor: DormitoryViewModel
    private lateinit var mAdapter: DorTableAdapter
    private lateinit var item: MutableList<Dormitory>
    private lateinit var dialog: BaseDialog
    private var index = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        viewDor = viewModelProvider()
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
                val clickItemIdList = listOf(
                    R.id.dialogDorAddFinish, R.id.dialogDorManOne, R.id.dialogDorManTwo,
                    R.id.dialogDorManThree, R.id.dialogDorManFour, R.id.dialogDorAssign
                )
                dialog = BaseDialog(it, R.layout.dialog_dor_add)
                dialog.addItemClickList(clickItemIdList) { dialog, view ->
                    when (view?.id) {
                        R.id.dialogDorAddFinish -> dialog.dismiss()
                        R.id.dialogDorManOne -> viewDor.getUserTable(TYPE_MAN_ONE)
                        R.id.dialogDorManTwo -> viewDor.getUserTable(TYPE_MAN_TWO)
                        R.id.dialogDorManThree -> viewDor.getUserTable(TYPE_MAN_THREE)
                        R.id.dialogDorManFour -> viewDor.getUserTable(TYPE_MAN_FOUR)
                        R.id.dialogDorAssign -> dormitoryAction(dialog, TYPE_ADD, -1)

                    }
                }.show()
            }
        }

        initNetwork()
        initAdapter()
        viewModel.getDormitory(1)
    }

    private fun dormitoryAction(dialog: BaseDialog, type: Int, position: Int) {
        val dorName = dialog.getView<EditText>(R.id.dialogDorNumber)
        val dorManOne = dialog.getView<TextView>(R.id.dialogDorManOne)
        val dorManTwo = dialog.getView<TextView>(R.id.dialogDorManTwo)
        val dorManThree = dialog.getView<TextView>(R.id.dialogDorManThree)
        val dorManFour = dialog.getView<TextView>(R.id.dialogDorManFour)

        if (!dorName.text.toString().isNotNullAndEmpty()) {
            context?.toast(getString(R.string.dor_number_error))
            return
        }

        val temp = listOf(
            dorManOne.text, dorManTwo.text,
            dorManThree.text, dorManFour.text
        ).replaceNullToEmpty()


        if (type == TYPE_ADD) {
            val dor = Dormitory(
                school_id = 1, dormitory_id = -1, dormitory_name = dorName.text.toString(),
                dormitory_student_list = temp
            )
            viewDor.insertDor(dor)
        } else if (type == TYPE_UPDATE) {
            val id = if (position != -1) mAdapter.data[position].dormitory_id else -1
            val dor = Dormitory(
                school_id = 1, dormitory_id = id, dormitory_name = dorName.text.toString(),
                dormitory_student_list = temp
            )
            viewDor.updateDor(dor)
        }
    }

    private fun showPickDialog(type: Int, optionList: List<String>, dialog: BaseDialog) {
        val optionArray = optionList.copyToArray()
        val one = dialog.getView<TextView>(R.id.dialogDorManOne)
        val two = dialog.getView<TextView>(R.id.dialogDorManTwo)
        val three = dialog.getView<TextView>(R.id.dialogDorManThree)
        val four = dialog.getView<TextView>(R.id.dialogDorManFour)

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.select_man))
            .setSingleChoiceItems(optionArray, -1) { _: DialogInterface?, which: Int ->
                index = which
            }.setPositiveButton(R.string.assign) { dialogInner, _ ->
                if (index != -1) {
                    when (type) {
                        TYPE_MAN_ONE -> one.text = optionArray[index]
                        TYPE_MAN_TWO -> two.text = optionArray[index]
                        TYPE_MAN_THREE -> three.text = optionArray[index]
                        TYPE_MAN_FOUR -> four.text = optionArray[index]
                    }
                }
                index = -1
                dialogInner.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInner, _ ->
                index = -1
                dialogInner.dismiss()
            }
            .show()
    }


    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dormitoryRecycler.layoutManager = mLayoutManager

        item = mutableListOf()
        mAdapter = DorTableAdapter(R.layout.item_dormitory_table, item)
        dormitoryRecycler.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view?.id) {
                R.id.dorTableMore -> {
                    view.showMorePopWindowMenu(listOf("修改", "删除")) { _, _, positionInner, pop ->
                        when (positionInner) {
                            0 -> {
                                val clickItemIdList = listOf(
                                    R.id.dialogDorAddFinish, R.id.dialogDorManOne, R.id.dialogDorManTwo,
                                    R.id.dialogDorManThree, R.id.dialogDorManFour, R.id.dialogDorAssign
                                )
                                dialog = BaseDialog(context!!, R.layout.dialog_dor_add)
                                dialog
                                    .init {
                                        it.getView<EditText>(R.id.dialogDorNumber).setText(mAdapter.data[position].dormitory_name)
                                        it.getView<TextView>(R.id.dialogDorManOne).text = mAdapter.data[position].dormitory_student_list[0].emptyToHolo()
                                        it.getView<TextView>(R.id.dialogDorManTwo).text = mAdapter.data[position].dormitory_student_list[1].emptyToHolo()
                                        it.getView<TextView>(R.id.dialogDorManThree).text = mAdapter.data[position].dormitory_student_list[2].emptyToHolo()
                                        it.getView<TextView>(R.id.dialogDorManFour).text = mAdapter.data[position].dormitory_student_list[3].emptyToHolo()
                                    }
                                    .addItemClickList(clickItemIdList) { dialog, view ->
                                        when (view?.id) {
                                            R.id.dialogDorAddFinish ->  dialog.dismiss()
                                            R.id.dialogDorManOne -> viewDor.getUserTable(TYPE_MAN_ONE)
                                            R.id.dialogDorManTwo -> viewDor.getUserTable(TYPE_MAN_TWO)
                                            R.id.dialogDorManThree -> viewDor.getUserTable(TYPE_MAN_THREE)
                                            R.id.dialogDorManFour -> viewDor.getUserTable(TYPE_MAN_FOUR)
                                            R.id.dialogDorAssign -> dormitoryAction(dialog, TYPE_UPDATE, position)
                                        }
                                    }.show()
                                pop?.hide()
                            }
                            1 -> {
                                viewModel.deleteDormitory(id = mAdapter.data[position].dormitory_id)
                                pop?.hide()
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
                    mAdapter.removeDataToNew(item)
                }
            }
        }

        observerAction(viewModel.deleteDormitoryLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.delete_success))
                    viewModel.getDormitory(1)
                }
            }
        }

        observerAction(viewDor.userTableLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    response.errorReason?.toInt()?.let { type ->
                        showPickDialog(
                            type = type,
                            optionList = response.respond.copyToString { it.name },
                            dialog = dialog
                        )
                    }
                }
            }
        }

        observerAction(viewDor.insertDorLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.add_success))
                    dialog.isShowing.yes { dialog.dismiss() }
                    viewModel.getDormitory(1)
                }
            }
        }

        observerAction(viewDor.updateDorLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.update_success))
                    dialog.isShowing.yes { dialog.dismiss() }
                    viewModel.getDormitory(1)
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = DormitoryInfoFragment()

        private const val TYPE_MAN_ONE = 3001
        private const val TYPE_MAN_TWO = 3002
        private const val TYPE_MAN_THREE = 3003
        private const val TYPE_MAN_FOUR = 3004
        private const val TYPE_ADD = 3005
        private const val TYPE_UPDATE = 3006
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


