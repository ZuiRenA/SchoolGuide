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
class GuideFragment : BaseFragment(), View.OnClickListener {

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
        return view
    }




    override fun onClick(v: View?) {

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
