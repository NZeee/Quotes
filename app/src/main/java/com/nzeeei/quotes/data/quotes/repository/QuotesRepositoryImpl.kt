package com.nzeeei.quotes.data.quotes.repository

import com.nzeeei.quotes.data.common.socket.WsStatusListener
import com.nzeeei.quotes.data.quotes.db.QuoteSubscriptionDao
import com.nzeeei.quotes.data.quotes.mapper.QuoteDisplayNameMapper
import com.nzeeei.quotes.data.quotes.model.Quote
import com.nzeeei.quotes.data.quotes.model.QuoteSubscription
import com.nzeeei.quotes.data.quotes.socket.QuotesSocketIncomingMessage
import com.nzeeei.quotes.data.quotes.socket.QuotesSocketOutgoingMessage
import com.nzeeei.quotes.data.quotes.socket.QuotesSocketWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Response
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class QuotesRepositoryImpl @Inject constructor(
    private val quoteSubscriptionDao: QuoteSubscriptionDao,
    private val quotesSocketWrapper: QuotesSocketWrapper,
    private val quoteDisplayNameMapper: QuoteDisplayNameMapper
) : QuotesRepository {

    private val dataFlow: MutableStateFlow<Map<String, Quote>> = MutableStateFlow(mapOf())

    private val dispatcher: ExecutorCoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val scope = CoroutineScope(dispatcher)
    private val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    override suspend fun connect() {
        quotesSocketWrapper.open(object : WsStatusListener() {
            override fun onOpen(response: Response) {
                scope.launch(handler) {
                    resubscribe()
                }
            }
        }).collect {
            when (it) {
                is QuotesSocketIncomingMessage.Ticks -> {
                    updateValue(it.quotes)
                }
                QuotesSocketIncomingMessage.Invalid -> {
                    // ignore
                }
            }
        }
        resubscribe()
    }

    override suspend fun disconnect() {
        quotesSocketWrapper.close()
    }

    override suspend fun subscribe(quoteId: String) {
        quoteSubscriptionDao.subscribe(quoteId)
        quotesSocketWrapper.sendMessage(QuotesSocketOutgoingMessage.Subscribe(listOf(quoteId)))
    }

    override suspend fun unsubscribe(quoteId: String) {
        quoteSubscriptionDao.unsubscribe(quoteId)
        quotesSocketWrapper.sendMessage(QuotesSocketOutgoingMessage.Unsubscribe(listOf(quoteId)))
    }

    @OptIn(FlowPreview::class)
    override suspend fun observeSubscribedQuotes(): Flow<List<Quote>> = combine(
        quoteSubscriptionDao.observeAllSubscriptions(),
        dataFlow.sample(DATA_THROTTLE_PERIOD_MILLIS)
    ) { subscriptions, data ->
        subscriptions.map {
            data[it.id] ?: Quote(
                id = it.id,
                displayName = quoteDisplayNameMapper.map(it.id)
            )
        }
    }

    override suspend fun getAllSubscriptions(): List<QuoteSubscription> {
        val allSubscriptionsIds = quoteSubscriptionDao.getAllSubscriptions().map { it.id }.toSet()
        return defaultSubscriptions.map {
            QuoteSubscription(
                id = it,
                displayName = quoteDisplayNameMapper.map(it),
                subscribed = allSubscriptionsIds.contains(it)
            )
        }
    }

    private suspend fun resubscribe() {
        val allSubscriptionsIds = quoteSubscriptionDao.getAllSubscriptions().map { it.id }
        quotesSocketWrapper.sendMessage(QuotesSocketOutgoingMessage.Subscribe(allSubscriptionsIds))
    }

    private fun updateValue(quotes: List<Quote>) {
        dataFlow.value = dataFlow.value.plus(quotes.map { it.id to it })
    }

    private companion object {
        const val DATA_THROTTLE_PERIOD_MILLIS = 500L

        val defaultSubscriptions = listOf(
            "BTCUSD",
            "EURUSD",
            "EURGBP",
            "USDJPY",
            "GBPUSD",
            "USDCHF",
            "USDCAD",
            "AUDUSD",
            "EURJPY",
            "EURCHF"
        )
    }
}
