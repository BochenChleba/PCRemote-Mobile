package com.example.pcremote.ui.abstraction

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.pcremote.data.exception.NavigatorNotInitializedException
import com.example.pcremote.persistance.Preferences
import io.reactivex.disposables.CompositeDisposable
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.lang.ref.WeakReference

abstract class BaseViewModel<T: BaseNavigator>: ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val mapper = jacksonObjectMapper()
    lateinit var prefs: Preferences
    private lateinit var navigator: WeakReference<T>

    protected fun getNavigator() =
        navigator.get() ?: throw NavigatorNotInitializedException()

    fun setNavigator(nav: T) {
        this.navigator = WeakReference(nav)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun initializePreferencesInstance(context: Context) {
        prefs = Preferences.getInstance(context)
    }

}
