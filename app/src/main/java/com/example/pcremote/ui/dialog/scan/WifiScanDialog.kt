package com.example.pcremote.ui.dialog.scan

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pcremote.R
import com.example.pcremote.ext.gone
import com.example.pcremote.ui.abstraction.BaseDialog
import kotlinx.android.synthetic.main.dialog_scan.*
import org.jetbrains.anko.toast

class WifiScanDialog : BaseDialog(), WifiScanNavigator {

    companion object {
        const val TAG = "Wifi scan"
        fun newInstance(): WifiScanDialog {
            return WifiScanDialog()
        }
    }

    lateinit var itemSelectedCallback: (String)->Unit
    private lateinit var viewModel: WifiScanViewModel
    private lateinit var adapter: ArrayAdapter<String>
    private val foundAddresses = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_scan, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { fragmentActivity ->
            initializeViewModel(fragmentActivity)
            initializeListView(fragmentActivity)
            setOnClickListeners()
            initializeWifiScan(fragmentActivity)
        }
    }

    private fun initializeViewModel(activity: FragmentActivity) {
        viewModel = ViewModelProviders.of(activity).get(WifiScanViewModel::class.java)
        viewModel.setNavigator(this)
        viewModel.shouldPerformScan = true
    }

    private fun initializeListView(activity: FragmentActivity) {
        availableDevicesListView.setOnItemClickListener { _, _, position, _ ->
            itemSelectedCallback.invoke(foundAddresses[position])
            dismiss()
        }
        adapter = ArrayAdapter(activity, R.layout.list_item_ip_address, ArrayList<String>())
        availableDevicesListView.adapter = adapter
    }

    private fun setOnClickListeners() {
        declineTv.setOnClickListener {
            dismiss()
        }
    }

    private fun initializeWifiScan(activity: FragmentActivity) {
        val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        viewModel.performWifiScan(wifiManager)
    }

    override fun updateFoundDevicesList(address: String) {
        foundAddresses.add(address)
        adapter.add(address)
        adapter.notifyDataSetChanged()
        Log.d("kurwamac", address)
    }

    override fun scanCompleted() {
        progressBar.gone()
        messageTv.gone()
        activity?.toast(R.string.wifi_scan_dialog_scan_completed_toast)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.shouldPerformScan = false
    }

}
