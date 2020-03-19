package com.example.pcremote.ui.dialog.scan

import android.net.wifi.WifiManager
import android.util.Log
import com.example.pcremote.data.constants.NetworkConstants
import com.example.pcremote.ui.activity.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.InetSocketAddress
import java.net.Socket

private const val PING_TIMEOUT = 100

class WifiScanViewModel: BaseViewModel<WifiScanNavigator>() {

    fun performWifiScan(wifiManager: WifiManager) {
        val subnetAddress = getSubnetAddress(wifiManager.dhcpInfo.gateway)
        getAvailableHosts(subnetAddress)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ address ->
                navigator.updateFoundDevicesList(address)
            }, { ex ->
                Log.e(WifiScanDialog.TAG, ex.toString())
            }, {
                navigator.scanCompleted()
            })
            .let { compositeDisposable.add(it) }
    }

    private fun getSubnetAddress(address: Int) =
        String.format(
            "%d.%d.%d",
            address and 0xff,
            address shr 8 and 0xff,
            address shr 16 and 0xff
        )

    private fun getAvailableHosts(subnetAddress: String): Observable<String> {
        return Observable.create { emitter ->
            Thread.sleep(5000)
            for (i in 1..255) {
                val hostAddress = "$subnetAddress.$i"
                try {
                    Socket().use { soc ->
                        soc.connect(
                            InetSocketAddress(hostAddress, NetworkConstants.PORT_NR),
                            PING_TIMEOUT
                        )
                    }
                    emitter.onNext(hostAddress)
                } catch (ex: Exception) {}
            }
            emitter.onNext("192.169.2.224")
            emitter.onNext("192.169.0.0")
            emitter.onComplete()
        }
    }
}
