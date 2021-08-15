package com.sun.findflight.data.source.ultils

import android.os.AsyncTask
import java.util.concurrent.CancellationException

class DownloaderTask<I, O>(
    private val callback: OnDataCallBack<O>,
    private val handler: (I) -> O?
) : AsyncTask<I, Unit, O?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: I) =
        try {
            handler(params[0])
        } catch (e: Exception) {
            exception = e
            null
        }

    override fun onPostExecute(result: O?) {
        super.onPostExecute(result)
        result?.let {
            callback.onSuccess(it)
        } ?: callback.onFailure(exception)
    }

    override fun onCancelled() {
        super.onCancelled()
        callback.onFailure(CancellationException(ERROR_TIME_OUT))
    }

    companion object {
        const val WAIT_TIMEOUT = 5000L
        const val ERROR_TIME_OUT= "Connection error or timeout"
    }
}
