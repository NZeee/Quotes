package com.nzeeei.quotes.presentation.subscriptions

import com.nzeeei.quotes.data.quotes.model.QuoteSubscription

interface SubscriptionsInteractor {
    suspend fun subscribe(quoteId: String)
    suspend fun unsubscribe(quoteId: String)
    suspend fun getAllSubscriptions(): List<QuoteSubscription>
}
