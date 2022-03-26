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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var txtWifi: TextView
    lateinit var btnScan: Button

    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
    }

    private fun setupView() {
        setContentView(R.layout.activity_main)
        txtWifi = findViewById(R.id.txtWifi)
        btnScan = findViewById(R.id.btnStartScan)

        btnScan.setOnClickListener {
            //setupWifi()
            scanIp()
        }
    }

    private fun setupWifi() {
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        this.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager.startScan()
    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults

        if (results.isNotEmpty()) {
            txtWifi.text = results[0].toString()
        }
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
    }

    private fun startIpScan() {

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiInfo = wifiManager.connectionInfo

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
                //var subnet = getSubnetAddress()
                txtWifi.text = "${txtWifi.text} -- $ip"
                Log.d(TAG, ip)
            }
        }

        GlobalScope.launch {
            checkHosts(subnetIp)
        }
    }

    private fun getSubnetAddress(address: Int): String {
        return String.format(
            "%d.%d.%d",
            (address and 0xff),
            (address shr 8 and 0xff),
            (address shr 16 and 0xff)
        )
    }

    private fun checkHosts(subnet: String) {
        Log.d(TAG, "Host :: $subnet")
        val splittedIp = subnet.split(".")
        var realSubnet = ""
        for (i in 0..2) {
            realSubnet = "$realSubnet${splittedIp[i]}."
        }
        try {
            val timeout = 5
            for ( i in 1..255) {
                val host = "$realSubnet$i"
                Log.d(TAG, "Check host :: $host")
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d(TAG, "checkHosts() :: "+host + " is reachable")
                }
            }
        }
        catch (e: UnknownHostException) {
            Log.d(TAG, "checkHosts() :: UnknownHostException e : "+e)
            e.printStackTrace()
        }
        catch (e: IOException) {
            Log.d(TAG, "checkHosts() :: IOException e : "+e)
            e.printStackTrace()
        }
    }
}
