package com.example.schoolguide

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.extUtil.toast
import com.example.schoolguide.login.LoginActivity
import com.example.schoolguide.login.LoginViewModel
import com.example.schoolguide.main.GuideFragment
import com.example.schoolguide.main.HomeFragment
import com.example.schoolguide.main.MineFragment
import com.example.schoolguide.model.LogoutEvent
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.view.BaseActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {

    private lateinit var bottomNavigationBar: BottomNavigationBar
    private lateinit var fragmentHome: HomeFragment
    private lateinit var fragmentGuide: GuideFragment
    private lateinit var fragmentMine: MineFragment
    private lateinit var viewModel: LoginViewModel

    private var lastSelectedPosition = 0

    companion object {
        private const val Home = 0
        private const val Guide = 1
        private const val Mine = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        EventBus.getDefault().register(this)

        login()

        fragmentHome = HomeFragment.newInstance()
        fragmentGuide = GuideFragment.newInstance()
        fragmentMine = MineFragment.newInstance()

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        attachBar()
        bottomNavigationBar.setTabSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity_frag_container, fragmentHome)
                .commit()

            bottomNavigationBar.setFirstSelectedPosition(lastSelectedPosition)
        }
    }

    private fun login() {
        val loginInfo = LoginUtil.getLoginInfo()
        viewModel.loginLiveData?.observe(this, Observer {
            if (it != null) {
                if(!it.isSuccess) {
                    it.errorReason?.let { error ->
                        toast(error)
                    }

                    intent(LoginActivity::class.java)
                    finish()
                }
            } else {
                toast(getString(R.string.network_error))
            }
        })

        viewModel.login(loginInfo.account.toLong(), loginInfo.password)
    }

    private fun attachBar() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomNavigationBar.addItem(
            BottomNavigationItem(
                R.drawable.home_menu_11,
                resources.getString(R.string.home)
            ).setActiveColorResource(R.color.loginMain)
        )
            .addItem(
                BottomNavigationItem(
                    R.drawable.home_menu_12,
                    resources.getString(R.string.guide)
                ).setActiveColorResource(R.color.loginMain)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.home_menu_13,
                    resources.getString(R.string.mine)
                ).setActiveColorResource(R.color.loginMain)
            )
            .initialise()
    }

    override fun onTabReselected(position: Int) {}

    override fun onTabUnselected(position: Int) {}

    override fun onTabSelected(position: Int) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        lastSelectedPosition = position
        when (position) {
            Home -> {
                fragmentManager.replace(R.id.main_activity_frag_container, fragmentHome)
            }
            Guide -> {
                fragmentManager.replace(R.id.main_activity_frag_container, fragmentGuide)
            }
            Mine -> {
                fragmentManager.replace(R.id.main_activity_frag_container, fragmentMine)
            }
        }

        fragmentManager.commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun logOutEvent(event: LogoutEvent) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
