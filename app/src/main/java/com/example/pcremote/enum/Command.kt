package com.example.pcremote.enum

enum class Command(val stringVal: String, val awaitsForResponse: Boolean) {
    PING("0", true),
    SHUTDOWN_NOW("1", true),
    SCHEDULE_SHUTDOWN("2", true),
    ABORT_SHUTDOWN("3", true),
    RESTART("4", true),
    SET_VOLUME("5", true),
    GET_VOLUME("6", true),
    MUTE("7", true),
    UNMUTE("8", true),
    MOUSE_MOVE("9", false),
    MOUSE_LEFT("10", true),
    MOUSE_RIGHT("11", true),
}