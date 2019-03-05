package com.example.schoolguide.mine

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.R
import com.example.schoolguide.util.*
import com.example.schoolguide.view.BaseActivity
import kotlinx.android.synthetic.main.activity_person_data.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileNotFoundException

class PersonDataActivity : BaseActivity(), View.OnClickListener {

    private lateinit var popupWindow: PopupWindow
    private lateinit var imagePickUtil: ImagePickUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_data)
        imagePickUtil = ImagePickUtil(this)

        initView()
        initClick()
    }

    private fun initClick() {
        personDataAvatar.setOnClickListener(this)
    }

    private fun initView() {
        Glide.with(this).load(R.drawable.test_avatar_icon)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(personDataAvatar)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.personDataAvatar -> {
                openPopUpWindow()
            }
        }
    }

    private fun openPopUpWindow() {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_person_data, null)
        popupWindow =
            PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true

        popupWindow.animationStyle = R.style.popWindow
        popupWindow.showAtLocation(view, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
        popupWindow.setOnDismissListener {
            setBackground(1f)
        }
        setOnPopupViewClick(view)
        setBackground(0.5f)
    }

    private fun setOnPopupViewClick(view: View) {
        val mTakePhoto: TextView = view.findViewById(R.id.takePhoto)
        val mSelectPhoto: TextView = view.findViewById(R.id.selectPhoto)
        val mCancel: TextView = view.findViewById(R.id.personCancel)

        mTakePhoto.setOnClickListener {
            imagePickUtil.openCamera()
        }
        mSelectPhoto.setOnClickListener {
            imagePickUtil.openPhotos()
        }
        mCancel.setOnClickListener { popupWindow.dismiss() }
    }

    private fun setBackground(alpha: Float) {
        val lp: WindowManager.LayoutParams = window.attributes
        lp.alpha = alpha
        window.attributes = lp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK)
            return
        when(requestCode){
            REQUESTCODE_TAKE ->{
                val temp = File(externalCacheDir, HEAD_ICON_NAME)
                imagePickUtil.startPhotoZoom(Uri.fromFile(temp))
            }
            REQUESTCODE_PICK ->{
                data?.data?.let {
                    imagePickUtil.startPhotoZoom(it)
                }
            }
            REQUESTCODE_CUTTING ->{
                try {
                    imagePickUtil.mImageUri?.let {
                        val bitmap = BitmapFactory.decodeStream(contentResolver
                            .openInputStream(it))
                        Glide.with(this).load(bitmap)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(personDataAvatar)
                    }
                }catch (e: FileNotFoundException){
                    e.printStackTrace()
                }
            }
        }

        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }
}
