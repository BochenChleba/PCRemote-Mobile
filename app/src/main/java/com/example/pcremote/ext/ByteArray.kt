package com.example.pcremote.ext

import com.example.pcremote.NetworkConstants
import com.example.pcremote.singleton.Communicator
import java.nio.charset.Charset

fun ByteArray.isSuccessfulResponse() = this
    .copyOfRange(0, Communicator.FEEDBACK_SUCCEED.length)
    .toString(Charset.forName(NetworkConstants.COMMUNICATION_CHARSET)) ==
        Communicator.FEEDBACK_SUCCEED

fun ByteArray.isAwaitingParamsResponse() = this
    .copyOfRange(0, Communicator.FEEDBACK_AWAITING_PARAMS.length)
    .toString(Charset.forName(NetworkConstants.COMMUNICATION_CHARSET)) ==
        Communicator.FEEDBACK_AWAITING_PARAMS