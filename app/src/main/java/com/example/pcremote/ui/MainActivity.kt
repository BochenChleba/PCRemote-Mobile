package com.example.pcremote.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.adapter.MainViewPagerAdapter
import com.example.pcremote.constants.MiscConstants
import com.example.pcremote.ext.hideKeyboard
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.tabWidget
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainNavigator {

    // touchpad communication
    // fullscreen touchpad

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeMainViewModel()
        initializeViewPager()
    }

    override fun onPause() {
        super.onPause()
        applicationContext.hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    override fun showToast(text: String) {
        toast(text)
    }

    override fun showToast(resourceId: Int) {
        toast(getString(resourceId))
    }

    private fun initializeMainViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.navigator = this
        viewModel.initializePreferencesInstance(this)
        viewModel.initializeCommunicator()
    }

    private fun initializeViewPager() {
        viewPager.offscreenPageLimit = MiscConstants.VIEW_PAGER_OFFSCREEN_PAGE_LIMIT
        viewPager.adapter = MainViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

}
