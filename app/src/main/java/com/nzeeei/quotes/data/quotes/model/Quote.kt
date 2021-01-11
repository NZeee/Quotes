package com.nzeeei.quotes.data.quotes.model

data class Quote(
    val id: String,
    val displayName: String,
    val ask: String? = null,
    val bid: String? = null,
    val spread: String? = null
)
