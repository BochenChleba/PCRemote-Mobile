package com.example.pcremote.ui.dialog.schedlued_shutdown.specified

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.MiscConstants
import com.example.pcremote.R
import com.example.pcremote.TimeConstants
import com.example.pcremote.ext.onActionDone
import com.example.pcremote.ext.toIntOrZero
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.MainActivity
import com.example.pcremote.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_shutdown_specified.*
import org.jetbrains.anko.support.v4.toast

class ShutdownSpecifiedFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var privateViewModel: ShutdownSpecifiedViewModel

    companion object {
        lateinit var dismissCallback: ()->Unit
        fun newInstance(): ShutdownSpecifiedFragment {
            return ShutdownSpecifiedFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shutdown_specified, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {fragmentActivity ->
            viewModel = ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)
            privateViewModel = ViewModelProviders.of(fragmentActivity).get(ShutdownSpecifiedViewModel::class.java)
            setClickListeners()
            setTextWatchers()
        }
    }

    override fun onResume() {
        super.onResume()
        hoursEt?.requestFocus()
    }

    private fun setClickListeners(){
        declineTv?.setOnClickListener { dismissCallback.invoke() }
        confirmTv?.setOnClickListener { performShutdown() }
        secondsEt?.onActionDone { performShutdown() }
    }

    private fun setTextWatchers() {
        hoursEt?.addTextChangedListener {
            val text = it.toString()
            if (text.length == MiscConstants.TIME_INPUT_LENGTH && text.toIntOrZero() < TimeConstants.HOURS_IN_DAY ) {
                minutesEt?.requestFocus()
            }
        }
        minutesEt?.addTextChangedListener {
            val text = it.toString()
            if (text.length == MiscConstants.TIME_INPUT_LENGTH && text.toIntOrZero() < TimeConstants.MINUTES_IN_HOUR ) {
                secondsEt?.requestFocus()
            }
        }
    }

    private fun performShutdown() {
        val timeout = privateViewModel.calculateTimeoutInSeconds(
            hoursEt?.text.toString(),
            minutesEt?.text.toString(),
            secondsEt?.text.toString()
        )

        if (timeout == null) {
            toast(R.string.toast_invalid_time)
            return
        }

        viewModel.communicate(Communicator.COMMAND_SCHEDULE_SHUTDOWN, timeout.toString()) {
            toast(getString(R.string.shutdown_scheduled))
            dismissCallback.invoke()
        }
    }
}