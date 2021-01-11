package com.nzeeei.quotes.data.common.socket

import okhttp3.Response

abstract class WsStatusListener {
    open fun onOpen(response: Response) {}
    open fun onClosing(code: Int, reason: String) {}
    open fun onClosed(code: Int, reason: String) {}
    open fun onFailure(t: Throwable, response: Response?) {}
}
