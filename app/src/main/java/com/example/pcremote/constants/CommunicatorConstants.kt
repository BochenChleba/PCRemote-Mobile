package com.example.pcremote.constants

object CommunicatorConstants {
    const val COMMAND_PING = "0"
    const val COMMAND_SHUTDOWN_NOW = "1"
    const val COMMAND_SCHEDULE_SHUTDOWN = "2"
    const val COMMAND_ABORT_SHUTDOWN = "3"
    const val COMMAND_RESTART = "4"
    const val COMMAND_SET_VOLUME = "5"
    const val COMMAND_GET_VOLUME = "6"
    const val COMMAND_MUTE = "7"
    const val COMMAND_UNMUTE = "8"
    const val COMMAND_MOUSE_MOVE = "9"
    const val COMMAND_MOUSE_LEFT = "10"
    const val COMMAND_MOUSE_RIGHT = "11"

    const val FEEDBACK_AWAITING_PARAMS = "ready"
    const val FEEDBACK_SUCCEED = "ok"
    const val FEEDBACK_PONG = "pong"

    const val RESPONSE_VOLUME_INDEX = 0
    const val RESPONSE_MUTED_INDEX = 1
}