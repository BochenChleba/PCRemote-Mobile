package com.example.pcremote.ui.dialog.shutdown_now

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.MainViewModel
import kotlinx.android.synthetic.main.dialog_shutdown_now.*

class ShutdownNowDialog : DialogFragment() {

    companion object {
        const val TAG = "shutdown now"

        fun newInstance(): ShutdownNowDialog {
            return ShutdownNowDialog()
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_shutdown_now, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { fragmentActivity ->
            viewModel = ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)

            confirmTv.setOnClickListener {
                viewModel.communicate(Communicator.COMMAND_SHUTDOWN_NOW) {
                    dismiss()
                }
            }

            declineTv.setOnClickListener { dismiss() }
        }
    }

}
