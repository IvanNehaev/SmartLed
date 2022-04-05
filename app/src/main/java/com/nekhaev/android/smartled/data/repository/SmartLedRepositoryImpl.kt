package com.nekhaev.android.smartled.data.repository

import android.util.Log
import com.nekhaev.android.smartled.data.remote.SmartLedApi
import com.nekhaev.android.smartled.domain.repository.SmartLedRepository

class SmartLedRepositoryImpl(
    private val api: SmartLedApi
): SmartLedRepository {
    private val TAG = SmartLedRepositoryImpl::class.java.simpleName.toString()

    override suspend fun getSmartLedIp(addressList: List<String>): String {
        for(address in addressList) {
            Log.d(TAG, "Address: $address")
            if (api.getControlPanel(address))
                return address
        }
        return ""
    }
}