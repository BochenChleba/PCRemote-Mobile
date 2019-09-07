package com.example.pcremote.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainNavigator{

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.navigator = this

        viewModel.initializeCommunicator()
        initializeViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    override fun showToast(text: String) {
        toast(text)
    }

    private fun initializeViewPager() {
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

}
