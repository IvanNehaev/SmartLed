package com.nekhaev.android.smartled

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var txtWifi: TextView
    lateinit var btnScan: Button

    private lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupWifi()
    }

    private fun setupView() {
        setContentView(R.layout.activity_main)
        txtWifi = findViewById(R.id.txtWifi)
        btnScan = findViewById(R.id.btnStartScan)

        btnScan.setOnClickListener {
            startScan()
        }
    }

    private fun setupWifi() {
        registerReceiver(WifiScanReceiver(), IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private fun startScan() {
        val scanList = wifiManager.scanResults
        if (scanList.isNotEmpty()) {
            txtWifi.text = scanList[0].toString()
        }
    }
}

class WifiScanReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        TODO("Not yet implemented")
    }
}