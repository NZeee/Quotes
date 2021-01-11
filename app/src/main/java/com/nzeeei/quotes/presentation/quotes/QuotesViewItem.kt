package com.nzeeei.quotes.presentation.quotes

import com.nzeeei.quotes.data.quotes.model.Quote

sealed class QuotesViewItem {
    object QuotesHeader : QuotesViewItem()
    object QuotesEmpty : QuotesViewItem()
    data class QuoteData(val data: Quote) : QuotesViewItem()
}
