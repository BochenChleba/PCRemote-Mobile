package com.example.pcremote.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcremote.NetworkConstants
import com.example.pcremote.R
import com.example.pcremote.enum.ConnectionStatus
import com.example.pcremote.singleton.Communicator
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainViewModel: ViewModel() {
    lateinit var navigator: MainNavigator

    val connectionStatus = MutableLiveData<ConnectionStatus>()

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

    fun communicate(command: String, params: String? = null, onCommandSent: (()->Any)? = null) {
        if (connectionStatus.value != ConnectionStatus.CONNECTED) {
            navigator.showToast(R.string.toast_no_connection)
            return
        }

        val disposable = callCommand(command, params)
            ?.subscribe( {
                2+2
            }, {ex ->
                Log.e(NetworkConstants.LOG_TAG, ex.toString())
            })
        if (disposable != null) {
            compositeDisposable.add(disposable)
        }
        onCommandSent?.invoke()
    }

    private fun callCommand(command: String, params: String?): Single<Unit>? {
        return when (command) {
            Communicator.COMMAND_SHUTDOWN_NOW -> communicator?.shutdownNow()
            Communicator.COMMAND_SCHEDULE_SHUTDOWN -> communicator?.scheduleShutdown(params ?: "")
            else -> null
        }
    }

}