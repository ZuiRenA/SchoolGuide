package com.example.schoolguide.guide

import android.os.Bundle
import com.example.schoolguide.R
import com.example.schoolguide.view.BaseActivity
import kotlinx.android.synthetic.main.base_toolbar.*

class SelectDorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_dor)

        baseToolbarTitle.text = getString(R.string.select_dor)
        baseToolbar.setNavigationOnClickListener { finish() }
    }
}
