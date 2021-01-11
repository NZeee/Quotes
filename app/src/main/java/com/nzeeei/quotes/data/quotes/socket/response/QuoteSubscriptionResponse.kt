package com.nzeeei.quotes.data.quotes.socket.response

import com.google.gson.annotations.SerializedName

data class QuoteSubscriptionResponse(
    @SerializedName("subscribed_count") val subscribedCount: Int?,
    @SerializedName("subscribed_list") val ticksResponse: QuoteTicksResponse?
)
