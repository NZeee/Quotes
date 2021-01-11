package com.nzeeei.quotes.data.quotes.socket

import com.nzeeei.quotes.data.common.socket.WsStatusListener
import kotlinx.coroutines.flow.Flow

interface QuotesSocketWrapper {
    fun open(listener: WsStatusListener?): Flow<QuotesSocketIncomingMessage>
    fun close()
    fun sendMessage(message: QuotesSocketOutgoingMessage): Boolean
}
