package com.example.pcremote.ui.fragment.keyboard

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pcremote.R
import com.example.pcremote.data.constants.KeyboardKey.KEY_ALT
import com.example.pcremote.data.constants.KeyboardKey.KEY_BACKSPACE
import com.example.pcremote.data.constants.KeyboardKey.KEY_CTRL
import com.example.pcremote.data.constants.KeyboardKey.KEY_ESC
import com.example.pcremote.data.constants.KeyboardKey.KEY_SHIFT
import com.example.pcremote.data.constants.KeyboardKey.KEY_TAB
import com.example.pcremote.data.constants.KeyboardKey.KEY_WINDOWS
import com.example.pcremote.data.constants.MiscConstants
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ext.showKeyboard
import com.example.pcremote.ui.abstraction.BaseFragment
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.fragment_keyboard.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class KeyboardFragment: BaseFragment() {

    private var keysStateMap: MutableMap<String, Boolean> = mutableMapOf(
        KEY_SHIFT to false,
        KEY_CTRL to false,
        KEY_ALT to false
    )

    companion object {
        fun newInstance(): KeyboardFragment {
            return KeyboardFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {fragmentActivity ->
            setClickListeners()
            setKeyboardListener()
        }
    }

    private fun setClickListeners() {
        KeyboardVisibilityEvent.setEventListener(activity, keyboardVisibilityChangeListener)
        keyEscButton.setOnClickListener {
            sendMessageAndUpdateKeysMapOnSuccess(KEY_ESC)
        }
        keyTabButton.setOnClickListener {
            sendMessageAndUpdateKeysMapOnSuccess(KEY_TAB)
        }
        keyShiftButton.setOnClickListener {
            sendMessageAndUpdateKeysMapOnSuccess(KEY_SHIFT)
        }
        keyCtrlButton.setOnClickListener {
            sendMessageAndUpdateKeysMapOnSuccess(KEY_CTRL)
        }
        keyWindowsButton.setOnClickListener {
            sendMessageAndUpdateKeysMapOnSuccess(KEY_WINDOWS)
        }
        keyAltButton.setOnClickListener {
            sendMessageAndUpdateKeysMapOnSuccess(KEY_ALT)
        }
    }

    private fun sendMessageAndUpdateKeysMapOnSuccess(key: String) {
        sharedViewModel?.communicate(
            Message(Command.KEYBOARD_SPECIAL_KEY, listOf(key)),
            onSuccess = {
                val currentState = keysStateMap[key] ?: return@communicate Unit
                keysStateMap[key] = !currentState
                invalidateButtonsState()
            }
        )
    }

    private fun invalidateButtonsState() {
        keysStateMap.forEach { entry ->
            val affectedButton = when (entry.key) {
                KEY_SHIFT -> { keyShiftButton }
                KEY_CTRL -> { keyCtrlButton }
                KEY_ALT -> { keyAltButton }
                else -> { null }
            }
            if (entry.value) {
                affectedButton?.background = context?.getDrawable(R.drawable.shape_button_clicked)
            } else {
                affectedButton?.background = context?.getDrawable(R.drawable.shape_button)
            }
        }
    }

    private fun setKeyboardListener() {
        keyboardRootLayout.setOnKeyListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val key = event.unicodeChar.toChar()
                when (key) {
                    MiscConstants.UNICODE_BACKSPACE -> {
                        sharedViewModel?.communicate(
                            Message(Command.KEYBOARD_SPECIAL_KEY, listOf(KEY_BACKSPACE))
                        )
                    }
                    else -> {
                        sharedViewModel?.communicate(
                            Message(Command.KEYBOARD_REGULAR_KEY, listOf(key))
                        )
                    }
                }
            }
            false
        }
    }


    private val keyboardVisibilityChangeListener = KeyboardVisibilityEventListener { isVisible ->
        if (isVisible) {
            showOrHideKeyboardButton.apply {
                text = context.getText(R.string.hide_keyboard_button_text)
                setOnClickListener { hideKeyboard() }
            }
            keyboardRootLayout.requestFocus()
        } else {
            showOrHideKeyboardButton.apply {
                text = context.getText(R.string.show_keyboard_button_text)
                setOnClickListener {
                    showKeyboard(keyboardRootLayout)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchKeysState()
        showKeyboard(keyboardRootLayout)
    }

    private fun fetchKeysState() {
        sharedViewModel?.communicate(
            Message(Command.KEYBOARD_FETCH_KEY_STATE),
            onSuccess = { response ->
                val keysStateMap = deserializeKeysStateMapFromResponse(response)
                if (keysStateMap != null) {
                    this.keysStateMap = keysStateMap
                    invalidateButtonsState()
                }
            }
        )
    }

    private fun deserializeKeysStateMapFromResponse(response: List<String>) =
        response.firstOrNull()?.let { serializedMap ->
            sharedViewModel?.mapper?.readValue<MutableMap<String, Boolean>>(serializedMap)
        }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}