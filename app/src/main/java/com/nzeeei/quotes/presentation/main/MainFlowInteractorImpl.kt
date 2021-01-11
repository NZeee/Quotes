package com.nzeeei.quotes.presentation.main

import com.nzeeei.quotes.data.quotes.repository.QuotesRepository
import javax.inject.Inject

class MainFlowInteractorImpl @Inject constructor(
    private val quotesRepository: QuotesRepository
) : MainFlowInteractor {

    override suspend fun connect() {
        quotesRepository.connect()
    }

    override suspend fun disconnect() {
        quotesRepository.disconnect()
    }
}
