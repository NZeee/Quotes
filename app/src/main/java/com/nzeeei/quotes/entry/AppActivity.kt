package com.nzeeei.quotes.entry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.nzeeei.quotes.R
import com.nzeeei.quotes.core.presentation.BaseFragment
import com.nzeeei.quotes.di.Scopes
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class AppActivity : AppCompatActivity() {

    private val navigatorHolder: NavigatorHolder by inject()
    private val navigator: Navigator = object : AppNavigator(this, R.id.container, supportFragmentManager) {}

    private val appLauncher: AppLauncher by inject()

    private val currentFragment: BaseFragment<*>?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment<*>

    private fun injectDependencies() {
        KTP.openScope(Scopes.APP_SCOPE).openSubScope(Scopes.ACTIVITY_SCOPE).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        if (savedInstanceState == null) {
            appLauncher.firstLaunch()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }
}
