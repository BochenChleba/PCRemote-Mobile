package com.example.pcremote.ui.abstraction

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.data.enum.ConnectionStatus
import com.example.pcremote.ui.activity.main.MainViewModel
import org.jetbrains.anko.support.v4.toast

abstract class BaseFragment : Fragment(),
    BaseNavigator {
    var sharedViewModel: MainViewModel? = null

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

    fun checkConnectionStatus(): Unit? {
        return if (sharedViewModel?.connectionStatus?.value != ConnectionStatus.CONNECTED) {
            toast(R.string.toast_no_connection)
            null
        } else {}
    }

}
