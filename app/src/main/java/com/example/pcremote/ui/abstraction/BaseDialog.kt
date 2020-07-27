package com.example.pcremote.ui.abstraction

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.ui.activity.main.MainViewModel

abstract class BaseDialog : DialogFragment(),
    BaseNavigator {
    var sharedViewModel: MainViewModel? = null

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { fragmentActivity ->
            sharedViewModel = ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)
        }
    }

    override fun showToast(text: String) {
        (activity as? BaseActivity<*>)?.showToast(text)
    }

    override fun showToast(resourceId: Int) {
        (activity as? BaseActivity<*>)?.showToast(resourceId)
    }
}
