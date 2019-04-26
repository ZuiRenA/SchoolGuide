package com.example.schoolguide.mine

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.mineView.MyEditTextDialog
import com.example.schoolguide.model.School
import com.example.schoolguide.util.*
import com.example.schoolguide.view.BaseActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_person_data.*
import kotlinx.android.synthetic.main.base_toolbar.*
import java.io.File
import java.io.FileNotFoundException
import java.util.ArrayList
import java.util.regex.Pattern

class PersonDataActivity : BaseActivity(), View.OnClickListener {

    companion object {
        private const val NICKNAME = 10001
        private const val NAME = 10002
        private const val ID_CARD = 10003
        private const val TYPE_SCHOOL = 10005
        private const val TYPE_COLLEGE = 10006
    }

    private lateinit var imagePickUtil: ImagePickUtil
    private lateinit var storageRef: StorageReference
    private lateinit var viewModel: PersonDataViewModel
    private var schoolName: String? = null
    private var collegeName: String? = null
    private val pickImagePopUtil by lazy {
        PickImagePopUtil(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_data)
        imagePickUtil = ImagePickUtil(this)
        storageRef = FirebaseStorage.getInstance().reference
        baseToolbarTV.visibility = View.VISIBLE
        baseToolbarTitle.text = getString(R.string.person_data)
        baseToolbar.setNavigationOnClickListener { finish() }

        viewModel = ViewModelProviders.of(this).get(PersonDataViewModel::class.java)

        pickImagePopUtil.takePhotoAction {
            imagePickUtil.openCamera()
        }

        pickImagePopUtil.selectPhotoAction {
            imagePickUtil.openPhotos()
        }

        initView()
        initClick()
        initNetWork()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.personDataAvatar -> {
                pickImagePopUtil.openPopUpWindow()
            }
            R.id.baseToolbarTV -> {
                showLoading()
                uploadAvatar()
            }
            R.id.personDataNickname -> {
                inputName(getString(R.string.nickname_input), type = NICKNAME)
            }
            R.id.personDataSchool -> {
                viewModel.school()
            }
            R.id.personDataCollege -> {
                viewModel.college(1)
            }
            R.id.personDataName -> {
                inputName(getString(R.string.name_input), type = NAME)
            }
            R.id.personDataIdCard -> {
                inputName(getString(R.string.id_card_input), type = ID_CARD)
            }
        }
    }

    private fun initNetWork() {
        observerAction(viewModel.schoolLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    pickDialog(response.respond, TYPE_SCHOOL)
                }
            }
        }

        observerAction(viewModel.collegeLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    pickDialog(response.respond, TYPE_COLLEGE)
                }.otherwise { toast(response.errorReason ?: "") }
            }
        }

        observerAction(viewModel.uploadLiveData) {
            dismissLoading()
            it?.let { response ->
                (response.isSuccess).yes { toast(getString(R.string.upload_person_success)) }
                    .otherwise { toast(response.errorReason!!) }
            }
        }
    }

    private fun pickDialog(list: List<Any>, type: Int) {
        val arrayList = list.copyToArrayList()
        val title = (type == TYPE_SCHOOL).yes { getString(R.string.school_select) }
            .otherwise { getString(R.string.college_select) }
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
        val temp = arrayOfNulls<String>(arrayList.size)

        (type == TYPE_SCHOOL).yes {
            (arrayList as ArrayList<School>).forEachIndexed { index, school ->
                temp[index] = school.school_name
            }
        }.otherwise {
            (arrayList as ArrayList<String>).forEachIndexed { index, college ->
                temp[index] = college
            }
        }

        builder.setSingleChoiceItems(temp, -1) { _, index ->
            (type == TYPE_SCHOOL).yes { schoolName = temp[index] }.otherwise { collegeName = temp[index] }
        }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            (type == TYPE_SCHOOL).yes { schoolName = null }.otherwise { collegeName = null }
            dialog.dismiss()
        }.setPositiveButton(R.string.assign) { dialog, _ ->
            if (type == TYPE_SCHOOL) {
                schoolName?.let { personSchoolStatus.text = schoolName }
            } else {
                collegeName?.let { personCollegeStatus.text = collegeName }
            }
            dialog.dismiss()
        }.show()
    }

    private fun uploadAvatar() {
        (imagePickUtil.mImageUri != null).yes {
            val file = Uri.fromFile(File(imagePickUtil.mImageUri?.path))
            val imageRef = storageRef.child("picture/${LoginUtil.user?.phone_number}_${file.lastPathSegment}")
            val uploadTask = imageRef.putFile(file)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        dismissLoading()
                        throw it
                    }
                }
                return@Continuation imageRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    uploadToServer(downloadUri.toString())
                } else {
                    this.toast(getString(R.string.upload_avatar_error))
                    dismissLoading()
                }
            }
        }.otherwise {
            uploadToServer()
        }
    }

    private fun uploadToServer(avatar: String? = null) {
        val nowAvatar = avatar ?: LoginUtil.user?.user_avatar
        val name = (personNicknameStatus.text == LoginUtil.user?.name).no { personNicknameStatus.text.toString() }
            .otherwise { null }
        val school = isNotNull(personSchoolStatus.text).yes { personSchoolStatus.text.toString() }.otherwise { null }
        val college = isNotNull(personCollegeStatus.text).yes { personCollegeStatus.text.toString() }.otherwise { null }
        val userName = isNotNull(personNameStatus.text).yes { personNameStatus.text.toString() }.otherwise { null }
        val id = isNotNull(personIdStatus.text).yes { personIdStatus.text.toString() }.otherwise { null }
        viewModel.upload(
            name = name,
            avatar = nowAvatar,
            school = school,
            college = college,
            userName = userName,
            idCard = id
        )
    }

    private fun isNotNull(content: CharSequence): Boolean =
        (content != getString(R.string.not_in_write)).yes { true }.otherwise { false }

    private fun inputName(title: String, message: String? = null, type: Int) {
        MyEditTextDialog(this)
            .setTitle(title)
            .setMessage(message)
            .optionOneCLick(getString(R.string.assign)) { content, dialog ->
                TextUtils.isEmpty(content)
                    .yes { toast(getString(R.string.input_null_error)) }
                    .otherwise {
                        when (type) {
                            NICKNAME -> {
                                personNicknameStatus.text = content
                                dialog.dismiss()
                            }
                            NAME -> {
                                personNameStatus.text = content
                                dialog.dismiss()
                            }
                            ID_CARD -> {
                                personIdStatus.text = content
                                dialog.dismiss()
                            }
                        }
                    }
            }.optionTwoCLick(getString(R.string.cancel)) {
                it.dismiss()
            }.show()
    }

    private fun initView() {
        val userInfo = LoginUtil.user

        personDataAvatar.show(
            uri = userInfo?.user_avatar,
            placeholder = ColorDrawable(Color.YELLOW),
            error = R.drawable.test_avatar_icon
        ) {
            RequestOptions().centerCrop().transform(CircleCrop())
        }

        isRight(userInfo?.name)
            .yes { personNicknameStatus.text = userInfo?.name }

        isRight(userInfo?.user_school)
            .yes { personSchoolStatus.text = userInfo?.user_school }
            .otherwise { personSchoolStatus.text = getString(R.string.not_in_write) }

        isRight(userInfo?.user_college)
            .yes { personCollegeStatus.text = userInfo?.user_college }
            .otherwise { personCollegeStatus.text = getString(R.string.not_in_write) }

        isRight(userInfo?.user_name)
            .yes { personNameStatus.text = userInfo?.user_name }
            .otherwise { personNameStatus.text = getString(R.string.not_in_write) }

        isRight(userInfo?.user_id_card)
            .yes { personIdStatus.text = getString(R.string.yet_in_write) }
            .otherwise { personIdStatus.text = getString(R.string.not_in_write) }

        isRight(userInfo?.user_dormitory)
            .yes { personDorStatus.text = userInfo?.user_dormitory }
            .otherwise { personDorStatus.text = getString(R.string.not_in_write) }
    }

    private fun isRight(content: String?): Boolean =
        (content != null && content != "").yes { true }.otherwise { false }

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

        pickImagePopUtil.popWindowAction {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    private fun initClick() {
        personDataAvatar.setOnClickListener(this)
        baseToolbarTV.setOnClickListener(this)
        personDataNickname.setOnClickListener(this)
        personDataSchool.setOnClickListener(this)
        personDataCollege.setOnClickListener(this)
        personDataName.setOnClickListener(this)
        personDataIdCard.setOnClickListener(this)
    }
}










