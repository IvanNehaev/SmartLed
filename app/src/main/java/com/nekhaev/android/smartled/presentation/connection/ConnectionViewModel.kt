package com.nekhaev.android.smartled.presentation.connection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConnectionViewModel : ViewModel() {

    private val _state = MutableLiveData<ConnectionState>(ConnectionState.NoConnection())
    val state: LiveData<ConnectionState> = _state

    fun onButtonConnectClick() {
        viewModelScope.launch {
            _state.value = ConnectionState.Loading()
            delay(5000)
            _state.value = ConnectionState.Connected()
        }
    }
}