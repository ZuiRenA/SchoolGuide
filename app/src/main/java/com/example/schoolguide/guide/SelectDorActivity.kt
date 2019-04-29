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
import com.example.schoolguide.main.MineAdapter
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.view.BaseActivity
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory
import kotlinx.android.synthetic.main.activity_select_dor.*
import kotlinx.android.synthetic.main.base_toolbar.*
import java.util.LinkedHashSet

class SelectDorActivity : BaseActivity() {
    private lateinit var mAdapter: DorAdapter
    private var data: MutableList<Dormitory> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_dor)

        baseToolbarTitle.text = getString(R.string.select_dor)
        baseToolbar.setNavigationOnClickListener { finish() }
        initAdapter()
    }

    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerSelectDor.layoutManager = mLayoutManager

        for (i in 0..10) {
            data.add(Dormitory(1, 1, "aas", listOf()))
        }

        mAdapter = DorAdapter(R.layout.item_select_dor, data)
        recyclerSelectDor.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, view, position ->
            view.setBackgroundResource(R.drawable.change_password_btn)
            mAdapter.helperList[position].allChildViewFilterIdAndAction<TextView>(view?.id) {
                it?.setBackgroundResource(R.drawable.item_select_bg)
            }
        }
    }
}

class DorAdapter(resId: Int, data: List<Dormitory>): BaseQuickAdapter<Dormitory, BaseViewHolder>(resId, data) {
    var helperList: MutableList<BaseViewHolder?> = mutableListOf()

    override fun convert(helper: BaseViewHolder?, item: Dormitory?) {
        helper?.addOnClickListener(R.id.textViewDorOne)
            ?.addOnClickListener(R.id.textViewDorTwo)
            ?.addOnClickListener(R.id.textViewDorThree)
            ?.addOnClickListener(R.id.textViewDorFour)
        helperList.add(helper)
    }
}

fun <T: View> BaseViewHolder?.allChildViewFilterIdAndAction(id: Int?, block:(T?) -> Unit) {
    val childId = this?.childClickViewIds
    childId?.forEach {
        if (id != it) {
            val view = this?.getView<T>(it)
            block(view)
        }
    }
}


