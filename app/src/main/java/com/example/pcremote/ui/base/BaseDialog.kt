package com.example.pcremote.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ui.MainViewModel
import kotlinx.android.synthetic.main.dialog_simple.*

abstract class BaseDialog : DialogFragment() {
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

}
