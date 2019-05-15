package com.example.schoolguide.admin

import android.os.Bundle
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.action
import com.example.schoolguide.extUtil.replace
import com.example.schoolguide.model.AdminFinishEvent
import com.example.schoolguide.view.BaseActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AdminActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {

    private lateinit var bottomNavigationBar: BottomNavigationBar
    private lateinit var fragmentUserInfo: UserInfoFragment
    private lateinit var fragmentSchoolInfo: SchoolInfoFragment
    private lateinit var fragmentGuideInfo: GuideInfoFragment
    private lateinit var fragmentDormitoryInfo: DormitoryInfoFragment
    private var lastSelectedPosition = 0

    companion object {
        private const val User = 0
        private const val School = 1
        private const val Guide = 2
        private const val Dormitory = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        fragmentUserInfo = UserInfoFragment.newInstance()
        fragmentSchoolInfo = SchoolInfoFragment.newInstance()
        fragmentGuideInfo = GuideInfoFragment.newInstance()
        fragmentDormitoryInfo = DormitoryInfoFragment.newInstance()

        bottomNavigationBar = findViewById(R.id.adminBottomBar)
        attachBar()
        bottomNavigationBar.setTabSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.replace(R.id.adminContainer, fragmentUserInfo)
            bottomNavigationBar.setFirstSelectedPosition(lastSelectedPosition)
        }

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: AdminFinishEvent) {
        finish()
    }

    private fun attachBar() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomNavigationBar.addItem(
            BottomNavigationItem(
                R.drawable.user_info,
                resources.getString(R.string.user_table)
            ).setActiveColor(R.color.loginMain)
        )
            .addItem(
                BottomNavigationItem(
                    R.drawable.school_info_admin,
                    resources.getString(R.string.school_table)
                ).setActiveColor(R.color.loginMain)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.guide_admin,
                    resources.getString(R.string.guide_table)
                ).setActiveColor(R.color.loginMain)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.dormitory_admin,
                    resources.getString(R.string.dormitory_table)
                ).setActiveColor(R.color.loginMain)
            )
            .initialise()
    }

    override fun onTabReselected(position: Int) {}

    override fun onTabUnselected(position: Int) {}

    override fun onTabSelected(position: Int) {
        supportFragmentManager.action {
            lastSelectedPosition = position
            when (position) {
                User -> {
                    it.replace(R.id.adminContainer, fragmentUserInfo)
                }
                School -> {
                    it.replace(R.id.adminContainer, fragmentSchoolInfo)
                }
                Guide -> {
                    it.replace(R.id.adminContainer, fragmentGuideInfo)
                }
                Dormitory -> {
                    it.replace(R.id.adminContainer, fragmentDormitoryInfo)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
