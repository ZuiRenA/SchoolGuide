package com.example.schoolguide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.PopupWindow
import com.example.schoolguide.view.BaseActivity
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder
import kotlinx.android.synthetic.main.activity_easter_egg.*

class EasterEggActivity : BaseActivity() {

    private var countRecord = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easter_egg)

        val uri = "android.resource://" + packageName + "/" + R.raw.aone
        testPlayer.setVideoURI(Uri.parse(uri))
        testPlayer.start()

        testPlayer.setOnCompletionListener {
            if (countRecord < 3) {
                testPlayer.start()
                countRecord++
            } else {
                val dialog = NiftyDialogBuilder.getInstance(this)
                dialog.withTitle("是否去开启权限")
                    .withTitleColor(R.color.black)
                    .withMessage("三天之内杀了你!?")
                    .withMessageColor(R.color.black)
                    .withButton1Text("我同意")
                    .withButton2Text("但是, 我拒绝")
                    .setButton1Click {
                        val packUri = Uri.parse("package:$packageName")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packUri)
                        startActivity(intent)
                        finish()
                    }
                    .setButton2Click {
                        countRecord = 0
                        testPlayer.start()
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onBackPressed() {

    }
}
