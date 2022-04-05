package com.nekhaev.android.smartled.data.remote

interface SmartLedApi {

    fun getControlPanel(ip: String): Boolean
}