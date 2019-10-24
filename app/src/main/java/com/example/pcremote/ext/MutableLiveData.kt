package com.example.pcremote.ext

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import android.os.Looper



inline fun <T:Any> MutableLiveData<T>.changeValueIfDifferent(newValue: T) {
    val prevValue = this.value
    if (newValue != prevValue) {
        Handler(Looper.getMainLooper()).post {
            this.value = newValue
        }
    }
}