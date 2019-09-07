package com.example.pcremote.singleton

import com.example.pcremote.Constants
import com.example.pcremote.exception.UnsuccessfulResponseException
import com.example.pcremote.ext.isSuccessfulResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.Socket

class Communicator {

    private val socket = Socket(Constants.SERVER_IP, Constants.PORT_NR)
    private val outputStream = socket.getOutputStream()
    private val inputStream = socket.getInputStream()
    private val secondaryBuff = ByteArray(4)
    private val dataBuff = ByteArray(512)
    private var dataSize: Int = 0

    companion object {
        const val COMMAND_SHUTDOWN_NOW = "1"
        const val FEEDBACK_READY_TO_SEND = "ready"
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
    }

    fun ping() {

    }

    fun shutdownNow(): Single<Unit> =
        Single.fromCallable {
            outputStream.write(COMMAND_SHUTDOWN_NOW.toByteArray())
            inputStream.read(dataBuff)
            if (!dataBuff.isSuccessfulResponse()) {
                throw UnsuccessfulResponseException()
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


}