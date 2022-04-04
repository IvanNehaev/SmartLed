package com.nekhaev.android.smartled.domain.repository

interface SmartLedRepository {

    suspend fun getSmartLedIp(addressList: List<String>): String
}