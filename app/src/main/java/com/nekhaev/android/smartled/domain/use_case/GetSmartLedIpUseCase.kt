package com.nekhaev.android.smartled.domain.use_case

import com.nekhaev.android.smartled.domain.repository.SmartLedRepository
import com.nekhaev.android.smartled.domain.utils.WifiScanUtil

class GetSmartLedIpUseCase(
    private val wifiScanUtil: WifiScanUtil,
    private val smartLedRepository: SmartLedRepository
) {
    suspend operator fun invoke(): String {
        val hostIp = wifiScanUtil.getHostAddress()
        val addressList = wifiScanUtil.scanSubnet(hostIp)
        return smartLedRepository.getSmartLedIp(addressList)
    }
}