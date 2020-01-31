package com.example.pcremote.ui.fragment.touchpad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.ui.fragment.base.BaseFragment
import com.example.pcremote.singleton.Preferences
import com.example.pcremote.view.touchpad.ITouchpadView
import com.example.pcremote.data.Offset
import com.example.pcremote.ext.*
import com.example.pcremote.ui.fragment.connection_status.ConnectionStatusFragment
import com.example.pcremote.view.scroll_view.ISwipeableViewPager
import kotlinx.android.synthetic.main.fragment_touchpad.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class TouchpadFragment: BaseFragment(), TouchpadNavigator {
    private lateinit var viewModel: TouchpadViewModel
    var fragmentSwipeListener: ISwipeableViewPager? = null

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
            viewModel = ViewModelProviders.of(fragmentActivity).get(TouchpadViewModel::class.java)
            viewModel.navigator = this
            viewModel.prefs = sharedViewModel?.prefs ?: Preferences.getInstance(fragmentActivity)
            setListeners(fragmentActivity)
        }
    }

    private fun setListeners(activity: FragmentActivity) {
        touchpadView.listener = touchpadListener
       // touchpadScrollView.swipeListener = fragmentSwipeListener
        keyboardImageView.setOnClickListener(keyboardImageClickListener)
        KeyboardVisibilityEvent.setEventListener(activity, keyboardVisibilityChangeListener)
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
        override fun onTouchEventIntercepted(isEventFinished: Boolean) {
          //  touchpadScrollView.eventIntercepted = !isEventFinished
        }
    }

    private val keyboardVisibilityChangeListener: KeyboardVisibilityEventListener
            = KeyboardVisibilityEventListener { isVisible: Boolean ->
        if (isVisible) {
        //    touchpadScrollView.scrollToBottom()
            collapseConnectionStatusFragment()
            keyboardImageView.setOnClickListener(keyboardImageClickListener2)
        } else {
            keyboardImageView.setOnClickListener(keyboardImageClickListener)
        }
    }

    private val keyboardImageClickListener: View.OnClickListener = View.OnClickListener { view ->
        context?.showKeyboard()
    }

    private val keyboardImageClickListener2: View.OnClickListener = View.OnClickListener { view ->
        context?.hideKeyboard(touchPadRootLayout)
    }

    private fun collapseConnectionStatusFragment() {
        val connectionStatusFragment =
            fragmentManager?.findFragmentById(R.id.connectionStatusFragment)
                    as? ConnectionStatusFragment
        connectionStatusFragment?.collapse()
    }
}
