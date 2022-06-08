package com.nekhaev.android.smartled.presentation.connection

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nekhaev.android.smartled.R
import com.nekhaev.android.smartled.presentation.afterMeasured
import com.nekhaev.android.smartled.presentation.control_panel.ControlPanelFragment
import com.nekhaev.android.smartled.presentation.views.fairyLightView.FairyLightAnimations
import com.nekhaev.android.smartled.presentation.views.fairyLightView.FairyLightsView
import dagger.hilt.android.AndroidEntryPoint
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

    private fun setupUi(view: View) {
        mProgress = view.findViewById(R.id.connection_progress)
        mTxtConnectionResult = view.findViewById(R.id.connection_txtConnectionResult)
        mBtnConnect = view.findViewById(R.id.connection_btnConnect)
        mBtnConnect.setOnClickListener {
            viewModel.onButtonConnectClick()
        }

        mFairyLightsView = view.findViewById(R.id.fairyLightsView)
        mFairyLightsView.afterMeasured {
            this as FairyLightsView
            this.effects.onStartEffect()
        }
    }

    private fun setStateObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            it.apply(
                progressBar = mProgress,
                btnConnect = mBtnConnect,
                txtConnectionResult = mTxtConnectionResult,
                fairyLightsView = mFairyLightsView,
                connectedText = getString(R.string.connected_text),
                connectionErrorText = getString(R.string.connection_error_text),
                noConnectionText = getString(R.string.no_connection_text)
            )
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
}