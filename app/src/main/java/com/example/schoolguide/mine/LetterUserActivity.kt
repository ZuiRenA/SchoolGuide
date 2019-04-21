package com.example.schoolguide.mine

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.util.PhotoPickUtil
import com.example.schoolguide.util.PhotoPickUtil.Companion.CUTTING
import com.example.schoolguide.util.PhotoPickUtil.Companion.PICK
import com.example.schoolguide.util.PhotoPickUtil.Companion.TAKE
import com.example.schoolguide.util.PickImagePopUtil
import com.example.schoolguide.view.BaseActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_letter_user.*
import kotlinx.android.synthetic.main.base_toolbar.*
import java.io.File

class LetterUserActivity : BaseActivity() {

    private lateinit var storageRef: StorageReference
    private var caUri: String? = null
    private var bitmap: Bitmap? = null
    private val photoPickUtil by lazy {
        PhotoPickUtil(context = this, str = "user_letter_ca.png")
    }
    private lateinit var viewModel: LetterUserViewModel

    private val pickImagePopUtil by lazy {
        PickImagePopUtil(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_user)
        viewModel = ViewModelProviders.of(this).get(LetterUserViewModel::class.java)

        baseToolbarTitle.text = getString(R.string.upload_info_ca)
        baseToolbar.setNavigationOnClickListener { finish() }

        storageRef = FirebaseStorage.getInstance().reference

        initClick()
        initNetWork()
    }

    private fun initNetWork() {
        observerAction(viewModel.uploadLetterLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    toast(getString(R.string.upload_ca_success))
                }.otherwise {
                    toast(response.errorReason ?: "")
                }
            }.run {
                toast(getString(R.string.network_error))
            }
        }
    }

    private fun initClick() {
        pickImagePopUtil.takePhotoAction {
            photoPickUtil.openCamera()
        }
        pickImagePopUtil.selectPhotoAction {
            photoPickUtil.openPhotos()
        }
        imageViewAddPhoto.onClick {
            pickImagePopUtil.openPopUpWindow()
        }
        imageViewClear.onClick {
            bitmap?.recycle()
            baseToolbarTV.visibility = View.INVISIBLE
            imageViewAddPhoto.visibility = View.VISIBLE
            textViewAddPhotoDescribe.visibility = View.VISIBLE
            Glide.with(this).clear(imageViewUploadCA)
            this.visibility = View.INVISIBLE
        }

        baseToolbarTV.onClick {
            caUri?.let { viewModel.uploadLetter(LoginUtil.user?.phone_number ?: 1, it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        pickImagePopUtil.popWindowAction {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        pickPhotoResult(requestCode, resultCode, data)
    }

    private fun pickPhotoResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED)
            return
        when (requestCode) {
            TAKE -> {
                val temp = File(externalCacheDir, photoPickUtil.imageName)
                photoPickUtil.startPhotoZoom(Uri.fromFile(temp))
            }
            PICK -> {
                data?.data?.let { photoPickUtil.startPhotoZoom(it) }
            }
            CUTTING -> {
                try {
                    photoPickUtil.imageUri?.let { uri ->
                        showLoading()
                        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        Log.d("bitmapSize", "${bitmap?.byteCount}")

                        imageViewAddPhoto.visibility = View.INVISIBLE
                        textViewAddPhotoDescribe.visibility = View.INVISIBLE
                        imageViewClear.visibility = View.VISIBLE
                        baseToolbarTV.visibility = View.VISIBLE

                        glideLoad()
                        uploadImage()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun glideLoad() {
        Glide.with(this).load(bitmap)
            .apply(RequestOptions.bitmapTransform(MultiTransformation<Bitmap>(
                CenterCrop(),
                RoundedCornersTransformation(8.dp, 0)
            )))
            .into(imageViewUploadCA)
    }

    private fun uploadImage() {
        photoPickUtil.imageUri?.let {
            val file = Uri.fromFile(File(it.path))
            val imageRef = storageRef.child("picture/${LoginUtil.user?.phone_number}_${file.lastPathSegment}")

            val uploadTask = imageRef.putFile(file)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { e ->
                        dismissLoading()
                        throw e
                    }
                }
                return@Continuation imageRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    caUri = task.result.toString()
                    dismissLoading()
                    Log.d("uri",  caUri)
                } else {
                    this.toast(getString(R.string.upload_avatar_error))
                    dismissLoading()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bitmap?.recycle()
    }
}


