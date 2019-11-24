package com.example.pcremote.ext

import com.example.pcremote.constants.CommunicatorConstants
import com.example.pcremote.constants.NetworkConstants
import com.example.pcremote.exception.UnsuccessfulResponseException
import com.example.pcremote.singleton.Communicator
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