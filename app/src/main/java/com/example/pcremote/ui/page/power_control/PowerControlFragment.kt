package com.example.pcremote.ui.page.power_control

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ext.gone
import com.example.pcremote.ext.hide
import com.example.pcremote.ext.show
import com.example.pcremote.singleton.Communicator
import com.example.pcremote.ui.MainViewModel
import com.example.pcremote.ui.dialog.schedlued_shutdown.ScheduledShutdownDialog
import com.example.pcremote.ui.dialog.shutdown_now.ShutdownNowDialog
import kotlinx.android.synthetic.main.fragment_power_control.*
import org.jetbrains.anko.support.v4.toast

class PowerControlFragment: Fragment(), PowerControlNavigator {

    private lateinit var sharedViewModel: MainViewModel
    private lateinit var viewModel: PowerControlViewModel

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
            sharedViewModel = ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)
            viewModel = ViewModelProviders.of(fragmentActivity).get(PowerControlViewModel::class.java)
            viewModel.navigator = this
            viewModel.prefs = sharedViewModel.prefs

            ScheduledShutdownDialog.shutdownScheduledCallback = { viewModel.checkScheduledShutdown() }

            shutdownNowBtn?.setOnClickListener {
                ShutdownNowDialog.newInstance()
                    .show(fragmentActivity.supportFragmentManager, ShutdownNowDialog.TAG)
            }

            scheduledShutdownBtn?.setOnClickListener {
                ScheduledShutdownDialog.newInstance()
                    .show(fragmentActivity.supportFragmentManager, ScheduledShutdownDialog.TAG)
            }

            abortShutdownTv.setOnClickListener {
                sharedViewModel.communicate(Communicator.COMMAND_ABORT_SHUTDOWN) {
                    viewModel.abortShutdown()
                    toast("Shutdown aborted")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkScheduledShutdown()
    }

    override fun displayCountdown(remainedTime: String) {
        shutdownScheduledCountdownTv?.text =
            getString(R.string.shutdown_countdown_message, remainedTime)
    }

    override fun hideCountdown() {
        shutdownScheduledLayout.gone()
    }

    override fun showCountdown() {
        shutdownScheduledLayout.show()
    }

    override fun toastOnShutdown() {
        toast(R.string.toast_shutdown_complete)
    }
}