package com.nekhaev.android.smartled.data.remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.IllegalStateException
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class SmartLedApiImpl: SmartLedApi {

    private val TAG = SmartLedApiImpl::class.java.simpleName.toString()
    private val baseUrl = "http://"
    private val setCommandUrl = "/set"

    private val client = OkHttpClient()

    override var smartLedIp: String = ""

    override fun isReachable() = getControlPanel()

    override fun getControlPanel(): Boolean {

        val request = Request.Builder()
            .url("$baseUrl$smartLedIp")
            .build()

        try {
            val response = client.newCall(request).execute()
            Log.d(
                TAG,
                "GET $baseUrl$smartLedIp Code: ${response.code} isSuccessful: ${response.isSuccessful}"
            )
            return response.isSuccessful
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.toString())
        }
        return false
    }

    override suspend fun setBrightness(value: Int): Boolean {
        val urlBuilder = "$baseUrl$smartLedIp$setCommandUrl".toHttpUrlOrNull()?.newBuilder()

        urlBuilder?.let { builder ->
            builder.addQueryParameter("b", "$value")

            val url = builder.build().toString()

            val request = Request.Builder()
                .url(url)
                .build()
            try {
                val response = client.newCall(request).execute()
                Log.d(
                    TAG,
                    "$url CODE: ${response.code}"
                )
                return response.isSuccessful
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            } catch (e: IllegalStateException) {
                Log.e(TAG, e.toString())
            }
        }
        return false
    }
}