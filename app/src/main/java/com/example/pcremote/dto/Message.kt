package com.example.pcremote.dto

import com.example.pcremote.constants.MiscConstants
import com.example.pcremote.enum.Command

data class Message(
    val command: Command,
    val params: List<Any> = emptyList()
) {

    constructor(command: Command, param: Any) : this(command, listOf(param))

    fun toByteArray(): ByteArray {
        val output = StringBuilder()
        output.append(command.stringVal)
        output.append(MiscConstants.MESSAGE_SEPARATOR)
        params.forEachIndexed { index, param ->
            output.append(param)
            if (index < params.lastIndex) {
                output.append(MiscConstants.MESSAGE_SEPARATOR)
            }
        }
        output.removeRange(output.lastIndex, output.length)
        return output.toString().toByteArray()
    }
}