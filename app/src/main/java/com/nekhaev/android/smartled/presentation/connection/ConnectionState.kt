package com.nekhaev.android.smartled.presentation.connection

sealed class ConnectionState(
    val errorTextId: Int? = null
) {
    class Connected: ConnectionState()
    class Error( errorId: Int) : ConnectionState(errorId)
    class Loading : ConnectionState()
    class NoConnection : ConnectionState()
}