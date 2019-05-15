package com.example.schoolguide.admin

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
import com.example.schoolguide.model.User
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_user_info.*

class UserInfoFragment : Fragment() {

    private lateinit var mAdapter: UserTableAdapter
    private lateinit var item: MutableList<User>
    private lateinit var viewModel: AdminViewModel
    private var userNick: String? = null

    companion object {
        @JvmStatic
        fun newInstance() = UserInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider()
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        initNetwork()
        initRecycler()

        viewModel.getUserTable()
        baseToolbarTitle.text = getString(R.string.user_table)
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTV.text = getString(R.string.add_table)
        baseToolbarTV.onClick {
            dialogShow()
        }
    }


    private fun dialogShow() {
        context?.let {
            BaseDialog(it, R.layout.dialog_user_table)
                .init { dialog ->
                    dialog.getView<TextView>(R.id.bigDick).text = "aaaa"
                }
                .addItemClick(R.id.testBtn) { dialog, _ ->
                    userNick = dialog.getView<EditText>(R.id.dialogUserNick).text.toString()
                    context?.toast(userNick ?: "无")
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun initNetwork() {
        observerAction(viewModel.userTableLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    item = it.respond as MutableList<User>
                    mAdapter.addData(item)
                    mAdapter.notifyDataSetChanged()
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
