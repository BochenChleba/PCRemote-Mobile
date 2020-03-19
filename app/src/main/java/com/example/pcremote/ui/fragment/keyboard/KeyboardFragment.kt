package com.example.pcremote.ui.fragment.keyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.data.constants.KeyboardKey.KEY_ALT
import com.example.pcremote.data.constants.KeyboardKey.KEY_CTRL
import com.example.pcremote.data.constants.KeyboardKey.KEY_ESC
import com.example.pcremote.data.constants.KeyboardKey.KEY_SHIFT
import com.example.pcremote.data.constants.KeyboardKey.KEY_TAB
import com.example.pcremote.data.constants.KeyboardKey.KEY_WINDOWS
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ext.hideKeyboard
import com.example.pcremote.ext.showKeyboard
import com.example.pcremote.singleton.Preferences
import com.example.pcremote.ui.fragment.base.BaseFragment
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.fragment_keyboard.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class KeyboardFragment: BaseFragment(), KeyboardNavigator {

    private lateinit var viewModel: KeyboardViewModel
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
            initializeViewModel(fragmentActivity)
            setClickListeners()
        }
    }

    private fun initializeViewModel(activity: FragmentActivity) {
        viewModel = ViewModelProviders.of(activity).get(KeyboardViewModel::class.java)
        viewModel.navigator = this
        viewModel.prefs = sharedViewModel?.prefs ?: Preferences.getInstance(activity)
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

    private val keyboardVisibilityChangeListener = KeyboardVisibilityEventListener { isVisible ->
        if (isVisible) {
            showOrHideKeyboardButton.apply {
                text = context.getText(R.string.hide_keyboard_button_text)
                setOnClickListener { hideKeyboard() }
            }
        } else {
            showOrHideKeyboardButton.apply {
                text = context.getText(R.string.show_keyboard_button_text)
                setOnClickListener {
                    showKeyboard(hiddenEditText)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchKeysState()
        showKeyboard(hiddenEditText)
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