package com.example.schoolguide.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mineView.BaseDialog
import com.example.schoolguide.model.AddUserEvent
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.model.User
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_user_info.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        private const val TYPE_ADD = 2004
        private const val TYPE_UPDATE = 2005
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        viewUser = viewModelProvider()
        EventBus.getDefault().register(this)
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
                        viewUser.getDorList(1)
                    }
                    R.id.dialogUserAssign -> {
                        addUser(dialog, TYPE_ADD)
                    }
                }
            }
                .show()
        }
    }

    private fun addUser(dialog: BaseDialog, type: Int) {
        val nick = dialog.getView<EditText>(R.id.dialogUserNick)
        val phone = dialog.getView<EditText>(R.id.dialogUserPhone)
        val password = dialog.getView<EditText>(R.id.dialogUserPassword)
        val school = dialog.getView<TextView>(R.id.dialogUserSchool)
        val college = dialog.getView<TextView>(R.id.dialogUserCollege)
        val name = dialog.getView<EditText>(R.id.dialogUserName)
        val idCard = dialog.getView<EditText>(R.id.dialogUserIdCard)
        val dor = dialog.getView<TextView>(R.id.dialogUserDormitory)

        if (!phone.text.isNotNullAndEmpty() or !password.text.isNotNullAndEmpty()) {
            context?.toast(getString(R.string.phone_and_password_null_error))
            return
        }

        val user = User(
            name = nick.text.toString(),
            phone_number = phone.text.toString().toLong(),
            password = password.text.toString(),
            user_school = school.text.toString(),
            user_college = college.text.toString(),
            user_name = name.text.toString(),
            user_id_card = idCard.text.toString(),
            user_dormitory = dor.text.toString()
        )

        if (type == TYPE_ADD) {
            viewUser.addUser(user)
        } else if (type == TYPE_UPDATE) {
            viewUser.updataUser(user)
        }
    }

    private fun pickDialog(type: Int, optionList: List<String>) {
        val optionArray = optionList.copyToArray()
        val schoolView = mDialog.getView<TextView>(R.id.dialogUserSchool)
        val collegeView = mDialog.getView<TextView>(R.id.dialogUserCollege)
        val dormitoryView = mDialog.getView<TextView>(R.id.dialogUserDormitory)
        val title = when (type) {
            TYPE_SCHOOL -> {
                getString(R.string.school_select)
            }
            TYPE_COLLEGE -> {
                getString(R.string.college_select)
            }
            else -> {
                getString(R.string.dormitory_select)
            }
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
                        TYPE_DORMITORY -> {
                            dormitoryView.text = optionArray[index]
                        }
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

        observerAction(viewUser.dormitoryListLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    pickDialog(TYPE_DORMITORY, response.respond.copyToString { it.dormitory_name })
                }
            }
        }

        observerAction(viewUser.addUserLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.add_success))
                    EventBus.getDefault().post(AddUserEvent())
                    mDialog.dismiss()
                }
            }
        }

        observerAction(viewUser.updataLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    context?.toast(getString(R.string.update_success))
                    EventBus.getDefault().post(AddUserEvent())
                    mDialog.dismiss()
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
                        showChangeDialog(mAdapter.data[position])
                        pop?.hide()
                    }
                    1 -> {
                        viewModel.deleteUser(mAdapter.data[position].phone_number)
                        pop?.hide()
                    }
                }
            }
        }
    }

    private fun showChangeDialog(user: User) {
        mDialog = BaseDialog(context!!, R.layout.dialog_user_table)
        mDialog.init {
            it.getView<EditText>(R.id.dialogUserNick).setText(user.name)
            it.getView<EditText>(R.id.dialogUserPhone).apply {
                setText(user.phone_number.toString())
                isEnabled = false
            }
            it.getView<EditText>(R.id.dialogUserPassword).setText(user.password)
            it.getView<TextView>(R.id.dialogUserSchool).text = user.user_school
            it.getView<TextView>(R.id.dialogUserCollege).text = user.user_college
            it.getView<EditText>(R.id.dialogUserName).setText(user.user_name)
            it.getView<EditText>(R.id.dialogUserIdCard).setText(user.user_id_card)
            it.getView<TextView>(R.id.dialogUserDormitory).text = user.user_dormitory

        }.addItemClickList(
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
                    viewUser.getDorList(1)
                }
                R.id.dialogUserAssign -> {
                    addUser(dialog, TYPE_UPDATE)
                }
            }
        }
            .show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: AddUserEvent) {
        viewModel.getUserTable()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
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

