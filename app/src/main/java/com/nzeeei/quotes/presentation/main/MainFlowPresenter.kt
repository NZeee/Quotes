package com.nzeeei.quotes.presentation.main

import com.nzeeei.quotes.core.presentation.BasePresenter
import javax.inject.Inject

class MainFlowPresenter @Inject constructor(
    private val interactor: MainFlowInteractor
) : BasePresenter<MainFlowView>() {

    override fun attachView(view: MainFlowView?) {
        super.attachView(view)
        io { interactor.connect() }
    }

    override fun detachView(view: MainFlowView?) {
        super.detachView(view)
        io { interactor.disconnect() }
    }
}
