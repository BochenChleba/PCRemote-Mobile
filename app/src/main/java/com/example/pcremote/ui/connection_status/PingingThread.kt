package com.example.pcremote.ui.connection_status

import android.os.Handler
import android.util.Log
import com.example.pcremote.constants.MiscConstants
import com.example.pcremote.enum.ConnectionStatus
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.MainViewModel

class PingingThread(private val viewModel: MainViewModel): Thread() {

    private val handler = Handler()
    private var runnable: Runnable? = null

    companion object {
        private const val PING_INTERVAL_MS = 15000L
    }

    fun stopPinging() {
        runnable?.let { r ->
            handler.removeCallbacks(r)
            interrupt()
        }
    }

    override fun run() {
        runnable = Runnable {
            viewModel.communicate(
                Communicator.COMMAND_PING,
                onSuccess =  { changeConnectionStatus(ConnectionStatus.CONNECTED) },
                onFailure = { changeConnectionStatus(ConnectionStatus.DISCONNECTED) }
            )
            handler.postDelayed(this, PING_INTERVAL_MS)
        }.apply { run() }
    }

    private fun changeConnectionStatus(newStatus: ConnectionStatus) {
        viewModel.connectionStatus.value?.let { prevStatus ->
            if (newStatus != prevStatus) {
                viewModel.connectionStatus.postValue(newStatus)
            }
        }
    }
}