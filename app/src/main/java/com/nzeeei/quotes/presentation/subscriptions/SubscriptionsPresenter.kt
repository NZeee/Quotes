package com.nzeeei.quotes.presentation.subscriptions

import com.github.terrakok.cicerone.Router
import com.nzeeei.quotes.core.presentation.BasePresenter
import kotlinx.coroutines.Job
import javax.inject.Inject

class SubscriptionsPresenter @Inject constructor(
    private val interactor: SubscriptionsInteractor,
    private val router: Router
) : BasePresenter<SubscriptionsView>() {

    private var loadSubscriptionsJob: Job? = null

    fun onResume() {
        loadSubscriptions()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadSubscriptionsJob?.cancel()
    }

    fun onBackPressed() {
        router.exit()
    }

    private fun loadSubscriptions() {
        if (loadSubscriptionsJob?.isActive == true) return
        loadSubscriptionsJob = io {
            val subscriptions = interactor.getAllSubscriptions()
            val viewItems = subscriptions.map { SubscriptionsViewItem.Subscription(it) }
            view { showData(viewItems) }
        }
    }

    fun onQuoteCheckChanged(quoteId: String, checked: Boolean) {
        io {
            if (checked) {
                interactor.subscribe(quoteId)
            } else {
                interactor.unsubscribe(quoteId)
            }
        }
    }
}
