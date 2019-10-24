package com.example.pcremote.ext

import com.example.pcremote.constants.MiscConstants

fun String.toIntOrZero(): Int = this.toIntOrNull() ?: 0

fun List<String>.serialize(): String {
    val output = StringBuilder()
    this.forEachIndexed { index, string ->
        output.append(string)
        if (index < this.size - 1) {
            output.append(MiscConstants.PARAMS_SEPARATOR)
        }
    }
    return output.toString()
}
