package com.example.pcremote.ext

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.onActionDone(callback: ()->Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE){
            callback.invoke()
            true
        } else {
            false
        }
    }
}