package com.example.schoolguide.main

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.view.BaseFragment

import com.example.schoolguide.R
import com.example.schoolguide.databinding.FragmentGuideBinding
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.mine.PersonDataActivity
import com.example.schoolguide.model.GuideTime
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_guide.*
import kotlinx.android.synthetic.main.fragment_guide.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class GuideFragment : BaseFragment(), View.OnClickListener {

    private lateinit var viewModel: GuideViewModel
    private var timeDialog: TimePickerDialog? = null
    private lateinit var sf: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(GuideViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_guide, container, false)
        view.refreshGuide.setRefreshHeader(ClassicsHeader(context))
        view.guidePersonData.setOnClickListener(this)
        view.guideUpload.setOnClickListener(this)
        return view
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.guidePersonData -> {
                context?.intent(PersonDataActivity::class.java)
            }

            R.id.guideUpload -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        refreshGuide.setOnRefreshListener {
            it.finishRefresh(2000, false)
        }

        context?.let {
            Glide.with(it)
                .load(R.drawable.test_avatar_icon)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(guideAvatar)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideFragment()
    }
}
