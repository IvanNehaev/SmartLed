package com.nekhaev.android.smartled.domain.utils

interface WifiScanUtil {

    suspend fun getHostAddress(): String

    suspend fun scanSubnet(subnet: String): List<String>

}