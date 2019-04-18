package com.example.schoolguide.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.view.BaseFragment

import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mine.PersonDataActivity
import com.example.schoolguide.model.User
import com.example.schoolguide.util.LoginUtil
import com.jzxiang.pickerview.TimePickerDialog
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_guide.*
import kotlinx.android.synthetic.main.fragment_guide.view.*
import java.text.SimpleDateFormat

class GuideFragment : BaseFragment(), View.OnClickListener {

    private lateinit var viewModel: GuideViewModel
    private var timeDialog: TimePickerDialog? = null
    private lateinit var sf: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(GuideViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_guide, container, false)
        view.refreshGuide.setRefreshHeader(ClassicsHeader(context))
        view.guidePersonData.setOnClickListener(this)
        view.guideUpload.setOnClickListener(this)
        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {
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

        init()
    }

    private fun init() {
        val userInfo = LoginUtil.user
        context?.let {
            if (userInfo?.user_avatar != null && userInfo.user_avatar != "") {
                Glide.with(it)
                    .load(userInfo.user_avatar)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(guideAvatar)
            } else {
                Glide.with(it)
                    .load(R.drawable.test_avatar_icon)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(guideAvatar)
            }
        }

        (userInfo != null).yes {
            guideName.text = userInfo?.name
            guideId.text = (userInfo?.user_id_card !=null && userInfo.user_id_card != "").yes { userInfo?.user_id_card }.otherwise { getString(R.string.id_not_input) }
            isComplete(userInfo)
        }.otherwise {
            context?.toast(getString(R.string.init_error_one))
        }
    }

    private fun isComplete(userInfo: User?) {
        (userInfo?.user_school == null || userInfo.user_college == null || userInfo.user_id_card == null || userInfo.user_name == null
                || userInfo.user_school == "" || userInfo.user_college == "" || userInfo.user_id_card == "" || userInfo.user_name == "").no {
            guidePersonStatus.text = getString(R.string.yes_finish_info)
            guidePersonStatus.setTextColor(resources.getColor(R.color.default_text_view_color))
            guideStatusText.text = getString(R.string.finish_guide_info)
            guideYes.text = getString(R.string.guide_yes)
            guideIconYes.setImageResource(R.drawable.success)
        }.otherwise {
            guidePersonStatus.text = getString(R.string.no_finish_info)
            guidePersonStatus.setTextColor(resources.getColor(R.color.light_blue))
            guideStatusText.text = getString(R.string.quick_guide_info)
            guideYes.text = getString(R.string.guide_no)
            guideIconYes.setImageResource(R.drawable.fail)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideFragment()
    }
}
