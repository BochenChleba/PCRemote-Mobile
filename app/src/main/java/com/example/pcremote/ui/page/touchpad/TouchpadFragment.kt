package com.example.pcremote.ui.page.touchpad

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ui.base.BaseFragment
import com.example.pcremote.util.Preferences
import com.example.pcremote.view.ITouchpadView
import com.example.pcremote.view.Offset
import kotlinx.android.synthetic.main.fragment_touchpad.*

class TouchpadFragment: BaseFragment(), TouchpadNavigator, ITouchpadView {
    private lateinit var viewModel: TouchpadViewModel

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
            setClickListeners(fragmentActivity)
        }
    }

    private fun setClickListeners(activity: FragmentActivity) {
        touchpadView.listener = this
    }

    override fun onTouchpadMove(offset: Offset) {
        Log.d("kurwamac", "x: ${offset.xOffset}, y: ${offset.yOffset}")
    }

    override fun onTouchpadClick() {
        Log.d("kurwamac", "click")
    }

    override fun onTouchpadLongClick() {
        Log.d("kurwamac", "long click")
    }
}
