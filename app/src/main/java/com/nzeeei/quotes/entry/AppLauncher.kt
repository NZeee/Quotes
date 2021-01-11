package com.nzeeei.quotes.entry

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nzeeei.quotes.presentation.main.MainFlowFragment
import javax.inject.Inject

class AppLauncher @Inject constructor(
    private val router: Router
) {

    fun firstLaunch() {
        router.newRootScreen(getMainScreen())
    }

    fun getMainScreen() = FragmentScreen { MainFlowFragment() }
}
