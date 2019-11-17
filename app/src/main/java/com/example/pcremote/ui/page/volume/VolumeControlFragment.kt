package com.example.pcremote.ui.page.volume

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.enum.ConnectionStatus
import com.example.pcremote.ext.gone
import com.example.pcremote.ext.hide
import com.example.pcremote.ext.show
import com.example.pcremote.singleton.Communicator
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

    override fun onResume() {
        super.onResume()
        getCurrentVolume()
        observeConnectionStatus()
    }

    override fun onPause() {
        super.onPause()
        disableVolumeSeekBar()
        stopObservingConnectionStatus()
    }

    private fun observeConnectionStatus() {
        sharedViewModel?.connectionStatus?.observe(this, Observer { connectionStatus ->
            if (connectionStatus == ConnectionStatus.CONNECTED) {
                enableVolumeSeekBar()
                getCurrentVolume()
            }
        })
    }

    private fun stopObservingConnectionStatus() {
        sharedViewModel?.connectionStatus?.removeObservers(this)
    }

    private fun getCurrentVolume() {
        sharedViewModel?.communicate(
            Communicator.COMMAND_GET_VOLUME,
            onSuccess = {
                it.firstOrNull()?.let { currentVolume ->
                    volumeSb.progress = currentVolume.toInt() / VOLUME_PROGRESS_MULTIPLIER
                }
                enableVolumeSeekBar()
            },
            onFailure = {
                enableVolumeSeekBar()
            }
        )
    }

    private fun enableVolumeSeekBar() {
        volumeSb.show()
        progressBar.hide()
    }

    private fun disableVolumeSeekBar() {
        volumeSb.hide()
        progressBar.show()
    }

    private fun setClickListeners(activity: FragmentActivity) {
        volumeSb?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                checkConnectionStatus() ?: return
                val progress = seekBar?.progress ?: 50
                sharedViewModel?.communicate(
                    Communicator.COMMAND_SET_VOLUME,
                    progress * VOLUME_PROGRESS_MULTIPLIER
                )
            }
        })
    }
}