package com.nzeeei.quotes.presentation.main

interface MainFlowInteractor {
    suspend fun connect()
    suspend fun disconnect()
}
