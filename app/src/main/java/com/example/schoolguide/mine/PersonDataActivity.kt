package com.example.schoolguide.mine

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.toast
import com.example.schoolguide.util.*
import com.example.schoolguide.view.BaseActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_person_data.*
import java.io.File
import java.io.FileNotFoundException
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.base_toolbar.*

class PersonDataActivity : BaseActivity(), View.OnClickListener {

    private lateinit var popupWindow: PopupWindow
    private lateinit var imagePickUtil: ImagePickUtil
    private lateinit var storageRef: StorageReference
    private lateinit var viewModel: PersonDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_data)
        imagePickUtil = ImagePickUtil(this)
        storageRef = FirebaseStorage.getInstance().reference
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTitle.text = getString(R.string.person_data)
        baseToolbar.setNavigationOnClickListener { finish() }

        viewModel = ViewModelProviders.of(this).get(PersonDataViewModel::class.java)

        initView()
        initClick()
    }

    private fun initClick() {
        personDataAvatar.setOnClickListener(this)
        baseToolbarTV.setOnClickListener(this)
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

            R.id.baseToolbarTV -> {
                imagePickUtil.mImageUri?.let { uri ->
                    val file = Uri.fromFile(File(uri.path))
                    val imageRef = storageRef.child("picture/${file.lastPathSegment}")
                    val uploadTask = imageRef.putFile(file)

                    val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation imageRef.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            this.toast(downloadUri.toString())
                        } else {
                            this.toast("上传失败")
                        }
                    }
                }
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
        when (requestCode) {
            REQUESTCODE_TAKE -> {
                val temp = File(externalCacheDir, HEAD_ICON_NAME)
                imagePickUtil.startPhotoZoom(Uri.fromFile(temp))
            }
            REQUESTCODE_PICK -> {
                data?.data?.let {
                    imagePickUtil.startPhotoZoom(it)
                }
            }
            REQUESTCODE_CUTTING -> {
                try {
                    imagePickUtil.mImageUri?.let { uri ->
                        val bitmap = BitmapFactory.decodeStream(
                            contentResolver
                                .openInputStream(uri)
                        )
                        Glide.with(this).load(bitmap)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(personDataAvatar)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }

        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }
}
