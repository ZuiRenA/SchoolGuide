package com.example.schoolguide.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import java.io.File

class PhotoPickUtil(private var context: Activity, str: String?) {
    companion object {
        const val TAKE = 10001
        const val PICK = 10002
        const val CUTTING = 10003
    }

    var imageUri: Uri? = null
    var imageName: String? = null

    init {
        initPhotoError()
        imageName = str
    }

    fun openCamera() {
        // 创建打开系统相机的意图
        val takeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 设置参数。将拍照所得的照片存到磁盘中；照片文件的位置，此处是在外置SD卡
        imageName?.let {
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(context.externalCacheDir, it)))
        }
        // 调用系统相机
        context.startActivityForResult(takeIntent, TAKE)
    }

    fun openPhotos() {
        // 创建打开系统图库的意图
        val pickIntent = Intent(Intent.ACTION_PICK, null)
        // 设置Intent的参数，选择MediaStore中的图片
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        // 开启意图
        context.startActivityForResult(pickIntent, PICK)
    }

    fun startPhotoZoom(uri: Uri) {
        imageName?.let {
            imageUri = Uri.fromFile(File(context.externalCacheDir, it))
        }
        val intent = Intent("com.android.camera.action.CROP")
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "false")
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)

        // 调用系统的图片剪切
        context.startActivityForResult(intent, CUTTING)
    }

    @SuppressLint("NewApi")
    fun initPhotoError() {
        // android 7.0系统解决拍照的问题
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }
}