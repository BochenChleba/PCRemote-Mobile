package com.example.pcremote.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcremote.constants.NetworkConstants
import com.example.pcremote.R
import com.example.pcremote.enum.ConnectionStatus
import com.example.pcremote.exception.UnsuccessfulResponseException
import com.example.pcremote.ext.changeValueIfDifferent
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.util.Preferences
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MainViewModel: ViewModel() {
    lateinit var navigator: MainNavigator

    val connectionStatus = MutableLiveData<ConnectionStatus>()
    lateinit var prefs: Preferences

    private var communicator: Communicator? = null
    private val compositeDisposable = CompositeDisposable()

    fun dispose() = compositeDisposable.dispose()

    fun initializeCommunicator() {
        compositeDisposable.add(
            Communicator.getInstanceAsync()
                .observeInitialization()
        )
    }

    fun reinitializeCommunicator() {
        if (connectionStatus.value != ConnectionStatus.CONNECTING) {
            compositeDisposable.add(
                Communicator.reinstantiateAsync()
                    .observeInitialization()
            )
        }
    }

    fun initializePreferencesInstance(context: Context) {
        prefs = Preferences(context)
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

    fun communicate(command: String, params: String? = null, onSuccess: ((List<String>)->Any?)? = null,
                    onFailure: (()->Unit)? = null) {
        callCommand(command, params)
            ?.subscribe( { responseParams ->
                onSuccess?.invoke(responseParams)
            }, { ex ->
                handleCommunicationFailure(ex)
                onFailure?.invoke()
            })
            ?.let { disposable ->  compositeDisposable.add(disposable) }
    }

    private fun callCommand(command: String, params: String? = null): Single<List<String>>? {
        val comm = communicator ?: return null
        return when (command) {
            Communicator.COMMAND_PING -> comm.ping()
            Communicator.COMMAND_SHUTDOWN_NOW -> throw Exception()//comm.shutdownNow()
            Communicator.COMMAND_SCHEDULE_SHUTDOWN -> comm.scheduleShutdown(params ?: "")
            Communicator.COMMAND_ABORT_SHUTDOWN -> comm.abortShutdown()
            else -> Single.just(emptyList())
        }
    }

    private fun handleCommunicationFailure(ex: Throwable) {
        Log.e(NetworkConstants.LOG_TAG, ex.toString())
        navigator.showToast(R.string.toast_cannot_complete_command)
        connectionStatus.changeValueIfDifferent(ConnectionStatus.CONNECTING)

        communicate(
            Communicator.COMMAND_PING,
            onSuccess = { response ->
                response.firstOrNull()?.let { msg ->
                    if (msg == Communicator.FEEDBACK_PONG) {
                        connectionStatus.changeValueIfDifferent(ConnectionStatus.CONNECTED)
                    } else {
                        connectionStatus.changeValueIfDifferent(ConnectionStatus.DISCONNECTED)
                    }
                } ?: {
                    connectionStatus.changeValueIfDifferent(ConnectionStatus.DISCONNECTED)
                }
            }
        )
    }

}