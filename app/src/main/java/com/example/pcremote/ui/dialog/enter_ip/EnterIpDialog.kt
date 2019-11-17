package com.example.pcremote.ui.dialog.enter_ip

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
import com.example.pcremote.util.Preferences
import kotlinx.android.synthetic.main.dialog_enter_ip.*
import kotlinx.android.synthetic.main.dialog_scheduled_shutdown.*
import org.jetbrains.anko.support.v4.toast

class EnterIpDialog : BaseDialog() {

    companion object {
        const val TAG = "enter IP"

        lateinit var confirmCallback: (String)->Unit

        fun newInstance(): EnterIpDialog {
            return EnterIpDialog()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.dialog_enter_ip, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { fragmentActivity ->
            ipEt?.setText(Preferences.getInstance(fragmentActivity).getIpAddress())
            setOnClickListeners()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        context?.hideKeyboard()
    }

    private fun setOnClickListeners() {
        declineTv?.setOnClickListener {
            dismiss()
        }
        confirmTv?.setOnClickListener {
            confirmCallback.invoke(ipEt.text.toString())
            dismiss()
        }
    }

}
