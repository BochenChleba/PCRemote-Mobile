package com.example.pcremote.ui.page.volume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.constants.CommunicatorConstants
import com.example.pcremote.enum.ConnectionStatus
import com.example.pcremote.ext.hide
import com.example.pcremote.ext.show
import com.example.pcremote.ui.base.BaseFragment
import com.example.pcremote.util.Preferences
import kotlinx.android.synthetic.main.fragment_volume_control.*

class VolumeControlFragment: BaseFragment(), VolumeControlNavigator {
    private lateinit var viewModel: VolumeControlViewModel

    companion object {
        const val VOLUME_PROGRESS_MULTIPLIER = 2
        fun newInstance(): VolumeControlFragment {
            return VolumeControlFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_volume_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {fragmentActivity ->
            viewModel = ViewModelProviders.of(fragmentActivity).get(VolumeControlViewModel::class.java)
            viewModel.navigator = this
            viewModel.prefs = sharedViewModel?.prefs ?: Preferences.getInstance(fragmentActivity)
            setClickListeners(fragmentActivity)
        }
    }

    private fun setClickListeners(activity: FragmentActivity) {
        volumeSb?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                checkConnectionStatus() ?: return
                val progress = seekBar?.progress ?: 50
                sharedViewModel?.communicate(
                    CommunicatorConstants.COMMAND_SET_VOLUME,
                    progress * VOLUME_PROGRESS_MULTIPLIER
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getCurrentVolume()
        observeConnectionStatus()
    }

    private fun getCurrentVolume() {
        sharedViewModel?.communicate(
            CommunicatorConstants.COMMAND_GET_VOLUME,
            onSuccess = { responseParams ->
                readGetVolumeResponse(responseParams)
                enableVolumeSeekBar()
            },
            onFailure = {
                enableVolumeSeekBar()
            }
        )
    }

    private fun readGetVolumeResponse(responseParams: List<String>) {
        val currentVolume = responseParams.getOrNull(CommunicatorConstants.RESPONSE_VOLUME_INDEX)?.toInt()
        if (currentVolume != null) {
            volumeSb.progress = currentVolume / VOLUME_PROGRESS_MULTIPLIER
        }
        val isMuted = responseParams.getOrNull(CommunicatorConstants.RESPONSE_MUTED_INDEX)?.toBoolean()
        if (isMuted != null) {
            setMuteButton(isMuted)
        }
    }

    private fun setMuteButton(isMuted: Boolean) {
        if (isMuted) {
            muteBtn.text = getString(R.string.volume_control_unmute_btn)
            muteBtn.setOnClickListener{
                sendUnmuteCommand()
            }
        } else {
            muteBtn.text = getString(R.string.volume_control_mute_btn)
            muteBtn.setOnClickListener{
                sendMuteCommand()
            }
        }
    }

    private fun sendMuteCommand() {
        sharedViewModel?.communicate(
            CommunicatorConstants.COMMAND_MUTE,
            onSuccess = {
                muteBtn.text = getString(R.string.volume_control_unmute_btn)
                muteBtn.setOnClickListener{
                    sendUnmuteCommand()
                }
            }
        )
    }

    private fun sendUnmuteCommand() {
        sharedViewModel?.communicate(
            CommunicatorConstants.COMMAND_UNMUTE,
            onSuccess = {
                muteBtn.text = getString(R.string.volume_control_mute_btn)
                muteBtn.setOnClickListener{
                    sendMuteCommand()
                }
            }
        )
    }

    private fun enableVolumeSeekBar() {
        volumeSb.show()
        muteBtn.show()
        progressBar.hide()
    }

    private fun observeConnectionStatus() {
        sharedViewModel?.connectionStatus?.observe(this, Observer { connectionStatus ->
            if (connectionStatus == ConnectionStatus.CONNECTED) {
                enableVolumeSeekBar()
                getCurrentVolume()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        disableVolumeSeekBar()
        stopObservingConnectionStatus()
    }

    private fun disableVolumeSeekBar() {
        volumeSb.hide()
        muteBtn.hide()
        progressBar.show()
    }

    private fun stopObservingConnectionStatus() {
        sharedViewModel?.connectionStatus?.removeObservers(this)
    }
}
