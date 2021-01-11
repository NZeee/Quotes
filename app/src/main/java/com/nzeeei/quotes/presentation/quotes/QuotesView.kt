package com.nzeeei.quotes.presentation.quotes

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface QuotesView : MvpView {
    @AddToEndSingle
    fun showData(data: List<QuotesViewItem>)
}
