package com.nekhaev.android.smartled.presentation.connection

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nekhaev.android.smartled.R
import com.nekhaev.android.smartled.presentation.control_panel.ControlPanelFragment
import com.nekhaev.android.smartled.presentation.views.fairyLightView.FairyLightsView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

@AndroidEntryPoint
class ConnectionFragment : Fragment(R.layout.fragment_connection) {

    private val TAG = ConnectionFragment::class.java.simpleName.toString()

    private lateinit var mBtnConnect: Button
    private lateinit var mProgress: ProgressBar
    private lateinit var mTxtConnectionResult: TextView
    private lateinit var mFairyLightsView: FairyLightsView

    private val viewModel: ConnectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi(view)
        setStateObserver()
    }

    override fun onResume() {
        super.onResume()

        // Color test
        lifecycleScope.launch(Dispatchers.Main) {
            var index = 0

            while (true) {
                mFairyLightsView.setFairyLightColor(index, Color.GREEN)
                if (index < mFairyLightsView.fairyLightsCount - 1) {
                    index++
                } else {
                    index = 0
                }
                mFairyLightsView.setFairyLightColor(index, Color.BLUE)
                delay(500)
            }
        }

        // Scale test
        lifecycleScope.launch(Dispatchers.Main) {
            var scale = 1f
            var upscale = 1

            while (true) {
                mFairyLightsView.fairyLightsList.map {
                    it.scaleFactor = scale
                }
                mFairyLightsView.setFairyLightScale(0, scale)
                if (upscale == 1) {
                    if (scale < 10f) {
                        scale += 0.1f
                    } else {
                        upscale = 0
                    }
                } else {
                    if (scale > 0) {
                        scale -= 0.1f
                    } else {
                        upscale = 1
                    }
                }
                delay(70)
            }
        }
    }

    private fun setupUi(view: View) {
        mProgress = view.findViewById(R.id.connection_progress)
        mTxtConnectionResult = view.findViewById(R.id.connection_txtConnectionResult)
        mBtnConnect = view.findViewById(R.id.connection_btnConnect)
        mBtnConnect.setOnClickListener {
            viewModel.onButtonConnectClick()
        }

        mFairyLightsView = view.findViewById(R.id.fairyLightsView)
    }

    private fun setStateObserver() {
        viewModel.state.observe(viewLifecycleOwner) { connectionState ->
            when (connectionState) {
                is ConnectionState.Loading -> {
                    setLoadingState()
                }
                is ConnectionState.Connected -> {
                    setConnectedState(connectionState.smartLedAddress)
                    navigateToControlPanel()
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

    private fun navigateToControlPanel() {
        try {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container_view, ControlPanelFragment())
                commit()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
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