package com.example.pcremote.ui.activity.input

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ui.activity.base.BaseActivity

class InputActivity : BaseActivity<InputViewModel>(), InputNavigator {

    override lateinit var viewModel: InputViewModel

    override fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(InputViewModel::class.java)
        viewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
    }

}
