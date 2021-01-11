package com.nzeeei.quotes.presentation.subscriptions

import android.os.Bundle
import android.view.View
import com.nzeeei.quotes.core.presentation.BaseFragment
import com.nzeeei.quotes.databinding.FragmentSubscriptionsBinding
import com.nzeeei.quotes.presentation.subscriptions.adapter.SubscriptionsAdapter
import toothpick.Scope
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

class SubscriptionsFragment : BaseFragment<FragmentSubscriptionsBinding>(), SubscriptionsView {

    private val presenter: SubscriptionsPresenter by injectPresenter()
    private val adapter: SubscriptionsAdapter by inject()

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(module {
            bind<SubscriptionsInteractor>().toClass<SubscriptionsInteractorImpl>()
            bind<(String, Boolean) -> Unit>().toInstance { quoteId, checked ->
                presenter.onQuoteCheckChanged(quoteId, checked)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            subscriptionsList.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun showData(data: List<SubscriptionsViewItem>) {
        adapter.items = data
        adapter.notifyDataSetChanged()
    }
}
