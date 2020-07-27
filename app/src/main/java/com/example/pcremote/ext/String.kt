package com.example.pcremote.ext

fun String.toIntOrZero(): Int =
    this.toIntOrNull() ?: 0
