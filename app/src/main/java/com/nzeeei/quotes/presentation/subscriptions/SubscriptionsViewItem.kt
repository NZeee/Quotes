package com.nzeeei.quotes.presentation.subscriptions

import com.nzeeei.quotes.data.quotes.model.QuoteSubscription

sealed class SubscriptionsViewItem {
    data class Subscription(val data: QuoteSubscription) : SubscriptionsViewItem()
}
