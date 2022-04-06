package com.nekhaev.android.smartled

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nekhaev.android.smartled.presentation.connection.ConnectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

@AndroidEntryPoint
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
    }
}
