package com.nekhaev.android.smartled.data.utils

import android.util.Log
import com.nekhaev.android.smartled.domain.utils.WifiScanUtil
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

class WifiScanUtilImpl: WifiScanUtil {

    private val TAG = this.javaClass.simpleName.toString()

    override suspend fun getHostAddress(): String {
        val enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (enumNetworkInterfaces.hasMoreElements()) {
            val networkInterface = enumNetworkInterfaces.nextElement()
            val enumInetAddress = networkInterface.inetAddresses
            while (enumInetAddress.hasMoreElements()) {
                val inetAddress = enumInetAddress.nextElement()
                if (inetAddress.isSiteLocalAddress) {
                    return inetAddress.hostAddress
                }
            }
        }
        return ""
    }

    override suspend fun scanSubnet(subnet: String): List<String> {
        val addressList = mutableListOf<String>()
        Log.d(TAG, "Host :: $subnet")
        val splitIp = subnet.split(".")
        var newAddress = ""
        for (i in 0..2) {
            newAddress = "$newAddress${splitIp[i]}."
        }
        try {
            val timeout = 20
            for ( i in 1..255) {
                val host = "$newAddress$i"
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d(TAG, "checkHosts() :: $host is reachable")
                    addressList.add(host)
                }
            }
        }
        catch (e: UnknownHostException) {
            Log.d(TAG, "checkHosts() :: UnknownHostException e : $e")
            e.printStackTrace()
        }
        catch (e: IOException) {
            Log.d(TAG, "checkHosts() :: IOException e : $e")
            e.printStackTrace()
        }
        return addressList
    }
}