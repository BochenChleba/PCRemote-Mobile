package com.example.pcremote.singleton

import com.example.pcremote.data.constants.NetworkConstants
import com.example.pcremote.data.dto.Message
import com.example.pcremote.ext.readResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.InetSocketAddress
import java.net.Socket

class Communicator(ipAddress: String) {

    private val socket = Socket().apply {
            connect(
                InetSocketAddress(ipAddress, NetworkConstants.PORT_NR),
                SOCKET_TIMEOUT
            )
    }
    private val outputStream = socket.getOutputStream()
    private val inputStream = socket.getInputStream()

    companion object {
        const val DATA_BUFF_SIZE = 1024
        const val SOCKET_TIMEOUT = 2500
        private var instance: Communicator? = null

        @Synchronized
        fun getInstanceAsync(ipAddress: String): Single<Communicator> {
            return Single.fromCallable {
                if (instance == null) {
                    instance = Communicator(ipAddress)
                }
                instance!!
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        @Synchronized
        fun reinstantiateAsync(ipAddress: String): Single<Communicator> {
            return Single.fromCallable {
                instance?.socket?.close()
                instance = Communicator(ipAddress)
                instance!!
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun sendCommand(message: Message): Single<List<String>> {
        return Single.fromCallable {
            val dataBuff = ByteArray(DATA_BUFF_SIZE)
            outputStream.write(message.toByteArray())
            inputStream.read(dataBuff)
            if (message.command.awaitsForResponse) {
                dataBuff.readResponse()
            } else {
                emptyList()
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
