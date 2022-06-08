package com.nekhaev.android.smartled.presentation.connection

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekhaev.android.smartled.R
import com.nekhaev.android.smartled.domain.use_case.GetSmartLedIpUseCase
import com.nekhaev.android.smartled.presentation.views.fairyLightView.FairyLightsView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val getSmartLedIpUseCase: GetSmartLedIpUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ConnectionUi>(ConnectionUi.NoConnectionUi())
    val state: LiveData<ConnectionUi> = _state


    fun onButtonConnectClick() {
        _state.value = ConnectionUi.LoadingUi()
        viewModelScope.launch {
            var smartLedIp = ""

            viewModelScope.launch(Dispatchers.IO) {
                smartLedIp = getSmartLedIpUseCase()
            }.join()

            if (smartLedIp.isEmpty()) {
                _state.value = ConnectionUi.ErrorUi()
            } else {
                _state.value = ConnectionUi.ConnectedUi(smartLedIp)
            }
        }
    }

    interface ConnectionUi {

        fun apply(
            progressBar: View,
            btnConnect: Button,
            txtConnectionResult: TextView,
            fairyLightsView: FairyLightsView,
            connectedText: String,
            connectionErrorText: String,
            noConnectionText: String
        )

        class LoadingUi : ConnectionUi {
            override fun apply(
                progressBar: View,
                btnConnect: Button,
                txtConnectionResult: TextView,
                fairyLightsView: FairyLightsView,
                connectedText: String,
                connectionErrorText: String,
                noConnectionText: String
            ) {
                progressBar.visibility = View.VISIBLE
                btnConnect.isEnabled = false
                txtConnectionResult.visibility = View.GONE
                fairyLightsView.effects.loadingEffect()
            }
        }

        class ConnectedUi(
            private val address: String
        ) : ConnectionUi {
            override fun apply(
                progressBar: View,
                btnConnect: Button,
                txtConnectionResult: TextView,
                fairyLightsView: FairyLightsView,
                connectedText: String,
                connectionErrorText: String,
                noConnectionText: String
            ) {
                progressBar.visibility = View.GONE
                btnConnect.isEnabled = false
                txtConnectionResult.visibility = View.VISIBLE
                txtConnectionResult.text = "$connectedText} to $address"
            }
        }

        class ErrorUi : ConnectionUi {
            override fun apply(
                progressBar: View,
                btnConnect: Button,
                txtConnectionResult: TextView,
                fairyLightsView: FairyLightsView,
                connectedText: String,
                connectionErrorText: String,
                noConnectionText: String
            ) {
                progressBar.visibility = View.GONE
                btnConnect.isEnabled = true
                txtConnectionResult.visibility = View.VISIBLE
                txtConnectionResult.text = connectionErrorText

                fairyLightsView.effects.errorEffect()
            }
        }

        class NoConnectionUi : ConnectionUi {
            override fun apply(
                progressBar: View,
                btnConnect: Button,
                txtConnectionResult: TextView,
                fairyLightsView: FairyLightsView,
                connectedText: String,
                connectionErrorText: String,
                noConnectionText: String
            ) {
                progressBar.visibility = View.GONE
                btnConnect.isEnabled = true
                txtConnectionResult.visibility = View.VISIBLE
                txtConnectionResult.text = noConnectionText
            }
        }
    }
}