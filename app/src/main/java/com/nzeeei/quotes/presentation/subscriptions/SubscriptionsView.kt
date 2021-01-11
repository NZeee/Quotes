package com.nzeeei.quotes.presentation.subscriptions

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface SubscriptionsView : MvpView {
    @OneExecution
    fun showData(data: List<SubscriptionsViewItem>)
}
