package com.example.pcremote.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.adapter.MainViewPagerAdapter
import com.example.pcremote.ext.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainNavigator {

    // todo divide scheduled shutdown to shutdown at and shutdown in

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.navigator = this
        viewModel.initializePreferencesInstance(this)
        viewModel.initializeCommunicator()
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

    private fun initializeViewPager() {
        viewPager.adapter = MainViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

}
