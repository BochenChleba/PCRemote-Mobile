package com.example.pcremote.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.toast

abstract class BaseActivity<T: BaseViewModel<*>> : AppCompatActivity(), BaseNavigator {
    // fullscreen touchpad
    // keyboard

    abstract var viewModel: T
    abstract fun initializeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViewModel()
        viewModel.initializePreferencesInstance(this)
        viewModel.initializeCommunicator()
    }

    override fun onPause() {
        super.onPause()
     //   applicationContext.hideKeyboard()
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

}
