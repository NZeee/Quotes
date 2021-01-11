package com.nzeeei.quotes.presentation.quotes

import com.github.terrakok.cicerone.Router
import com.nzeeei.quotes.core.presentation.BasePresenter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class QuotesPresenter @Inject constructor(
    private val interactor: QuotesInteractor,
    private val router: Router
) : BasePresenter<QuotesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        io {
            interactor.observeSubscribedQuotes().collect { quotes ->
                val viewItems = listOf(QuotesViewItem.QuotesHeader)
                    .run {
                        if (quotes.isEmpty()) {
                            plus(QuotesViewItem.QuotesEmpty)
                        } else {
                            plus(quotes.map { QuotesViewItem.QuoteData(it) })
                        }
                    }
                view { showData(viewItems) }
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }
}
