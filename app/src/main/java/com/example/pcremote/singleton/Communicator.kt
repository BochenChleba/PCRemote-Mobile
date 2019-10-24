package com.example.pcremote.singleton

import com.example.pcremote.constants.NetworkConstants
import com.example.pcremote.exception.UnsuccessfulResponseException
import com.example.pcremote.ext.isAwaitingParamsResponse
import com.example.pcremote.ext.readResponse
import com.example.pcremote.ext.serialize
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class Communicator {

    private val socket = Socket().apply {
            connect(
                InetSocketAddress(NetworkConstants.SERVER_IP, NetworkConstants.PORT_NR),
                SOCKET_TIMEOUT
            )
    }
    private val outputStream = socket.getOutputStream()
    private val inputStream = socket.getInputStream()

    companion object {
        const val DATA_BUFF_SIZE = 64
        const val SOCKET_TIMEOUT = 5000

        const val COMMAND_PING = "0"
        const val COMMAND_SHUTDOWN_NOW = "1"
        const val COMMAND_SCHEDULE_SHUTDOWN = "2"
        const val COMMAND_ABORT_SHUTDOWN = "3"
        const val FEEDBACK_AWAITING_PARAMS = "ready"
        const val FEEDBACK_SUCCEED = "ok"
        const val FEEDBACK_PONG = "pong"

        private var instance: Communicator? = null

        fun getInstanceAsync(): Single<Communicator> {
            return Single.fromCallable {
                if (instance == null) {
                    instance = Communicator()
                }
                instance!!
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        fun reinstantiateAsync(): Single<Communicator> {
            return Single.fromCallable {
                instance?.socket?.close()
                instance = Communicator()
                instance!!
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun shutdownNow(): Single<List<String>> = sendCommand(COMMAND_SHUTDOWN_NOW)

    fun scheduleShutdown(remainingSeconds: String): Single<List<String>>
            = sendCommand(COMMAND_SCHEDULE_SHUTDOWN, listOf(remainingSeconds))

    fun abortShutdown() = sendCommand(COMMAND_ABORT_SHUTDOWN)

    fun ping() = sendCommand(COMMAND_PING)

    private fun sendCommand(command: String): Single<List<String>> {
        return Single.fromCallable {
            val dataBuff = ByteArray(DATA_BUFF_SIZE)
            outputStream.write(command.toByteArray())
            inputStream.read(dataBuff)
            dataBuff.readResponse()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun sendCommand(command: String, params: List<String>): Single<List<String>> {
        return Single.fromCallable {
            val dataBuff = ByteArray(DATA_BUFF_SIZE)
            val secondaryBuff = ByteArray(DATA_BUFF_SIZE)
            outputStream.write(command.toByteArray())
            inputStream.read(dataBuff)
            if (!dataBuff.isAwaitingParamsResponse()) {
                throw UnsuccessfulResponseException()
            }
            outputStream.write(params.serialize().toByteArray())
            inputStream.read(secondaryBuff)
            secondaryBuff.readResponse()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}