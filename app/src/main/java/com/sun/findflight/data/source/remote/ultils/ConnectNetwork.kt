package com.sun.findflight.data.source.remote.ultils

import android.util.Base64
import android.util.Log
import com.sun.findflight.BuildConfig
import com.sun.findflight.utils.NetworkConst
import com.sun.findflight.utils.TimeUtils
import com.sun.findflight.utils.TimeUtils.updateTokenTime
import com.sun.findflight.utils.requestNewToken
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL

private var bearerToken: Pair<String?, Long?> = null to null

fun getNetworkAuthority(urlLink: String) {
    val clientId = BuildConfig.API_KEY
    val clientSecret = BuildConfig.API_SECRET
    val auth = "$clientId:$clientSecret"
    val authentication = Base64.encodeToString(auth.toByteArray(), Base64.DEFAULT)
    val stringBuilder = StringBuilder()
    try {
        val url = URL(urlLink)
        val urlOpenConnection = url.openConnection() as HttpURLConnection
        urlOpenConnection.apply {
            requestMethod = NetworkConst.POST
            doOutput = true
            setRequestProperty(NetworkConst.AUTHORIZATION, "${NetworkConst.BASIC} $authentication")
            setRequestProperty(NetworkConst.CONTENT_TYPE, NetworkConst.DEFAULT_CONTENT_TYPE)
            setRequestProperty(NetworkConst.ACCEPT, NetworkConst.RESPONSE_TYPE)
        }
        val printStream = PrintStream(urlOpenConnection.outputStream)
        printStream.print(NetworkConst.GRANT_TYPE)
        printStream.close()
        val inputStreamReader = InputStreamReader(urlOpenConnection.inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        bufferedReader.forEachLine {
            stringBuilder.append(it)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    with(JSONObject(stringBuilder.toString())) {
        bearerToken =
            getString(NetworkConst.ACCESS_TOKEN) to updateTokenTime(getLong(NetworkConst.EXPIRES_IN))
    }
}

fun getNetworkData(urlLink: String): String {
    Log.d("FindFlight", "getNetworkData: $urlLink")
    val stringBuilder = StringBuilder()
    if (bearerToken.first == null || TimeUtils.tokenExpired(bearerToken.second)) requestNewToken()
    try {
        val url = URL(urlLink)
        val urlOpenConnection = url.openConnection() as HttpURLConnection
        urlOpenConnection.apply {
            setRequestProperty(NetworkConst.AUTHORIZATION, "${NetworkConst.BEARER} ${bearerToken.first}")
            requestMethod = NetworkConst.GET
        }
//        if (urlOpenConnection.responseCode == 200) {
            //TODO change
            val inputStreamReader = InputStreamReader(urlOpenConnection.inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            bufferedReader.forEachLine {
                stringBuilder.append(it)
            }
//        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}
