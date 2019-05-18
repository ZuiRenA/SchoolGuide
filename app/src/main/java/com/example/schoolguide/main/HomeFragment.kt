package com.example.schoolguide.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.model.GuideTime
import com.example.schoolguide.model.SchoolGuideTime
import com.example.schoolguide.model.SchoolInfo
import com.example.schoolguide.util.SearchTimeUtil
import com.example.schoolguide.view.BaseFragment
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator
import com.github.piasy.biv.view.BigImageView
import com.github.piasy.biv.view.GlideImageViewFactory
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : BaseFragment(), OnDateSetListener {

    private lateinit var imageList: List<String>
    private lateinit var viewModel: HomeViewModel
    private lateinit var sf: SimpleDateFormat
    private var timeDialog: TimePickerDialog? = null
    private var pop: PopupWindow? = null
    private lateinit var guideTimeList: MutableList<SchoolGuideTime>
    private lateinit var mAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        BigImageViewer.initialize(com.github.piasy.biv.loader.glide.GlideImageLoader.with(context))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        //设置Home页面Banner的各项属性
        //各项属性详见：https://github.com/youth5201314/banner
        homeBanner.setImageLoader(GlideImageLoader())
        homeBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        homeBanner.setBannerAnimation(Transformer.Default)
        homeBanner.setDelayTime(3000)
        homeBanner.setIndicatorGravity(BannerConfig.CENTER)

        initRecycler()
        initGuideTime()
        initNetWork()
        initClick()
        viewModel.schoolInfo(id = 1)
        viewModel.guideTime(id = 1)
    }

    private fun initClick() {
        showHomeMap.setOnClickListener {
            showPop()
        }

        showHomeTime.onClick {
            mAdapter.removeAllItem()
            mAdapter.addData(guideTimeList)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecycler() {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerHomeGuideTime.layoutManager = mLayoutManager
        guideTimeList = mutableListOf()
        mAdapter = HomeAdapter(R.layout.item_home_guide_time, guideTimeList)
        recyclerHomeGuideTime.adapter = mAdapter
    }

    private fun initNetWork() {
        observerAction(viewModel.schoolInfoLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    showSchoolInfo(it.respond)
                    imageList = it.respond.image_show_list
                    showBanner(imageList)
                }.otherwise {
                    context?.toast(it.errorReason ?: "")
                    imageList = listOf("", "", "", "")
                    showBanner(imageList)
                }
            }
        }

        observerAction(viewModel.guideTimeLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    guideTimeList = it.respond as MutableList<SchoolGuideTime>
                    mAdapter.removeDataToNew(guideTimeList)
                }.otherwise { context?.toast(response.errorReason ?: "") }
            }
        }
    }

    private fun showBanner(imageList: List<String>) {
        homeBanner.setImages(imageList)
        homeBanner.setOnBannerListener { position ->
            showDialog(imageList[position])
        }
        homeBanner.start()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initGuideTime() {
        sf = SimpleDateFormat("yyyy.MM.dd")
        val mCalendar = Calendar.getInstance()
        val year = mCalendar.get(Calendar.YEAR)
        val month = mCalendar.get(Calendar.MONTH) + 1
        val day = mCalendar.get(Calendar.DAY_OF_MONTH)
        val mFormat = DecimalFormat("00")
        val monthStr = mFormat.format(month)
        val dayStr = mFormat.format(day)
        yourGuideTime.text = "查询报到日期：$year.$monthStr.$dayStr"
        yourGuideTime.setOnClickListener {
            showTimeDialog()
        }
    }

    private fun showTimeDialog() {
        if (timeDialog == null) {
            val fourYears = 3L * 365 * 1000 * 60 * 60 * 24L
            timeDialog = TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(R.string.cancel))
                .setSureStringId(getString(R.string.assign))
                .setTitleStringId(getString(R.string.select_time))
                .setType(Type.YEAR_MONTH_DAY)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + fourYears)
                .setWheelItemTextSize(12)
                .setThemeColor(resources.getColor(R.color.loginMain))
                .build()
        }

        timeDialog?.show(fragmentManager, "year_month_day")
    }


    private fun showSchoolInfo(schoolInfo: SchoolInfo) {
        homeSchoolName.text = schoolInfo.school_name
        homeSchoolAddress.text = schoolInfo.school_address
        homeSchoolIntro.text = schoolInfo.school_introduce
    }

    private fun showPop() {
        val view = View.inflate(context, R.layout.pop_map, null)
        val bigImage = view.findViewById<BigImageView>(R.id.homeMap)
        val mapFinish = view.findViewById<ImageView>(R.id.mapPopFinish)
        if (pop != null && pop!!.isShowing) {
            pop!!.dismiss()
            pop = null
        } else {
            pop = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            pop!!.showAtLocation(showHomeMap, Gravity.BOTTOM, 0, 200)
            pop!!.isFocusable = true
            pop!!.isOutsideTouchable = true

            bigImage.showImage(Uri.parse("https://i.loli.net/2019/04/07/5ca9e0b8a2187.jpg"))
            bigImage.setImageViewFactory(GlideImageViewFactory())
            bigImage.setProgressIndicator(MyProgress(mapFinish))

            mapFinish.setOnClickListener {
                pop!!.dismiss()
                pop = null
            }
        }
    }

    private fun showDialog(uri: String) {
        val dialog = Dialog(context!!, R.style.HomeBigImgDialogTheme)
        val view = View.inflate(context, R.layout.home_image_dialog, null)
        val image = view.findViewById<BigImageView>(R.id.homeBigImage)
        val finish = view.findViewById<ImageView>(R.id.homeBigImageFinish)
        dialog.setContentView(view)
        Log.e("imagePath", uri)

        image.showImage(Uri.parse(uri))
        image.setImageViewFactory(GlideImageViewFactory())
        image.setProgressIndicator(ProgressPieIndicator())

        val window = dialog.window
        window?.let {
            window.setGravity(Gravity.CENTER)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        finish.setOnClickListener { dialog.dismiss() }
        image.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onStop() {
        super.onStop()
        homeBanner.stopAutoPlay()
        if (pop != null && pop!!.isShowing) {
            pop!!.dismiss()
            pop = null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        val time = sf.format(Date(millseconds))
        yourGuideTime?.text = "查询报到日期：$time"

        val rightIndex = SearchTimeUtil.searchTime(selectTime = time, searchTime = guideTimeList, firstTime = {
            mutableListOf<GuideTime>().copyAction(it) { result ->
                GuideTime(
                    result.guide_time_one.year,
                    result.guide_time_one.month,
                    result.guide_time_one.day
                )
            }
        }, secondTime = {
            mutableListOf<GuideTime>().copyAction(it) { result ->
                GuideTime(
                    result.guide_time_two.year,
                    result.guide_time_two.month,
                    result.guide_time_two.day
                )
            }
        })

        mAdapter.removeAllItem()
        SearchTimeUtil.rightIndexAction(rightIndex) {
            mAdapter.addData(guideTimeList[it])
        }
        mAdapter.notifyDataSetChanged()
    }
}

class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        if (context != null && imageView != null) {
            Glide.with(context).load(path)
                .placeholder(R.mipmap.timg2)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
        }
    }
}

class MyProgress(private var finishImg: ImageView) : ProgressPieIndicator() {
    override fun onFinish() {
        super.onFinish()
        finishImg.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        finishImg.visibility = View.INVISIBLE
    }
}

class HomeAdapter(resId: Int, data: List<SchoolGuideTime>) :
    BaseQuickAdapter<SchoolGuideTime, BaseViewHolder>(resId, data) {
    override fun convert(helper: BaseViewHolder?, item: SchoolGuideTime?) {
        helper?.setText(R.id.textViewItemCollege, item?.guide_college)
        helper?.setText(R.id.textViewItemTime, "${item?.guide_time_one} - ${item?.guide_time_two}")
    }
}

fun <T, R : BaseViewHolder> BaseQuickAdapter<T, R>.removeAllItem() {
    Log.d("adapter_before", data.size.toString())
    this.data.clear()
    this.notifyDataSetChanged()
    Log.d("adapter_after", data.size.toString())
}