package com.nekhaev.android.smartled.presentation.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekhaev.android.smartled.R
import com.nekhaev.android.smartled.data.remote.SmartLedApiImpl
import com.nekhaev.android.smartled.data.repository.SmartLedRepositoryImpl
import com.nekhaev.android.smartled.data.utils.WifiScanUtilImpl
import com.nekhaev.android.smartled.domain.use_case.GetSmartLedIpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val getSmartLedIpUseCase: GetSmartLedIpUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ConnectionState>(ConnectionState.NoConnection())
    val state: LiveData<ConnectionState> = _state

    var pickedColor: Int = 0

    fun onButtonConnectClick() {
        _state.value = ConnectionState.Loading()
        viewModelScope.launch {
            var smartLedIp = ""

            viewModelScope.launch(Dispatchers.IO) {
                smartLedIp = getSmartLedIpUseCase()
            }.join()

            if (smartLedIp.isEmpty()) {
                _state.value = ConnectionState.Error(R.string.connection_error_text)
            } else {
                _state.value = ConnectionState.Connected(smartLedIp)
            }
        }
    }

    fun onColorPicked(color: Int) {
        pickedColor = color
    }
}