package com.example.schoolguide.main

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.guide.LetterUserActivity
import com.example.schoolguide.guide.SelectDorActivity
import com.example.schoolguide.mine.PersonDataActivity
import com.example.schoolguide.model.User
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.view.BaseFragment
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_guide.*
import kotlinx.android.synthetic.main.fragment_guide.view.*

class GuideFragment : BaseFragment(), View.OnClickListener {

    private lateinit var viewModel: GuideViewModel

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
        view.fabGuide.setOnClickListener(this)
        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.guidePersonData -> {
                context?.intent(PersonDataActivity::class.java)
            }

            R.id.guideUpload -> {
                (guideIdStatus.text == getString(R.string.yes_finish_info)).no {
                    context?.startActivity<LetterUserActivity> {  }
                }.otherwise { context?.toast(getString(R.string.finish_guide_info)) }
            }

            R.id.fabGuide -> {
                context?.startActivity<SelectDorActivity> {  }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        refreshGuide.setOnRefreshListener {
            init()
            it.finishRefresh(true)
        }

        init()
    }

    private fun init() {
        val userInfo = LoginUtil.user

        guideAvatar.show(uri = userInfo?.user_avatar, placeholder = ColorDrawable(Color.YELLOW), error = R.drawable.test_avatar_icon) {
            RequestOptions().centerCrop().transform(CircleCrop())
        }

        (userInfo != null).yes {
            guideName.text = userInfo?.name
            guideId.text = (userInfo?.user_id_card !=null && userInfo.user_id_card != "").yes { userInfo?.user_id_card }.otherwise { getString(R.string.id_not_input) }
            isComplete(userInfo)
        }.otherwise {
            context?.toast(getString(R.string.init_error_one))
        }

    }

    @SuppressLint("RestrictedApi")
    private fun isComplete(userInfo: User?) {
        val requirementOne = userInfo?.user_letter.isNotNullAndEmpty()
        val requirementTwo = userInfo?.user_school.isNotNullAndEmpty()
                && userInfo?.user_college.isNotNullAndEmpty()
                && userInfo?.user_id_card.isNotNullAndEmpty()
                && userInfo?.user_name.isNotNullAndEmpty()

        (requirementOne).yes {
            guideIdStatus.text = getString(R.string.yes_finish_info)
            guideIdStatus.setTextColor(resources.getColor(R.color.default_text_view_color))
        }.otherwise {
            guideIdStatus.text = getString(R.string.no_finish_info)
            guideIdStatus.setTextColor(resources.getColor(R.color.light_blue))
        }

        (requirementTwo).yes {
            guidePersonStatus.text = getString(R.string.yes_finish_info)
            guidePersonStatus.setTextColor(resources.getColor(R.color.default_text_view_color))
        }.otherwise {
            guidePersonStatus.text = getString(R.string.no_finish_info)
            guidePersonStatus.setTextColor(resources.getColor(R.color.light_blue))
        }

        (requirementOne && requirementTwo).yes {
            guideStatusText.text = getString(R.string.finish_guide_info)
            guideYes.text = getString(R.string.guide_yes)
            guideIconYes.setImageResource(R.drawable.success)
            fabGuide.show()
        }.otherwise {
            guideStatusText.text = getString(R.string.quick_guide_info)
            guideYes.text = getString(R.string.guide_no)
            guideIconYes.setImageResource(R.drawable.fail)
            fabGuide.hide()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideFragment()
    }
}
