package com.nzeeei.quotes.data.quotes.socket.response

import com.google.gson.annotations.SerializedName

data class QuoteTickResponse(
    @SerializedName("s") val id: String,
    @SerializedName("b") val bid: String?,
    @SerializedName("a") val ask: String?,
    @SerializedName("spr") val spread: String?
)
