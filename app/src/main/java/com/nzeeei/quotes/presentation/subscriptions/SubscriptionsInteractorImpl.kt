package com.nzeeei.quotes.presentation.subscriptions

import com.nzeeei.quotes.data.quotes.model.QuoteSubscription
import com.nzeeei.quotes.data.quotes.repository.QuotesRepository
import javax.inject.Inject

class SubscriptionsInteractorImpl @Inject constructor(
    private val quotesRepository: QuotesRepository
) : SubscriptionsInteractor {

    override suspend fun subscribe(quoteId: String) {
        quotesRepository.subscribe(quoteId)
    }

    override suspend fun unsubscribe(quoteId: String) {
        quotesRepository.unsubscribe(quoteId)
    }

    override suspend fun getAllSubscriptions(): List<QuoteSubscription> =
        quotesRepository.getAllSubscriptions()
}
