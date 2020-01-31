package com.example.pcremote.ui.dialog.restart

import com.example.pcremote.R
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ui.dialog.base.SimpleDialog

class RestartDialog : SimpleDialog() {

    companion object {
        const val TAG = "restart dialog"
        fun newInstance(): RestartDialog {
            return RestartDialog()
        }
    }

    override val confirmCallback = {
        sharedViewModel?.communicate(Message(Command.RESTART))
    }
    override val titleStringRes = R.string.restart_dialog_title
    override val messageStringRes = R.string.restart_dialog_message
}
