package com.nzeeei.quotes.data.common.socket

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


class WsChannel(
    private val okHttpClient: OkHttpClient,
    private val wsUrl: String,
    private val wsStatusListener: WsStatusListener? = null
) {

    private var socket: WebSocket? = null
    private val incoming = Channel<String>()
    private val listener = WebSocketChannelListener(incoming)

    private var request: Request? = null

    @get:Synchronized
    @set:Synchronized
    private var currentStatus: Int = WsStatus.DISCONNECTED

    private var isManualClose = false

    private var lock: Lock = ReentrantLock()
    private val wsMainHandler: Handler = Handler(Looper.getMainLooper())
    private var reconnectCount = 0

    private val reconnectRunnable = java.lang.Runnable {
        buildConnect()
    }

    val incomingFlow: Flow<String> = incoming.consumeAsFlow()

    init {
        buildConnect()
    }

    fun close() {
        isManualClose = true
        disconnect()
    }

    fun send(request: String): Boolean {
        var isSend = false
        if (this.socket != null && currentStatus == WsStatus.CONNECTED) {
            isSend = socket?.send(request) == true
            if (!isSend) {
                tryReconnect()
            }
        }
        return isSend
    }

    private fun initWebSocket() {
        val request = request ?: Request.Builder()
            .url(wsUrl)
            .build()
            .also {
                this.request = it
            }
        okHttpClient.dispatcher.cancelAll()
        try {
            lock.lockInterruptibly()
            try {
                okHttpClient.newWebSocket(request, listener)
            } finally {
                lock.unlock()
            }
        } catch (e: InterruptedException) {
        }
    }

    private fun tryReconnect() {
        if (isManualClose) {
            return
        }
        currentStatus = WsStatus.RECONNECT
        val delay = (reconnectCount * RECONNECT_INTERVAL).coerceAtMost(RECONNECT_MAX_TIME)
        wsMainHandler.postDelayed(reconnectRunnable, delay)
        reconnectCount++
    }

    private fun cancelReconnect() {
        wsMainHandler.removeCallbacks(reconnectRunnable)
        reconnectCount = 0
    }

    private fun connected() {
        cancelReconnect()
    }

    private fun disconnect() {
        if (currentStatus == WsStatus.DISCONNECTED) {
            return
        }
        cancelReconnect()
        okHttpClient.dispatcher.cancelAll()
        currentStatus = WsStatus.DISCONNECTED
        socket = null
    }

    @Synchronized
    private fun buildConnect() {
        when (currentStatus) {
            WsStatus.CONNECTED, WsStatus.CONNECTING -> {
            }
            else -> {
                currentStatus = WsStatus.CONNECTING
                initWebSocket()
            }
        }
    }

    inner class WebSocketChannelListener(
        private val incoming: Channel<String>
    ) : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            socket = webSocket
            currentStatus = WsStatus.CONNECTED
            connected()
            wsStatusListener?.onOpen(response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            incoming.offer(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            incoming.offer(bytes.toString())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            wsStatusListener?.onClosing(code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            wsStatusListener?.onClosed(code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            wsStatusListener?.onFailure(t, response)
            tryReconnect()
        }
    }

    companion object {
        private val RECONNECT_INTERVAL = TimeUnit.SECONDS.toMillis(3)
        private val RECONNECT_MAX_TIME = TimeUnit.SECONDS.toMillis(120)
    }
}
