package com.example.schoolguide.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import java.io.File


val REQUESTCODE_TAKE = 1001
val REQUESTCODE_PICK = 1002
val REQUESTCODE_CUTTING = 1003
val HEAD_ICON_NAME = "user_icon.png"

class ImagePickUtil(var context: Activity) {

    var mImageUri: Uri? = null

    init {
        initPhotoError()
    }

    fun openCamera() {
        // 创建打开系统相机的意图
        val takeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 设置参数。将拍照所得的照片存到磁盘中；照片文件的位置，此处是在外置SD卡
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(context.externalCacheDir, HEAD_ICON_NAME)))
        // 调用系统相机
        context.startActivityForResult(takeIntent, REQUESTCODE_TAKE)
    }

    fun openPhotos() {
        // 创建打开系统图库的意图
        val pickIntent = Intent(Intent.ACTION_PICK, null)
        // 设置Intent的参数，选择MediaStore中的图片
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        // 开启意图
        context.startActivityForResult(pickIntent, REQUESTCODE_PICK)
    }

    fun startPhotoZoom(uri: Uri) {
        mImageUri = Uri.fromFile(File(context.externalCacheDir, HEAD_ICON_NAME))
        val intent = Intent("com.android.camera.action.CROP")
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高，这里可以将宽高作为参数传递进来
        intent.putExtra("outputX", 1024)
        intent.putExtra("outputY", 1024)

        // 其实加上下面这两句就可以实现基本功能，
        //但是这样做我们会直接得到图片的数据，以bitmap的形式返回，在Intent中。而Intent传递数据大小有限制，1kb=1024字节，这样就对最后的图片的像素有限制。
        //intent.putExtra("return-data", true);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);

        // 解决不能传图片，Intent传递数据大小有限制，1kb=1024字节
        // 方法：裁剪后的数据不以bitmap的形式返回，而是放到磁盘中，更方便上传和本地缓存
        // 设置裁剪后的数据不以bitmap的形式返回，剪切后图片的位置，图片是否压缩等
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)

        // 调用系统的图片剪切
        context.startActivityForResult(intent, REQUESTCODE_CUTTING)
    }

    @SuppressLint("NewApi")
    fun initPhotoError() {
        // android 7.0系统解决拍照的问题
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }

}