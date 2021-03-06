package com.example.pcremote.ui.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pcremote.R
import com.example.pcremote.ui.abstraction.BaseDialog
import kotlinx.android.synthetic.main.dialog_simple.*

abstract class SimpleDialog : BaseDialog() {
    abstract val titleStringRes: Int
    abstract val messageStringRes: Int
    abstract val confirmCallback: ()->Any?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_simple, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setClickListeners()
    }

    private fun setLayout() {
        titleTv?.text = getString(titleStringRes)
        messageTv?.text = getString(messageStringRes)
    }

    private fun setClickListeners() {
        confirmTv?.setOnClickListener {
            confirmCallback.invoke()
            dismiss()
        }
        declineTv?.setOnClickListener { dismiss() }
    }

}
