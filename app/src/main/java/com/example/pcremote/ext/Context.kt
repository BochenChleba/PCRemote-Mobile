package com.example.pcremote.ext

import android.content.Context

fun Context.getScreenWidth() = resources.displayMetrics.widthPixels.toFloat()
fun Context.getScreenHeight() = resources.displayMetrics.heightPixels.toFloat()
