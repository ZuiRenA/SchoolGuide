package com.example.schoolguide.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.example.schoolguide.R

class  PickImagePopUtil(private  val context: Activity) {

    private lateinit var popupWindow: PopupWindow
    private var takePhoto: TakePhoto? = null
    private var selectPhoto: SelectPhoto? = null

    /**
     * 弹出popWindow
     */
    @SuppressLint("InflateParams")
    fun openPopUpWindow() {
        val view = LayoutInflater.from(context).inflate(R.layout.pop_person_data, null)
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

    private fun setBackground(alpha: Float) {
        val lp: WindowManager.LayoutParams = context.window.attributes
        lp.alpha = alpha
        context.window.attributes = lp
    }

    private fun setOnPopupViewClick(view: View) {
        val mTakePhoto: TextView = view.findViewById(R.id.takePhoto)
        val mSelectPhoto: TextView = view.findViewById(R.id.selectPhoto)
        val mCancel: TextView = view.findViewById(R.id.personCancel)

        mTakePhoto.setOnClickListener {
            takePhoto?.takePhotoAction()
        }
        mSelectPhoto.setOnClickListener {
            selectPhoto?.selectPhotoAction()
        }
        mCancel.setOnClickListener { popupWindow.dismiss() }
    }


    /**
     * 对popWindow进行操作的地方
     */
    fun popWindowAction(block: (PopupWindow) -> Unit): PickImagePopUtil {
        block(popupWindow)
        return this
    }

    fun takePhotoAction(block: () -> Unit): PickImagePopUtil {
        takeActionFun(object : TakePhoto{
            override fun takePhotoAction() {
                block()
            }
        })

        return this
    }

    fun selectPhotoAction(block: () -> Unit) {
        selectActionFun(object : SelectPhoto {
            override fun selectPhotoAction() {
                block()
            }
        })
    }

    private fun takeActionFun(takePhoto: TakePhoto) {
        this.takePhoto = takePhoto
    }

    private fun selectActionFun(selectPhoto: SelectPhoto) {
        this.selectPhoto = selectPhoto
    }

    interface TakePhoto {
        fun takePhotoAction()
    }

    interface SelectPhoto {
        fun selectPhotoAction()
    }
}
