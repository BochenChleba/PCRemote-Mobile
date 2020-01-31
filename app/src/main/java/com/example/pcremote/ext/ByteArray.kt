package com.example.pcremote.ext

import com.example.pcremote.data.constants.CommunicatorConstants
import com.example.pcremote.data.constants.NetworkConstants
import com.example.pcremote.data.exception.UnsuccessfulResponseException
import java.nio.charset.Charset

fun ByteArray.readResponse(): List<String> {
    val splittedResponse = this
        .toString(Charset.forName(NetworkConstants.COMMUNICATION_CHARSET))
        .trim('\u0000')
        .split('&')
    val isSuccessful = splittedResponse.firstOrNull()
    val params = splittedResponse.drop(1)
    if (isSuccessful == null || isSuccessful != CommunicatorConstants.FEEDBACK_SUCCEED) {
        throw UnsuccessfulResponseException(params)
    }
    return params
} 

fun ByteArray.isAwaitingParamsResponse() = this
    .copyOfRange(0, CommunicatorConstants.FEEDBACK_AWAITING_PARAMS.length)
    .toString(Charset.forName(NetworkConstants.COMMUNICATION_CHARSET)) ==
        CommunicatorConstants.FEEDBACK_AWAITING_PARAMS