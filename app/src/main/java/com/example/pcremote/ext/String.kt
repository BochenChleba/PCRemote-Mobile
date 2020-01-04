package com.example.pcremote.ext

import com.example.pcremote.constants.MiscConstants

fun String.toIntOrZero(): Int = this.toIntOrNull() ?: 0
