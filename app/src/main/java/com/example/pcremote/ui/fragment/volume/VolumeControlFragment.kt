package com.example.pcremote.ui.fragment.volume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.pcremote.R
import com.example.pcremote.data.constants.CommunicatorConstants
import com.example.pcremote.data.dto.Message
import com.example.pcremote.data.enum.Command
import com.example.pcremote.data.enum.ConnectionStatus
import com.example.pcremote.ext.hide
import com.example.pcremote.ext.show
import com.example.pcremote.ui.abstraction.BaseFragment
import kotlinx.android.synthetic.main.fragment_volume_control.*

class VolumeControlFragment: BaseFragment() {

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
            setClickListeners(fragmentActivity)
        }
    }

    private fun setClickListeners(activity: FragmentActivity) {
        volumeSb?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                checkConnectionStatus() ?: return
                val progress = (seekBar?.progress ?: 50) * VOLUME_PROGRESS_MULTIPLIER
                sharedViewModel?.communicate(
                    Message(Command.SET_VOLUME, progress)
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        observeConnectionStatus()
    }

    private fun getCurrentVolume() {
        sharedViewModel?.communicate(
            Message(Command.GET_VOLUME),
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
            Message(Command.MUTE),
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
            Message(Command.UNMUTE),
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
