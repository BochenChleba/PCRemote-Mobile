package com.example.pcremote.ui.dialog.schedlued_shutdown

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.pcremote.R
import com.example.pcremote.adapter.ShutdownViewPagerAdapter
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ext.showKeyboard
import com.example.pcremote.ui.dialog.schedlued_shutdown.countdown.ShutdownCountdownFragment
import com.example.pcremote.ui.dialog.schedlued_shutdown.specified.ShutdownSpecifiedFragment
import kotlinx.android.synthetic.main.dialog_scheduled_shutdown.*

class ScheduledShutdownDialog : DialogFragment() {

    companion object {
        const val TAG = "scheduled shutdown"

        fun newInstance(): ScheduledShutdownDialog {
            return ScheduledShutdownDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.dialog_scheduled_shutdown, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ShutdownCountdownFragment.dismissCallback = { dismiss() }
        ShutdownSpecifiedFragment.dismissCallback = { dismiss() }

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

}
