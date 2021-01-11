package com.nzeeei.quotes.data.quotes.socket

import com.nzeeei.quotes.data.quotes.model.Quote

sealed class QuotesSocketIncomingMessage {
    data class Ticks(val quotes: List<Quote>) : QuotesSocketIncomingMessage()
    object Invalid : QuotesSocketIncomingMessage()
}
