package com.example.pcremote.ui.activity.main

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.adapter.MainViewPagerAdapter
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ext.showKeyboard
import com.example.pcremote.ui.activity.base.BaseActivity
import com.example.pcremote.ui.fragment.keyboard.KeyboardFragment
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>(), MainNavigator {

    override lateinit var viewModel: MainViewModel
    private lateinit var viewPagerAdapter: MainViewPagerAdapter

    override fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.initializeCommunicator()
        requestPermissions()
        initializeViewPager()
    }

    private fun initializeViewPager() {
        viewPager.offscreenPageLimit = MiscConstants.VIEW_PAGER_OFFSCREEN_PAGE_LIMIT
        viewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun requestPermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (!report.areAllPermissionsGranted()) {
                        finish()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?) {}
            }).check()
    }
}
