package com.example.schoolguide.main

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.schoolguide.view.BaseFragment
import com.example.schoolguide.R
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.view.BigImageView
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var imageList: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        BigImageViewer.initialize(com.github.piasy.biv.loader.glide.GlideImageLoader.with(context))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        imageList = mutableListOf(R.drawable.blhx_01, R.drawable.blhx_02, R.drawable.blhx_03, R.drawable.blhx_04)
        //设置Home页面Banner的各项属性
        //各项属性详见：https://github.com/youth5201314/banner
        homeBanner.setImageLoader(GlideImageLoader())
        homeBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        homeBanner.setImages(imageList)
        homeBanner.setBannerAnimation(Transformer.Default)
        homeBanner.setDelayTime(3000)
        homeBanner.setIndicatorGravity(BannerConfig.CENTER)
        homeBanner.setOnBannerListener { position ->
            showDialog()
        }
        homeBanner.start()
    }

    private fun showDialog() {
        val dialog = Dialog(context!!, R.style.HomeBigImgDialogTheme)
        val view = View.inflate(context, R.layout.home_image_dialog, null)
        val image = view.findViewById<BigImageView>(R.id.homeBigImage)
        val finish = view.findViewById<ImageView>(R.id.homeBigImageFinish)
        dialog.setContentView(view)
        image.showImage(Uri.parse("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2871066900,1607816866&fm=175&app=25&f=JPEG?w=640&h=903&s=7F48A357899947DC4631F9EE0300E031"))

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
        fun newInstance() = HomeFragment()
    }
}

class GlideImageLoader() : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        if (context != null && imageView != null) {
            Glide.with(context).load(path)
                .into(imageView)
        }
    }
}
