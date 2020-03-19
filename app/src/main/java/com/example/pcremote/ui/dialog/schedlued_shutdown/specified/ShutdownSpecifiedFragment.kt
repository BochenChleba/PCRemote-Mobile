package com.example.pcremote.ui.dialog.schedlued_shutdown.specified

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.R
import com.example.pcremote.data.constants.TimeConstants
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ext.onActionDone
import com.example.pcremote.ext.toIntOrZero
import com.example.pcremote.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_shutdown_specified.*
import org.jetbrains.anko.support.v4.toast

class ShutdownSpecifiedFragment: BaseFragment(), ShutdownSpecifiedNavigator {

    private lateinit var viewModel: ShutdownSpecifiedViewModel
    lateinit var shutdownScheduledCallback: (timeout: Int)->Unit
    lateinit var dismissCallback: ()->Unit

    companion object {
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
            viewModel = ViewModelProviders.of(fragmentActivity).get(ShutdownSpecifiedViewModel::class.java)
            viewModel.navigator = this
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
        val timeout = viewModel.calculateTimeoutInSeconds(
            hoursEt?.text.toString(),
            minutesEt?.text.toString(),
            secondsEt?.text.toString()
        )

        if (timeout == null) {
            toast(R.string.toast_invalid_time)
            return
        }

        sharedViewModel?.communicate(
            Message(Command.SCHEDULE_SHUTDOWN, timeout.toString()),
            onSuccess = {
                shutdownScheduledCallback.invoke(timeout)
            })
    }
}