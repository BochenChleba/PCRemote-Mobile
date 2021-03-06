package com.example.pcremote.ui.fragment.touchpad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.pcremote.R
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ui.abstraction.BaseFragment
import com.example.pcremote.view.touchpad.ITouchpadView
import com.example.pcremote.data.dto.Offset
import com.example.pcremote.data.enum.ConnectionStatus
import kotlinx.android.synthetic.main.fragment_touchpad.*

class TouchpadFragment: BaseFragment() {

    companion object {
        fun newInstance(): TouchpadFragment {
            return TouchpadFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_touchpad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {fragmentActivity ->
            setListeners(fragmentActivity)
            observeConnectionStatus()
        }
    }

    private fun setListeners(activity: FragmentActivity) {
        touchpadView.listener = touchpadListener
    }

    private fun observeConnectionStatus() {
        sharedViewModel?.connectionStatus?.observe(this, Observer { connectionStatus ->
            touchpadView.isDisabled = connectionStatus != ConnectionStatus.CONNECTED
        })
    }

    private val touchpadListener = object : ITouchpadView {
        override fun onTouchpadMove(offset: Offset) {
            sharedViewModel?.communicate(
                Message(
                    Command.MOUSE_MOVE,
                    listOf(offset.xOffset, offset.yOffset)
                )
            )
        }
        override fun onTouchpadClick() {
            sharedViewModel?.communicate(
                Message(Command.MOUSE_LEFT_CLICK)
            )
        }
        override fun onTouchpadLongClick() {
            sharedViewModel?.communicate(
                Message(Command.MOUSE_RIGHT_CLICK)
            )
        }
    }


}
