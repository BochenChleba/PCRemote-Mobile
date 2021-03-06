package com.example.pcremote.ui.dialog.schedlued_shutdown.countdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.R
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ext.onActionDone
import com.example.pcremote.ui.abstraction.BaseFragment
import kotlinx.android.synthetic.main.fragment_shutdown_countdown.*

class ShutdownCountdownFragment: BaseFragment(), ShutdownCountdownNavigator {

    private lateinit var viewModel: ShutdownCountdownViewModel
    lateinit var shutdownScheduledCallback: (timeout: Int)->Unit
    lateinit var dismissCallback: ()->Unit

    companion object {
        fun newInstance(): ShutdownCountdownFragment {
            return ShutdownCountdownFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shutdown_countdown, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {fragmentActivity ->
            viewModel = ViewModelProviders.of(fragmentActivity).get(ShutdownCountdownViewModel::class.java)
            viewModel.setNavigator(this)
            setClickListeners()
            setTextWatchers()
        }
    }

    override fun onResume() {
        super.onResume()
        hoursEt?.requestFocus()
    }

    private fun setClickListeners(){
        declineTv.setOnClickListener { dismissCallback.invoke() }
        confirmTv.setOnClickListener { performShutdown() }
        secondsEt.onActionDone { performShutdown() }
    }

    private fun setTextWatchers() {
        hoursEt?.addTextChangedListener {
            val text = it.toString()
            if (text.length == MiscConstants.TIME_INPUT_LENGTH) {
                minutesEt?.requestFocus()
            }
        }
        minutesEt?.addTextChangedListener {
            val text = it.toString()
            if (text.length == MiscConstants.TIME_INPUT_LENGTH) {
                secondsEt?.requestFocus()
            }
        }
    }

    private fun performShutdown() {
        val timeout = viewModel.calculateTimeoutInSeconds(
            hoursEt.text.toString(),
            minutesEt.text.toString(),
            secondsEt.text.toString()
        )
        val startTime = System.currentTimeMillis()
        sharedViewModel?.communicate(
            Message(Command.SCHEDULE_SHUTDOWN, timeout.toString()),
            onSuccess = {
                val actualTimeout = timeout - ((System.currentTimeMillis() - startTime) / 1000)
                shutdownScheduledCallback.invoke(actualTimeout.toInt())
            }) {
        }
    }
}
