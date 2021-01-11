package com.nzeeei.quotes.data.quotes.socket.response

import com.google.gson.annotations.SerializedName

data class QuoteTicksResponse(
    @SerializedName("ticks") val ticks: List<QuoteTickResponse>?
)
