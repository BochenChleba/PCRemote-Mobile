package com.example.pcremote.ui.dialog.schedlued_shutdown.countdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.constants.MiscConstants
import com.example.pcremote.R
import com.example.pcremote.constants.CommunicatorConstants
import com.example.pcremote.dto.Message
import com.example.pcremote.enum.Command
import com.example.pcremote.ext.onActionDone
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_shutdown_countdown.*

class ShutdownCountdownFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var privateViewModel: ShutdownCountdownViewModel
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
            viewModel = ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)
            privateViewModel = ViewModelProviders.of(fragmentActivity).get(ShutdownCountdownViewModel::class.java)
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
        val timeout = privateViewModel.calculateTimeoutInSeconds(
            hoursEt.text.toString(),
            minutesEt.text.toString(),
            secondsEt.text.toString()
        )
        val startTime = System.currentTimeMillis()
        viewModel.communicate(
            Message(Command.SCHEDULE_SHUTDOWN, timeout.toString()),
            onSuccess = {
                val actualTimeout = timeout - ((System.currentTimeMillis() - startTime) / 1000)
                shutdownScheduledCallback.invoke(actualTimeout.toInt())
            }) {
        }
    }
}
