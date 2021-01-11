package com.nzeeei.quotes.data.quotes.repository

import com.nzeeei.quotes.data.quotes.model.Quote
import com.nzeeei.quotes.data.quotes.model.QuoteSubscription
import kotlinx.coroutines.flow.Flow

interface QuotesRepository {
    suspend fun connect()
    suspend fun disconnect()
    suspend fun subscribe(quoteId: String)
    suspend fun unsubscribe(quoteId: String)
    suspend fun observeSubscribedQuotes(): Flow<List<Quote>>
    suspend fun getAllSubscriptions(): List<QuoteSubscription>
}
