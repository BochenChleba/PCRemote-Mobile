package com.example.pcremote.ui.fragment.connection_status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.data.enum.ConnectionStatus
import com.example.pcremote.ext.gone
import com.example.pcremote.ext.show
import com.example.pcremote.ui.activity.main.MainViewModel
import com.example.pcremote.ui.dialog.enter_ip.EnterIpDialog
import com.example.pcremote.singleton.Preferences
import kotlinx.android.synthetic.main.fragment_connection_status.*
import org.jetbrains.anko.support.v4.toast

class ConnectionStatusFragment: Fragment() {

    private var viewModel: MainViewModel? = null
    private var expanded = false
    private var pingingThread: PingingThread? = null

    companion object {
        fun newInstance(): ConnectionStatusFragment {
            return ConnectionStatusFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_connection_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { actv ->
            viewModel = ViewModelProviders.of(actv).get(MainViewModel::class.java)
            setDialogCallbacks()
            setOnClicks(actv)
            ipAddressTv.text = getString(
                R.string.connection_status_ip_address,
                Preferences.getInstance(actv).getIpAddress()
            )
        }
        observeConnectionStatus()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.let { vm ->
           // pingingThread = PingingThread(vm).apply { run() }
        }
    }

    override fun onPause() {
        super.onPause()
        pingingThread?.stopPinging()
        pingingThread = null
    }

    private fun observeConnectionStatus() {
        viewModel?.connectionStatus?.observe(this, Observer { status ->
            if (context == null) {
                return@Observer
            }

            when (status) {
                ConnectionStatus.CONNECTED -> {
                    collapse()
                    progressBar.gone()
                    connectionStatusTv.text = getString(R.string.connectionStatusConnected)
                    rootLayout.setBackgroundColor(context!!.getColor(R.color.connectionOkBackground))
                }
                ConnectionStatus.CONNECTING -> {
                    progressBar.show()
                    connectionStatusTv.text = getString(R.string.connectionStatusConnecting)
                    rootLayout.setBackgroundColor(context!!.getColor(R.color.connectingBackground))
                }
                ConnectionStatus.DISCONNECTED -> {
                    expand()
                    progressBar.gone()
                    connectionStatusTv.text = getString(R.string.connectionStatusDisconnected)
                    rootLayout.setBackgroundColor(context!!.getColor(R.color.disconnectedBackground))
                }
                null -> {}
            }
        })
    }

    private fun setDialogCallbacks() {
        EnterIpDialog.confirmCallback = { ipAddress: String ->
            viewModel?.prefs?.setIpAddress(ipAddress)
            viewModel?.reinitializeCommunicator()
            ipAddressTv.text = ipAddress
        }
    }

    private fun setOnClicks(activity: FragmentActivity) {
        rootLayout.setOnClickListener {
            if (expanded) {
                collapse()
            } else {
                expand()
            }
        }

        enterIpTv?.setOnClickListener {
            EnterIpDialog.newInstance()
                .show(activity.supportFragmentManager, EnterIpDialog.TAG)
        }

        autoScanTv?.setOnClickListener {
            toast("jeszcze nie ma")
        }

        reconnectTv?.setOnClickListener {
            viewModel?.reinitializeCommunicator()
        }
    }

    fun expand() {
        context?.let { ctx ->
            ipAddressTv.show()
            enterIpTv.show()
            autoScanTv.show()
            reconnectTv.show()
            arrowIv.setImageDrawable(ctx.getDrawable(R.drawable.ic_arrow_drop_up))
            expanded = true
        }
    }

    fun collapse() {
        context?.let { ctx ->
            ipAddressTv.gone()
            enterIpTv.gone()
            autoScanTv.gone()
            reconnectTv.gone()
            arrowIv.setImageDrawable(ctx.getDrawable(R.drawable.ic_arrow_drop_down))
            expanded = false
        }
    }
}