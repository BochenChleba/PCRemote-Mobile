package com.example.pcremote.ui.activity.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.adapter.MainViewPagerAdapter
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ui.activity.base.BaseActivity
import com.example.pcremote.ui.fragment.touchpad.TouchpadFragment
import com.example.pcremote.view.scroll_view.ISwipeableViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>(), MainNavigator {

    //todo swiping on touch pad fragment

    override lateinit var viewModel: MainViewModel
    private lateinit var viewPagerAdapter: MainViewPagerAdapter

    override fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViewPager()
    }

    private fun initializeViewPager() {
        viewPager.offscreenPageLimit = MiscConstants.VIEW_PAGER_OFFSCREEN_PAGE_LIMIT
        viewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                hideKeyboard(mainRootLayout)
            }
        })
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is TouchpadFragment -> {
                fragment.fragmentSwipeListener = swipeListener
            }
        }
    }

    private val swipeListener: ISwipeableViewPager = object : ISwipeableViewPager {
        override fun onSwipeLeft() {
            selectPreviousTab()
        }
        override fun onSwipeRight() {
            selectNextTab()
        }
        override fun onSwipeTop() {}
        override fun onSwipeBottom() {}
    }

    private fun selectPreviousTab() {
        val currentTabIndex = tabLayout.selectedTabPosition
        val previousTabIndex = currentTabIndex - 1
        if (previousTabIndex > 0) {
            val previousTab = tabLayout.getTabAt(previousTabIndex)
            previousTab?.select()
        }
    }

    private fun selectNextTab() {
        val currentTabIndex = tabLayout.selectedTabPosition
        val nextTabIndex = currentTabIndex +1
        if (nextTabIndex > 0) {
            val nextTab = tabLayout.getTabAt(nextTabIndex)
            nextTab?.select()
        }
    }
}
