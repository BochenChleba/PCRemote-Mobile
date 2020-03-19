package com.example.pcremote.data.enum

enum class Command(val stringVal: String, val awaitsForResponse: Boolean = true) {
    PING("0"),
    SHUTDOWN_NOW("1"),
    SCHEDULE_SHUTDOWN("2"),
    ABORT_SHUTDOWN("3"),
    RESTART("4"),
    SET_VOLUME("5"),
    GET_VOLUME("6"),
    MUTE("7"),
    UNMUTE("8"),
    MOUSE_MOVE("9", false),
    MOUSE_LEFT_CLICK("10"),
    MOUSE_RIGHT_CLICK("11"),
    KEYBOARD_SPECIAL_KEY("12"),
    KEYBOARD_REGULAR_KEY("13"),
    KEYBOARD_FETCH_KEY_STATE("14")
}