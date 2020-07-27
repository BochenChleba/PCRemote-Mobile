package com.example.pcremote.ui.abstraction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.toast

abstract class BaseActivity<T: BaseViewModel<*>> : AppCompatActivity(),
    BaseNavigator {
    // fullscreen touchpad
    // keyboard

    abstract var viewModel: T
    abstract fun initializeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViewModel()
        viewModel.initializePreferencesInstance(this)
    }

    override fun showToast(text: String) {
        toast(text)
    }

    override fun showToast(resourceId: Int) {
        toast(getString(resourceId))
    }

}
