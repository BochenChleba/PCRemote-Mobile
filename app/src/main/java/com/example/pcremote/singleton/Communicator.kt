package com.example.pcremote.singleton

import com.example.pcremote.NetworkConstants
import com.example.pcremote.exception.UnsuccessfulResponseException
import com.example.pcremote.ext.isAwaitingParamsResponse
import com.example.pcremote.ext.isSuccessfulResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.Socket

class Communicator {

    private val socket = Socket(NetworkConstants.SERVER_IP, NetworkConstants.PORT_NR)
    private val outputStream = socket.getOutputStream()
    private val inputStream = socket.getInputStream()
    private val secondaryBuff = ByteArray(4)
    private val dataBuff = ByteArray(512)
    private var dataSize: Int = 0

    companion object {
        const val COMMAND_SHUTDOWN_NOW = "1"
        const val COMMAND_SCHEDULE_SHUTDOWN = "2"
        const val FEEDBACK_AWAITING_PARAMS = "ready"
        const val FEEDBACK_SUCCEED = "ok"

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

    fun shutdownNow(): Single<Unit> = sendCommand(COMMAND_SHUTDOWN_NOW)
    fun scheduleShutdown(remainingSeconds: String): Single<Unit> = sendCommand(COMMAND_SCHEDULE_SHUTDOWN, remainingSeconds)

    private fun sendCommand(command: String): Single<Unit> {
        return Single.fromCallable {
            outputStream.write(command.toByteArray())
            inputStream.read(dataBuff)
            if (!dataBuff.isSuccessfulResponse()) {
                throw UnsuccessfulResponseException()
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun sendCommand(command: String, params: String): Single<Unit> {
        return Single.fromCallable {
            outputStream.write(command.toByteArray())
            inputStream.read(dataBuff)
            if (!dataBuff.isAwaitingParamsResponse()) {
                throw UnsuccessfulResponseException()
            }
            outputStream.write(params.toByteArray())
            inputStream.read(secondaryBuff)
            if (!secondaryBuff.isSuccessfulResponse()) {
                throw UnsuccessfulResponseException()
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}