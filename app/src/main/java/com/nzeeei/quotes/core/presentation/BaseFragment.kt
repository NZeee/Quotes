package com.nzeeei.quotes.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nzeeei.quotes.core.extension.objectScopeName
import com.nzeeei.quotes.di.Scopes
import moxy.MvpAppCompatFragment
import moxy.MvpPresenter
import moxy.ktx.MoxyKtxDelegate
import moxy.ktx.moxyPresenter
import toothpick.Scope
import toothpick.ktp.KTP
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding> : MvpAppCompatFragment() {

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment<*>)?.fragmentScopeName ?: Scopes.ACTIVITY_SCOPE
    }

    lateinit var fragmentScopeName: String

    lateinit var scope: Scope
        private set

    private var instanceStateSaved: Boolean = false

    protected val binding: VB by viewBinding(getViewBindingClass(), CreateMethod.INFLATE)

    private fun getViewBindingClass(): Class<VB> =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>

    @CallSuper
    protected open fun installModules(scope: Scope) {
    }

    /**
     * Restore scope for current screen. It checks saved state and restore existing scope or open a new one if
     * screen hasn't never saved his state.
     */
    private fun restoreScope(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        val scopeOpen = KTP.isScopeOpen(fragmentScopeName)
        if (scopeOpen) {
            scope = KTP.openScope(fragmentScopeName)
        } else {
            scope = KTP.openScope(parentScopeName).openSubScope(fragmentScopeName)
            installModules(scope)
        }
        scope.inject(this)
        if (!scopeOpen) {
            scopeRestored(scope)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        restoreScope(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    open fun scopeRestored(scope: Scope) {}

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true

        // Save scope name for surviving while configuration changes or any other destruction processes are running
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Close scope if fragment is finally destroying and we need clear all dependencies.
        if (needDestroy()) {
            KTP.closeScope(scope.name)
        }
    }

    open fun onBackPressed() {}

    /**
     * Return true if fragment is real removing, false otherwise. The method checks if fragment didn't save state
     * and it is removing from activity (@see[androidx.fragment.app.Fragment.isRemoving]) or any parent fragment
     * in the fragments hierarchy is removing.
     */
    private fun isRealRemoving(): Boolean = (isRemoving && !instanceStateSaved) ||
            ((parentFragment as? BaseFragment<*>)?.isRealRemoving() ?: false)

    /**
     * Return true if fragment will be destroyed, false otherwise. The method checks activity states first (such as
     * finishing or configuration changing) and then if it's okay check if fragment is real removing from parent.
     */
    private fun needDestroy(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    inline fun <reified T : MvpPresenter<*>> injectPresenter(): MoxyKtxDelegate<T> =
        moxyPresenter { scope.getInstance(T::class.java) }

    companion object {
        private const val STATE_SCOPE_NAME = "state_scope_name"
    }
}
