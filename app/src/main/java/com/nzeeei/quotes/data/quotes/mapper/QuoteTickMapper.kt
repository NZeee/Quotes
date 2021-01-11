package com.nzeeei.quotes.data.quotes.mapper

import com.nzeeei.quotes.core.Mapper
import com.nzeeei.quotes.data.quotes.model.Quote
import com.nzeeei.quotes.data.quotes.socket.response.QuoteTickResponse
import javax.inject.Inject

class QuoteTickMapper @Inject constructor(
    private val quoteDisplayNameMapper: QuoteDisplayNameMapper
) : Mapper<QuoteTickResponse, Quote> {
    override fun map(from: QuoteTickResponse) = Quote(
        id = from.id,
        displayName = quoteDisplayNameMapper.map(from.id),
        bid = from.bid,
        ask = from.ask,
        spread = from.spread
    )
}
