package com.example.pcremote.ui.dialog.enter_ip

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pcremote.R
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ui.dialog.base.BaseDialog
import com.example.pcremote.singleton.Preferences
import kotlinx.android.synthetic.main.dialog_enter_ip.*

class EnterIpDialog : BaseDialog() {

    companion object {
        const val TAG = "enter IP"
        fun newInstance(): EnterIpDialog {
            return EnterIpDialog()
        }
    }

    lateinit var confirmCallback: (String)->Unit

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
        context?.hideKeyboard(enterIpRootLayout)
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
