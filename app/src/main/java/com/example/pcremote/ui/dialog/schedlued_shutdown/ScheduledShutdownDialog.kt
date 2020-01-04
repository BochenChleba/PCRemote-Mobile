package com.example.pcremote.ui.dialog.schedlued_shutdown

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.adapter.ShutdownViewPagerAdapter
import com.example.pcremote.constants.TimeConstants
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ext.showKeyboard
import com.example.pcremote.ui.MainViewModel
import com.example.pcremote.ui.base.BaseDialog
import com.example.pcremote.ui.dialog.schedlued_shutdown.countdown.ShutdownCountdownFragment
import com.example.pcremote.ui.dialog.schedlued_shutdown.specified.ShutdownSpecifiedFragment
import kotlinx.android.synthetic.main.dialog_scheduled_shutdown.*
import org.jetbrains.anko.support.v4.toast

class ScheduledShutdownDialog : BaseDialog() {

    lateinit var shutdownScheduledCallback: ()->Unit

    companion object {
        const val TAG = "scheduled shutdown"
        fun newInstance(): ScheduledShutdownDialog {
            return ScheduledShutdownDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.dialog_scheduled_shutdown, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { ctx ->
            viewPager?.adapter = ShutdownViewPagerAdapter(childFragmentManager, ctx)
            tabLayout?.setupWithViewPager(viewPager)
            ctx.showKeyboard()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        context?.hideKeyboard()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        (childFragment as? ShutdownCountdownFragment)?.let { fragment ->
            fragment.dismissCallback = {
                dismiss()
            }
            fragment.shutdownScheduledCallback = { timeout ->
                onShutdownScheduled(timeout)
            }
        }
        (childFragment as? ShutdownSpecifiedFragment)?.let { fragment ->
            fragment.dismissCallback = {
                dismiss()
            }
            fragment.shutdownScheduledCallback = { timeout ->
                onShutdownScheduled(timeout)
            }
        }
    }

    private fun onShutdownScheduled(timeout: Int) {
        sharedViewModel?.prefs?.setShutdownTime(
            System.currentTimeMillis() + timeout * TimeConstants.MILLIS_IN_SECOND
        )
        toast(getString(R.string.shutdown_scheduled))
        shutdownScheduledCallback.invoke()
        dismiss()
    }

}
