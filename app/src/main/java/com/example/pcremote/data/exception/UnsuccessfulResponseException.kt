package com.example.pcremote.data.exception

import java.lang.Exception

class UnsuccessfulResponseException(val params: List<String> = emptyList())
    : Exception("Could not get successful response") {
}