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
        const val DATA_BUFF_SIZE = 64
        const val SOCKET_TIMEOUT = 2500

        const val COMMAND_PING = "0"
        const val COMMAND_SHUTDOWN_NOW = "1"
        const val COMMAND_SCHEDULE_SHUTDOWN = "2"
        const val COMMAND_ABORT_SHUTDOWN = "3"
        const val COMMAND_RESTART = "4"
        const val COMMAND_SET_VOLUME = "5"
        const val COMMAND_GET_VOLUME = "6"
        const val FEEDBACK_AWAITING_PARAMS = "ready"
        const val FEEDBACK_SUCCEED = "ok"
        const val FEEDBACK_PONG = "pong"

        private var instance: Communicator? = null

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

    fun ping() = sendCommand(COMMAND_PING)

    fun shutdownNow(): Single<List<String>> = sendCommand(COMMAND_SHUTDOWN_NOW)

    fun scheduleShutdown(params: Array<Any>): Single<List<String>>
            = sendCommand(COMMAND_SCHEDULE_SHUTDOWN, params)

    fun abortShutdown() = sendCommand(COMMAND_ABORT_SHUTDOWN)

    fun restart() = sendCommand(COMMAND_RESTART)

    fun setVolume(params: Array<Any>): Single<List<String>>
            = sendCommand(COMMAND_SET_VOLUME, params)

    fun getVolume(): Single<List<String>> = sendCommand(COMMAND_GET_VOLUME)

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

    private fun sendCommand(command: String, params: Array<Any>): Single<List<String>> {
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