package com.nzeeei.quotes.presentation.quotes

import com.nzeeei.quotes.data.quotes.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuotesInteractor {
    suspend fun observeSubscribedQuotes(): Flow<List<Quote>>
}
