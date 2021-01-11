package com.nzeeei.quotes.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.nzeeei.quotes.R
import com.nzeeei.quotes.core.presentation.BaseFragment
import com.nzeeei.quotes.databinding.FragmentMainFlowBinding
import com.nzeeei.quotes.presentation.quotes.QuotesFragment
import com.nzeeei.quotes.presentation.subscriptions.SubscriptionsFragment
import toothpick.Scope
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class MainFlowFragment : BaseFragment<FragmentMainFlowBinding>(), MainFlowView {

    private val presenter: MainFlowPresenter by injectPresenter()

    private val currentTabFragment: BaseFragment<*>?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment<*>

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(module {
            bind<MainFlowInteractor>().toClass<MainFlowInteractorImpl>()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomTabs()
        selectTab(currentTabFragment?.tag ?: TAB_QUOTES)
    }

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }

    private fun configureBottomTabs() {
        binding.bottomNavigationView.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.tab_quotes -> selectTab(TAB_QUOTES)
                    R.id.tab_subscriptions -> selectTab(TAB_SUBSCRIPTIONS)
                }
                true
            }
        }
    }

    private fun selectTab(key: String) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(key)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) {
            return
        }

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) {
                add(R.id.container, createTabFragment(key), key)
            }

            currentFragment?.let {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.STARTED)
            }
            newFragment?.let {
                show(it)
                setMaxLifecycle(it, Lifecycle.State.RESUMED)
            }
        }.commitNow()
    }

    private fun createTabFragment(key: String): Fragment = when (key) {
        TAB_QUOTES -> QuotesFragment()
        TAB_SUBSCRIPTIONS -> SubscriptionsFragment()
        else -> throw NotImplementedError()
    }

    companion object {
        private const val TAB_QUOTES = "tab_quotes"
        private const val TAB_SUBSCRIPTIONS = "tab_subscriptions"
    }
}
