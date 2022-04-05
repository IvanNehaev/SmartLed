package com.nekhaev.android.smartled.presentation.connection

sealed class ConnectionState(
    val errorTextId: Int? = null,
    val smartLedAddress: String = ""
) {
    class Connected(address: String): ConnectionState(smartLedAddress = address)
    class Error( errorId: Int) : ConnectionState(errorId)
    class Loading : ConnectionState()
    class NoConnection : ConnectionState()
}