package com.example.pcremote.ui.connection_status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.enum.ConnectionStatus
import com.example.pcremote.ext.gone
import com.example.pcremote.ext.show
import com.example.pcremote.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_connection_status.*
import org.jetbrains.anko.support.v4.toast

class ConnectionStatusFragment: Fragment() {

    private var viewModel: MainViewModel? = null
    private var expanded = false

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
        activity?.let { viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java) }

        observeConnectionStatus()
        setOnClicks()
    }

    private fun observeConnectionStatus() {
        viewModel?.connectionStatus?.observe(this, Observer { status ->
            if (context == null) {
                return@Observer
            }

            when (status) {
                ConnectionStatus.CONNECTED -> {
                    toast("Connected with PC")
                    collapse()
                    progressBar.gone()
                    connectionStatusTv.text = getString(R.string.connectionStatusConnected)
                    rootLayout.setBackgroundColor(context!!.getColor(R.color.connectionOkBackground))
                }
                ConnectionStatus.CONNECTING -> {
                    toast("Connecting...")
                    progressBar.show()
                    connectionStatusTv.text = getString(R.string.connectionStatusConnecting)
                    rootLayout.setBackgroundColor(context!!.getColor(R.color.connectingBackground))
                }
                ConnectionStatus.DISCONNECTED -> {
                    toast("Cannot connect to PC")
                    expand()
                    progressBar.gone()
                    connectionStatusTv.text = getString(R.string.connectionStatusDisconnected)
                    rootLayout.setBackgroundColor(context!!.getColor(R.color.disconnectedBackground))
                }
                null -> {}
            }
        })
    }

    private fun setOnClicks() {
        rootLayout.setOnClickListener {
            if (expanded) {
                collapse()
            } else {
                expand()
            }
        }

        enterIpTv.setOnClickListener {
            toast("kurwa")
        }

        autoScanTv.setOnClickListener {
            toast("jeszcze nie ma")
        }

        reconnectTv.setOnClickListener {
            viewModel?.reinitializeCommunicator()
        }
    }

    private fun expand() {
        context?.let { ctx ->
            ipAddressTv.show()
            enterIpTv.show()
            autoScanTv.show()
            reconnectTv.show()
            arrowIv.setImageDrawable(ctx.getDrawable(R.drawable.ic_arrow_drop_up))
            expanded = true
        }
    }

    private fun collapse() {
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