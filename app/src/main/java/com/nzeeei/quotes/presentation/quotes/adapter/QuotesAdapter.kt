package com.nzeeei.quotes.presentation.quotes.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.nzeeei.quotes.databinding.ItemQuoteBinding
import com.nzeeei.quotes.databinding.ItemQuotesEmptyBinding
import com.nzeeei.quotes.databinding.ItemQuotesHeaderBinding
import com.nzeeei.quotes.presentation.quotes.QuotesViewItem
import javax.inject.Inject

fun quoteAdapterDelegate() = adapterDelegateViewBinding<QuotesViewItem.QuoteData, QuotesViewItem, ItemQuoteBinding>(
    { layoutInflater, root -> ItemQuoteBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.apply {
            quoteName.text = item.data.displayName
            bidAsk.text = listOfNotNull(item.data.bid, item.data.ask)
                .joinToString("/")
                .takeIf { it.isNotEmpty() }
                ?: "-"
            spread.text = item.data.spread ?: "-"
        }
    }
}

fun quotesHeaderAdapterDelegate() =
    adapterDelegateViewBinding<QuotesViewItem.QuotesHeader, QuotesViewItem, ItemQuotesHeaderBinding>(
        { layoutInflater, root -> ItemQuotesHeaderBinding.inflate(layoutInflater, root, false) }
    ) {}

fun quotesEmptyAdapterDelegate() =
    adapterDelegateViewBinding<QuotesViewItem.QuotesEmpty, QuotesViewItem, ItemQuotesEmptyBinding>(
        { layoutInflater, root -> ItemQuotesEmptyBinding.inflate(layoutInflater, root, false) }
    ) {}

class QuotesAdapter @Inject constructor() : ListDelegationAdapter<List<QuotesViewItem>>(
    quotesHeaderAdapterDelegate(),
    quotesEmptyAdapterDelegate(),
    quoteAdapterDelegate()
)
