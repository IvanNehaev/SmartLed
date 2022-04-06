package com.nekhaev.android.smartled.presentation.control_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekhaev.android.smartled.domain.repository.SmartLedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlPanelViewModel @Inject constructor(
    private val smartLedRepository: SmartLedRepository
) : ViewModel() {

    private var mBrightnessValue: Int = 100

    private val mBrightnessSharedFlow = MutableSharedFlow<Int>()
    val brightnessSharedFlow = mBrightnessSharedFlow.asSharedFlow()

    fun onButtonUpClick() {
        if (mBrightnessValue < 255) {
            mBrightnessValue += 5
            viewModelScope.launch(Dispatchers.IO) {
                smartLedRepository.setBrightness(mBrightnessValue)
            }
        }
    }

    fun onButtonDownClick() {
        if (mBrightnessValue > 0) {
            mBrightnessValue -= 5
            viewModelScope.launch(Dispatchers.IO) {
                smartLedRepository.setBrightness(mBrightnessValue)
            }
        }
    }
}