package com.example.pcremote.ui.activity.base

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcremote.data.constants.CommunicatorConstants
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.data.constants.NetworkConstants
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.data.enum.ConnectionStatus
import com.example.pcremote.ext.changeValueIfDifferent
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.singleton.Preferences
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class BaseViewModel<T: BaseNavigator>: ViewModel() {
    lateinit var navigator: T
    val compositeDisposable = CompositeDisposable()
    val mapper = jacksonObjectMapper()
    lateinit var prefs: Preferences

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun initializePreferencesInstance(context: Context) {
        prefs = Preferences.getInstance(context)
    }

}