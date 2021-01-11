package com.nzeeei.quotes.presentation.quotes

import android.os.Bundle
import android.view.View
import com.nzeeei.quotes.core.presentation.BaseFragment
import com.nzeeei.quotes.databinding.FragmentQuotesBinding
import com.nzeeei.quotes.presentation.quotes.adapter.QuotesAdapter
import toothpick.Scope
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

class QuotesFragment : BaseFragment<FragmentQuotesBinding>(), QuotesView {

    private val presenter: QuotesPresenter by injectPresenter()
    private val adapter: QuotesAdapter by inject()

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(module {
            bind<QuotesInteractor>().toClass<QuotesInteractorImpl>()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            quotesList.adapter = adapter
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun showData(data: List<QuotesViewItem>) {
        adapter.items = data
        adapter.notifyDataSetChanged()
    }
}
