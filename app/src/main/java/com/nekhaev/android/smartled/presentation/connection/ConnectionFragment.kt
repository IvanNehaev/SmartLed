package com.nekhaev.android.smartled.presentation.connection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.nekhaev.android.smartled.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ConnectionFragment: Fragment(R.layout.fragment_connection) {

    private lateinit var mBtnConnect: Button
    private lateinit var mProgress: ProgressBar
    private lateinit var mTxtConnectionResult: TextView

    private val viewModel: ConnectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi(view)
        setStateObserver()
    }

    private fun setupUi(view: View) {
        mProgress = view.findViewById(R.id.connection_progress)
        mTxtConnectionResult = view.findViewById(R.id.connection_txtConnectionResult)
        mBtnConnect = view.findViewById(R.id.connection_btnConnect)
        mBtnConnect.setOnClickListener {
            viewModel.onButtonConnectClick()
        }
    }

    private fun setStateObserver() {
        viewModel.state.observe(viewLifecycleOwner) { connectionState ->
            when (connectionState) {
                is ConnectionState.Loading -> {
                    setLoadingState()
                }
                is ConnectionState.Connected -> {
                    setConnectedState(connectionState.smartLedAddress)
                }
                is ConnectionState.Error -> {
                    setErrorState()
                }
                is ConnectionState.NoConnection -> {
                    setNoConnectionState()
                }
            }
        }
    }

    private fun setLoadingState() {
        mProgress.visibility = View.VISIBLE
        mBtnConnect.isEnabled = false
        mTxtConnectionResult.visibility = View.GONE
    }

    private fun setConnectedState(address: String) {
        mProgress.visibility = View.GONE
        mBtnConnect.isEnabled = false
        mTxtConnectionResult.visibility = View.VISIBLE
        mTxtConnectionResult.text = "${getString(R.string.connected_text)} to $address"
    }

    private fun setErrorState() {
        mProgress.visibility = View.GONE
        mBtnConnect.isEnabled = true
        mTxtConnectionResult.visibility = View.VISIBLE
        mTxtConnectionResult.text = getString(R.string.connection_error_text)
    }

    private fun setNoConnectionState() {
        mProgress.visibility = View.GONE
        mBtnConnect.isEnabled = true
        mTxtConnectionResult.visibility = View.VISIBLE
        mTxtConnectionResult.text = getString(R.string.no_connection_text)
    }
}