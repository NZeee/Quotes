package com.nzeeei.quotes.core.presentation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import moxy.MvpPresenter
import moxy.MvpView
import timber.log.Timber
import java.util.*

abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    private val subscriptions = LinkedList<ReceiveChannel<*>>()
    private val supervisorJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + supervisorJob)
    private val ioScope = CoroutineScope(Dispatchers.IO + supervisorJob)
    private val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    override fun onDestroy() {
        super.onDestroy()
        supervisorJob.cancelChildren()
        subscriptions.forEach {
            it.cancel()
        }
    }

    protected fun ui(block: suspend CoroutineScope.() -> Unit): Job =
        uiScope.launch(handler) { block(this) }

    protected fun io(block: suspend CoroutineScope.() -> Unit): Job =
        ioScope.launch(handler) { block(this) }

    protected fun view(block: suspend V.() -> Unit) =
        ui { block(viewState) }
}
