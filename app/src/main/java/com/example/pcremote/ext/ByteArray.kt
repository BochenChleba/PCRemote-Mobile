package com.example.pcremote.ext

import com.example.pcremote.Constants
import com.example.pcremote.singleton.Communicator
import java.nio.charset.Charset

fun ByteArray.isSuccessfulResponse() = this
    .copyOfRange(0, Communicator.FEEDBACK_SUCCEED.length)
    .toString(Charset.forName(Constants.COMMUNICATION_CHARSET)) ==
        Communicator.FEEDBACK_SUCCEED