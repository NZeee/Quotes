package com.nzeeei.quotes.presentation.quotes

import com.nzeeei.quotes.data.quotes.model.Quote
import com.nzeeei.quotes.data.quotes.repository.QuotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuotesInteractorImpl @Inject constructor(
    private val quotesRepository: QuotesRepository
) : QuotesInteractor {

    override suspend fun observeSubscribedQuotes(): Flow<List<Quote>> = quotesRepository.observeSubscribedQuotes()
}
