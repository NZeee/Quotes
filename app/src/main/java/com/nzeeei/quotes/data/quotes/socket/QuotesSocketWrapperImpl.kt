package com.nzeeei.quotes.data.quotes.socket

import com.google.gson.Gson
import com.nzeeei.quotes.data.common.socket.WsChannel
import com.nzeeei.quotes.data.common.socket.WsStatusListener
import com.nzeeei.quotes.data.quotes.mapper.QuoteTickMapper
import com.nzeeei.quotes.data.quotes.socket.response.QuoteSubscriptionResponse
import com.nzeeei.quotes.data.quotes.socket.response.QuoteTicksResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import javax.inject.Inject

class QuotesSocketWrapperImpl @Inject constructor(
    private val gson: Gson,
    private val okHttpClient: OkHttpClient,
    private val quoteTickMapper: QuoteTickMapper
) : QuotesSocketWrapper {

    private var webSocketChannel: WsChannel? = null

    override fun open(listener: WsStatusListener?): Flow<QuotesSocketIncomingMessage> {
        webSocketChannel?.close()
        WsChannel(okHttpClient, QUOTES_WS_URL, listener).run {
            webSocketChannel = this
            return incomingFlow.map { json ->
                try {
                    // try to parse as subscription response
                    val subscriptionResponse = gson.fromJson(json, QuoteSubscriptionResponse::class.java)
                    if (subscriptionResponse.ticksResponse != null) {
                        val ticks = subscriptionResponse.ticksResponse.ticks.orEmpty().map(quoteTickMapper::map)
                        return@map QuotesSocketIncomingMessage.Ticks(ticks)
                    }

                    // try to parse as ticks response
                    val ticksResponse = gson.fromJson(json, QuoteTicksResponse::class.java)
                    if (ticksResponse.ticks != null) {
                        val ticks = ticksResponse.ticks.orEmpty().map(quoteTickMapper::map)
                        return@map QuotesSocketIncomingMessage.Ticks(ticks)
                    }

                    // fallback result
                    QuotesSocketIncomingMessage.Invalid
                } catch (e: Exception) {
                    QuotesSocketIncomingMessage.Invalid
                }
            }
        }
    }

    override fun close() {
        webSocketChannel?.close()
    }

    override fun sendMessage(message: QuotesSocketOutgoingMessage): Boolean = when (message) {
        is QuotesSocketOutgoingMessage.Subscribe -> {
            val quotesString = message.quotes.joinToString(",")
            webSocketChannel?.send("$SUBSCRIBE_REQUEST$quotesString") ?: false
        }
        is QuotesSocketOutgoingMessage.Unsubscribe -> {
            val quotesString = message.quotes.joinToString(",")
            webSocketChannel?.send("$UNSUBSCRIBE_REQUEST$quotesString") ?: false
        }
    }

    companion object {
        private const val QUOTES_WS_URL = "wss://quotes.eccalls.mobi:18400"
        private const val SUBSCRIBE_REQUEST = "SUBSCRIBE: "
        private const val UNSUBSCRIBE_REQUEST = "UNSUBSCRIBE: "
    }
}
