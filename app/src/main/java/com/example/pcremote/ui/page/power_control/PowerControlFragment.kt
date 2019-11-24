package com.example.pcremote.ui.page.power_control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.constants.CommunicatorConstants
import com.example.pcremote.ext.gone
import com.example.pcremote.ext.show
import com.example.pcremote.ui.base.BaseFragment
import com.example.pcremote.ui.dialog.restart.RestartDialog
import com.example.pcremote.ui.dialog.schedlued_shutdown.ScheduledShutdownDialog
import com.example.pcremote.ui.dialog.shutdown_now.ShutdownNowDialog
import com.example.pcremote.util.Preferences
import kotlinx.android.synthetic.main.fragment_power_control.*
import org.jetbrains.anko.support.v4.toast

class PowerControlFragment: BaseFragment(), PowerControlNavigator {

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
            viewModel = ViewModelProviders.of(fragmentActivity).get(PowerControlViewModel::class.java)
            viewModel.navigator = this
            viewModel.prefs = sharedViewModel?.prefs ?: Preferences.getInstance(fragmentActivity)

            ScheduledShutdownDialog.shutdownScheduledCallback = {
                viewModel.checkScheduledShutdown()
            }
            setClickListeners(fragmentActivity)
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

    private fun setClickListeners(activity: FragmentActivity) {
        shutdownNowBtn?.setOnClickListener {
            checkConnectionStatus() ?: return@setOnClickListener
            ShutdownNowDialog.newInstance()
                .show(activity.supportFragmentManager, ShutdownNowDialog.TAG)
        }

        scheduledShutdownBtn?.setOnClickListener {
            checkConnectionStatus() ?: return@setOnClickListener
            ScheduledShutdownDialog.newInstance()
                .show(activity.supportFragmentManager, ScheduledShutdownDialog.TAG)
        }

        abortShutdownTv?.setOnClickListener {
            checkConnectionStatus() ?: return@setOnClickListener
            sharedViewModel?.communicate(
                command = CommunicatorConstants.COMMAND_ABORT_SHUTDOWN,
                onSuccess = {
                    viewModel.abortShutdown()
                    toast("Shutdown aborted")
                }
            )
        }

        restartBtn?.setOnClickListener {
            checkConnectionStatus() ?: return@setOnClickListener
            RestartDialog.newInstance()
                .show(activity.supportFragmentManager, RestartDialog.TAG)
        }
    }
}