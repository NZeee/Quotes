package com.nzeeei.quotes.entry

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.nzeeei.quotes.di.Scopes
import com.nzeeei.quotes.di.module.AppModule
import com.nzeeei.quotes.di.module.RoomModule
import timber.log.Timber
import toothpick.configuration.Configuration
import toothpick.ktp.KTP

class QuotesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initToothpick()
        initAppScope()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // TODO()
        }
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            KTP.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            KTP.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initAppScope() {
        KTP.openScope(Scopes.APP_SCOPE).installModules(
            AppModule(this),
            RoomModule(this)
        )
    }
}
