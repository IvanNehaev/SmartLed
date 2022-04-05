package com.nekhaev.android.smartled.data.remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException

class SmartLedApiImpl : SmartLedApi {

    private val TAG = SmartLedApiImpl::class.java.simpleName.toString()
    private val baseUrl = "http://"

    override fun getControlPanel(ip: String): Boolean {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl$ip")
            .build()

        try {
            var response = client.newCall(request).execute()
            Log.d(TAG, "GET $baseUrl$ip Code: ${response.code} isSuccessful: ${response.isSuccessful}")
            return response.isSuccessful
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.toString())
        }
        return false
    }
}