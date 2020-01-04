package com.example.pcremote.ui.dialog.shutdown_now

import com.example.pcremote.R
import com.example.pcremote.constants.CommunicatorConstants
import com.example.pcremote.dto.Message
import com.example.pcremote.enum.Command
import com.example.pcremote.ui.base.SimpleDialog

class ShutdownNowDialog : SimpleDialog() {

    companion object {
        const val TAG = "shutdown now"

        fun newInstance(): ShutdownNowDialog {
            return ShutdownNowDialog()
        }
    }

    override val confirmCallback = {
        sharedViewModel?.communicate(Message(Command.SHUTDOWN_NOW))
    }

    override val titleStringRes = R.string.shutdown_now_dialog_title
    override val messageStringRes = R.string.shutdown_now_dialog_message
}
