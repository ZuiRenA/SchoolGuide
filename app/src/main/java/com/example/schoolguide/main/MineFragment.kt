package com.example.schoolguide.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.schoolguide.R
import com.example.schoolguide.admin.AdminActivity
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mine.PersonDataActivity
import com.example.schoolguide.mine.SettingActivity
import com.example.schoolguide.model.Mine
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.view.BaseFragment
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine.view.*

class MineFragment : BaseFragment() {

    private lateinit var item: MutableList<Mine>
    private lateinit var mAdapter: MineAdapter
    private lateinit var mineRecycler: RecyclerView

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()

        private const val PersonData = 0
        private const val Setting =1
        private const val Admin = 2
    }

    override fun onStart() {
        super.onStart()
        item = mutableListOf(Mine(R.drawable.mine_person_data, resources.getString(R.string.person_data)),
            Mine(R.drawable.mine_setting, resources.getString(R.string.setting)))

        refreshMine.setOnRefreshListener { refresh ->
            view?.let { init(it) }
            refresh.finishRefresh()
        }

        LoginUtil.user?.user_permission?.let {
            if (it.toBoolean()) {
                item.add(Mine(name = getString(R.string.admin_mode), icon = R.drawable.admin))
            }
        }

        initAdapter()
    }

    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mineRecycler.layoutManager = mLayoutManager

        mAdapter = MineAdapter(R.layout.item_mine, item)
        mineRecycler.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            when(position) {
                PersonData -> {
                    context?.intent(PersonDataActivity::class.java)
                }

                Setting -> {
                    context?.startActivity<SettingActivity> {   }
                }

                Admin -> {
                    context?.startActivity<AdminActivity> {  }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        mineRecycler = view.mineRecycler
        init(view)
        view.refreshMine.setRefreshHeader(ClassicsHeader(context))
        return view
    }

    private fun init(view: View) {
        view.mine_avatar.show(uri =  LoginUtil.user?.user_avatar, placeholder = ColorDrawable(Color.YELLOW), error = R.drawable.test_avatar_icon) {
            RequestOptions().centerCrop().transform(CircleCrop())
        }

        view.mine_avatar_bg.show(uri =  LoginUtil.user?.user_avatar, placeholder = ColorDrawable(Color.YELLOW), error = R.drawable.test_avatar_icon) {
            RequestOptions.bitmapTransform(BlurTransformation(20, 3))
        }
    }
}

class MineAdapter(resId: Int, data: List<Mine>): BaseQuickAdapter<Mine, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: Mine?) {
        helper?.setText(R.id.mine_item_title, item?.name)
        Glide.with(mContext).load(item?.icon)
            .into(helper?.getView(R.id.mine_item_icon) as ImageView)
    }
}

