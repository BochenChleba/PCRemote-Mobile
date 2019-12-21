package com.example.pcremote.ext

import com.example.pcremote.constants.MiscConstants

fun String.toIntOrZero(): Int = this.toIntOrNull() ?: 0

fun Array<Any>.serialize(): String {
    val output = StringBuilder()
    this.forEachIndexed { index, element ->
        output.append(element)
        if (index < this.size - 1) {
            output.append(MiscConstants.PARAMS_SEPARATOR)
        }
    }
    return output.toString()
}
