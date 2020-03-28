package com.example.pcremote.ui.activity.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pcremote.data.constants.CommunicatorConstants
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.data.constants.NetworkConstants
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.data.enum.ConnectionStatus
import com.example.pcremote.ext.changeValueIfDifferent
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.activity.base.BaseViewModel
import com.example.pcremote.ui.fragment.connection_status.PingingThread
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class MainViewModel: BaseViewModel<MainNavigator>() {

    val connectionStatus = MutableLiveData<ConnectionStatus>()
    private var communicator: Communicator? = null
    private var communicationInProgress = false
    private val handler = android.os.Handler()
    private var pingingThread: PingingThread? = null

    fun initializeCommunicator() {
        compositeDisposable.add(
            Communicator.getInstanceAsync(prefs.getIpAddress())
                .observeInitialization()
        )
    }

    fun reinitializeCommunicator() {
        if (connectionStatus.value != ConnectionStatus.CONNECTING) {
            compositeDisposable.add(
                Communicator.reinstantiateAsync(prefs.getIpAddress())
                    .observeInitialization()
            )
        }
    }

    private fun Single<Communicator>.observeInitialization(): Disposable {
        return this.doOnSubscribe { connectionStatus.value = ConnectionStatus.CONNECTING }
            .subscribe({ communicatorInstance ->
                communicator = communicatorInstance
                connectionStatus.value = ConnectionStatus.CONNECTED
            }, { ex ->
                Log.e(NetworkConstants.LOG_TAG, ex.toString())
                connectionStatus.value = ConnectionStatus.DISCONNECTED
            })
    }

    fun communicate(message: Message, onSuccess: ((List<String>)->Any?)? = null,
                    onFailure: (()->Unit)? = null) {
        if (communicationInProgress) {
            if (message.command.awaitsForResponse) {
                handler.postDelayed({
                    communicate(message, onSuccess = onSuccess, onFailure = onFailure)
                }, MiscConstants.COMMUNICATION_RETRY_DELAY)
            }
            return
        }

        if (connectionStatus.value != ConnectionStatus.CONNECTED) {
            onFailure?.invoke()
            return
        }

        communicator?.sendCommand(message)?.let { commandObservable ->
            commandObservable
                .doOnSubscribe {
                    communicationInProgress = true
                }
                .doFinally {
                    communicationInProgress = false
                }
                .subscribe( { responseParams ->
                    onSuccess?.invoke(responseParams)
                }, { ex ->
                    handleCommunicationFailure(message.command, ex)
                    onFailure?.invoke()
                })
                .let { disposable ->
                    compositeDisposable.add(disposable)
                }
        } ?: reinitializeCommunicator()
    }

    private fun handleCommunicationFailure(command: Command, ex: Throwable) {
        Log.e(NetworkConstants.LOG_TAG, ex.toString())
        if (command == Command.PING) {
            return
        }
        connectionStatus.changeValueIfDifferent(ConnectionStatus.CONNECTING)

        communicate(
            Message(Command.PING),
            onSuccess = { response ->
                response.firstOrNull()?.let { msg ->
                    if (msg == CommunicatorConstants.FEEDBACK_PONG) {
                        connectionStatus.changeValueIfDifferent(ConnectionStatus.CONNECTED)
                    } else {
                        connectionStatus.changeValueIfDifferent(ConnectionStatus.DISCONNECTED)
                    }
                } ?: {
                    connectionStatus.changeValueIfDifferent(ConnectionStatus.DISCONNECTED)
                    reinitializeCommunicator()
                }
            },
            onFailure = {
                connectionStatus.changeValueIfDifferent(ConnectionStatus.DISCONNECTED)
                reinitializeCommunicator()
            }
        )
    }

    fun startPinging() {
        if (pingingThread == null && connectionStatus.value == ConnectionStatus.CONNECTED) {
            pingingThread = PingingThread(this).apply { run() }
        }
    }

    fun stopPinging() {
        pingingThread?.stopPinging()
        pingingThread = null
    }
}