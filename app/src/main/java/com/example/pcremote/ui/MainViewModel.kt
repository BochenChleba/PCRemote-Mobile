package com.example.pcremote.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pcremote.Constants
import com.example.pcremote.singleton.Communicator
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class MainViewModel: ViewModel() {
    lateinit var navigator: MainNavigator

    var communicator: Communicator? = null
    private val compositeDisposable = CompositeDisposable()

    fun dispose() = compositeDisposable.dispose()

    fun initializeCommunicator() {
        compositeDisposable.add(
            Communicator
                .getInstanceAsync()
                .subscribe({ communicatorInstance ->
                    communicator = communicatorInstance
                    navigator.showToast("Connected with PC")
                }, {
                    navigator.showToast("Cannot connect to PC")
                })
        )
    }

    fun communicate(command: String) {
        val disposable = callCommand(command)
            ?.subscribe( {
                2+2
            }, {ex ->
                Log.e(Constants.LOG_TAG, ex.toString())
            })
        if (disposable != null) {
            compositeDisposable.add(disposable)
        }
    }

    private fun callCommand(command: String): Single<Unit>? {
        return when (command) {
            Communicator.COMMAND_SHUTDOWN_NOW -> communicator?.shutdownNow()
            else -> null
        }
    }

}