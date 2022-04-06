package com.nekhaev.android.smartled.data.remote

interface SmartLedApi {

    var smartLedIp: String

    fun isReachable(): Boolean

    fun getControlPanel(): Boolean

    suspend fun setBrightness(value: Int): Boolean
}