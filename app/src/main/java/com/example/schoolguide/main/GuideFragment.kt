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
import com.example.schoolguide.view.BaseFragment

import com.example.schoolguide.R
import com.example.schoolguide.databinding.FragmentGuideBinding
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

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GuideFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GuideFragment : BaseFragment(), View.OnClickListener, OnDateSetListener {

    private var listener: OnFragmentInteractionListener? = null
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
        init(view)
        return view
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun init(view: View) {
        //TODO 初始化 各种东西
        sf = SimpleDateFormat("yyyy.MM.dd")

        //TODO 后面网络请求加上服务器的报到，没有就用本地当前时间
        val mCalendar = Calendar.getInstance()
        val year = mCalendar.get(Calendar.YEAR)
        val month = mCalendar.get(Calendar.MONTH) + 1
        val day = mCalendar.get(Calendar.DAY_OF_MONTH)
        val mFormat = DecimalFormat("00")
        val monthStr = mFormat.format(month)
        val dayStr = mFormat.format(day)
        view.tvGuideTime.text = "$year.$monthStr.$dayStr"

        view.refreshGuide.setRefreshHeader(ClassicsHeader(context))
        setClick(view)
    }

    private fun setClick(view: View) {
        view.tvGuideTime.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tvGuideTime -> {
                showTimeDialog()
            }
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

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        val time = sf.format(Date(millseconds))
        view?.tvGuideTime?.text = time
        //TODO 去获取rvCanGuide 的数据把他刷新出来
    }

    override fun onStart() {
        super.onStart()


        refreshGuide.setOnRefreshListener {
            it.finishRefresh(2000, false)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideFragment()
    }
}
