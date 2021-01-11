package com.nzeeei.quotes.data.quotes.socket

sealed class QuotesSocketOutgoingMessage {
    class Subscribe(val quotes: List<String>) : QuotesSocketOutgoingMessage()
    class Unsubscribe(val quotes: List<String>) : QuotesSocketOutgoingMessage()
}
