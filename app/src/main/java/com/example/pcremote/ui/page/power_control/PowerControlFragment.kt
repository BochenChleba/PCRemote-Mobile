package com.example.pcremote.ui.page.power_control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.MainViewModel
import com.example.pcremote.ui.dialog.schedlued_shutdown.ScheduledShutdownDialog
import com.example.pcremote.ui.dialog.shutdown_now.ShutdownNowDialog
import kotlinx.android.synthetic.main.fragment_power_control.*

class PowerControlFragment: Fragment() {

    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance(): PowerControlFragment {
            return PowerControlFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_power_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {fragmentActivity ->
            viewModel = ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)

            shutdownNowBtn?.setOnClickListener {
                ShutdownNowDialog.newInstance()
                    .show(fragmentActivity.supportFragmentManager, ShutdownNowDialog.TAG)
            }

            scheduledShutdownBtn?.setOnClickListener {
                ScheduledShutdownDialog.newInstance()
                    .show(fragmentActivity.supportFragmentManager, ScheduledShutdownDialog.TAG)
            }
        }
    }
}