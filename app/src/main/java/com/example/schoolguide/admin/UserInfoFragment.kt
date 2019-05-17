package com.example.schoolguide.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mineView.BaseDialog
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.User
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_user_info.*
import org.greenrobot.eventbus.EventBus

class UserInfoFragment : Fragment() {

    private lateinit var mAdapter: UserTableAdapter
    private lateinit var item: MutableList<User>
    private lateinit var viewModel: AdminViewModel
    private lateinit var viewUser: UserInfoViewModel
    private lateinit var mDialog: BaseDialog
    private var index = -1

    companion object {
        @JvmStatic
        fun newInstance() = UserInfoFragment()

        private const val TYPE_SCHOOL = 2001
        private const val TYPE_COLLEGE = 2002
        private const val TYPE_DORMITORY = 2003
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        viewUser = viewModelProvider()
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        initNetwork()
        initRecycler()

        viewModel.getUserTable()

        baseToolbar.setNavigationOnClickListener { EventBus.getDefault().post(AdminFinishEvent()) }
        baseToolbarTitle.text = getString(R.string.user_table)
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTV.text = getString(R.string.add_table)
        baseToolbarTV.onClick { dialogShow() }
    }


    private fun dialogShow() {
        context?.let {
            mDialog = BaseDialog(it, R.layout.dialog_user_table)
            mDialog.addItemClickList(
                    listOf(
                        R.id.dialogUserFinish,
                        R.id.dialogUserSchool, R.id.dialogUserCollege, R.id.dialogUserAssign,
                        R.id.dialogUserDormitory
                    )
                ) { dialog, view ->
                    when (view?.id) {
                        R.id.dialogUserFinish -> {
                            dialog.dismiss()
                        }
                        R.id.dialogUserSchool -> {
                            viewUser.getSchoolList()
                        }
                        R.id.dialogUserCollege -> {
                            viewUser.getCollegeList(1)
                        }
                        R.id.dialogUserDormitory -> {

                        }
                        R.id.dialogUserAssign -> {
                            Log.d("dialog: ", "listener: Assign")
                        }
                    }
                }
                .show()
        }
    }

    private fun pickDialog(type: Int, optionList: List<String>) {
        val optionArray = optionList.copyToArray()
        val schoolView = mDialog.getView<TextView>(R.id.dialogUserSchool)
        val collegeView = mDialog.getView<TextView>(R.id.dialogUserCollege)
        val dormitoryView = mDialog.getView<TextView>(R.id.dialogUserDormitory)
        val title = when(type) {
            TYPE_SCHOOL -> { getString(R.string.school_select) }
            TYPE_COLLEGE -> { getString(R.string.college_select)}
            else -> { getString(R.string.dormitory_select) }
        }

        AlertDialog.Builder(context)
            .setTitle(title)
            .setSingleChoiceItems(optionArray, -1) { _: DialogInterface?, which: Int ->
                index = which
            }
            .setPositiveButton(R.string.assign) { dialog, _ ->
                if (index != -1) {
                    when (type) {
                        TYPE_SCHOOL -> schoolView.text = optionArray[index]
                        TYPE_COLLEGE -> collegeView.text = optionArray[index]
                        TYPE_DORMITORY -> { dormitoryView.text = optionArray[index] }
                    }
                }
                index = -1
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                index = -1
                dialog.dismiss()
            }
            .show()
    }

    private fun initNetwork() {
        observerAction(viewModel.userTableLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    item = it.respond as MutableList<User>
                    mAdapter.removeDataToNew(item)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

        observerAction(viewModel.deleteUserLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.delete_success))
                    viewModel.getUserTable()
                }
            }
        }

        observerAction(viewUser.schoolListLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    pickDialog(TYPE_SCHOOL, response.respond.copyToString { it.school_name })
                }
            }
        }

        observerAction(viewUser.collegeListLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    pickDialog(TYPE_COLLEGE, response.respond)
                }
            }
        }
    }

    private fun initRecycler() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        userRecycler.apply {
            layoutManager = mLayoutManager
            itemAnimator?.changeDuration = 300
            itemAnimator?.moveDuration = 300
        }

        item = mutableListOf()
        mAdapter = UserTableAdapter(R.layout.item_user_table, item)
        userRecycler.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, view, position ->
            view.showMorePopWindowMenu(dataList = listOf("修改", "删除")) { _, _, positionInner, pop ->
                when (positionInner) {
                    0 -> {
                        TODO("增加user的修改")
                    }
                    1 -> {
                        viewModel.deleteUser(mAdapter.data[position].phone_number)
                        pop?.hide()
                    }
                }
            }
        }
    }
}

class UserTableAdapter(resId: Int, data: List<User>) : BaseQuickAdapter<User, BaseViewHolder>(resId, data) {

    /**
     * 标记展开的item
     */
    private var opened = -1

    override fun convert(helper: BaseViewHolder?, item: User?) {
        helper?.apply {
            setText(R.id.userTableName, item?.user_name)
            setText(R.id.userTablePhoneNumber, "${item?.phone_number}")
            setText(R.id.userSchool, "学校：" + item?.user_school)
            setText(R.id.userCollege, "学院：" + item?.user_college)
            setText(R.id.userIdCard, "身份证：" + item?.user_id_card)
            setText(R.id.userDor, "宿舍：" + item?.user_dormitory)
            addOnClickListener(R.id.userTableMore)

            action<ImageView>(R.id.userAvatar) {
                it.show(
                    uri = item?.user_avatar,
                    placeholder = ColorDrawable(Color.YELLOW),
                    error = R.drawable.test_avatar_icon
                ) {
                    RequestOptions().centerCrop().transform(CircleCrop())
                }

                if (position == opened) {
                    action<View>(R.id.userTableContent) { view ->
                        view.visibility = View.VISIBLE
                    }
                } else {
                    action<View>(R.id.userTableContent) { view ->
                        view.visibility = View.GONE
                    }
                }

                setOnClickListener(R.id.userTableGroup) {
                    if (opened == adapterPosition) {
                        opened = -1
                        notifyItemChanged(adapterPosition)
                    } else {
                        val oldOpened = opened
                        opened = adapterPosition
                        notifyItemChanged(oldOpened)
                        notifyItemChanged(opened)
                    }
                }
            }
        }
    }
}

