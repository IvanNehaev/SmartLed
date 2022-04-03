package com.nekhaev.android.smartled

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.nekhaev.android.smartled.presentation.connection.ConnectionFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
           supportFragmentManager.beginTransaction()
               .setReorderingAllowed(true)
               .add(R.id.fragment_container_view, ConnectionFragment())
               .commit()
        }

        setupView()
    }

    private fun setupView() {

        scanIp()
    }

    private fun scanIp() {
        var ip = ""
        var subnetIp = ""
        val enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (enumNetworkInterfaces.hasMoreElements()) {
            val networkInterface = enumNetworkInterfaces.nextElement()
            val enumInetAddress = networkInterface.inetAddresses
            while (enumInetAddress.hasMoreElements()) {
                val inetAddress = enumInetAddress.nextElement()
                var ipAddress = ""
                if (inetAddress.isSiteLocalAddress) {
                    ipAddress = "SiteLocalAddress: "
                    if (subnetIp.isBlank()) {
                        subnetIp = inetAddress.hostAddress
                    }
                }
                ip = "$ipAddress${inetAddress.hostAddress}\n"

                Log.d(TAG, ip)
            }
        }

        GlobalScope.launch {
            checkHosts(subnetIp)
        }
    }

    private fun checkHosts(subnet: String) {
        Log.d(TAG, "Host :: $subnet")
        val splittedIp = subnet.split(".")
        var realSubnet = ""
        for (i in 0..2) {
            realSubnet = "$realSubnet${splittedIp[i]}."
        }
        try {
            val timeout = 20
            for ( i in 1..255) {
                val host = "$realSubnet$i"
                //Log.d(TAG, "Check host :: $host")
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d(TAG, "checkHosts() :: $host is reachable")
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
    }
}
