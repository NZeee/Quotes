package com.nzeeei.quotes.di.module

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.gson.Gson
import com.nzeeei.quotes.data.quotes.repository.QuotesRepository
import com.nzeeei.quotes.data.quotes.repository.QuotesRepositoryImpl
import com.nzeeei.quotes.data.quotes.socket.QuotesSocketWrapper
import com.nzeeei.quotes.data.quotes.socket.QuotesSocketWrapperImpl
import com.nzeeei.quotes.di.provider.OkHttpClientProvider
import com.nzeeei.quotes.entry.AppLauncher
import okhttp3.OkHttpClient
import toothpick.config.Module
import toothpick.ktp.binding.bind

class AppModule(context: Context) : Module() {

    init {
        bind<Context>().toInstance(context)

        bind<AppLauncher>().singleton()

        bind<OkHttpClient>().toProvider(OkHttpClientProvider::class).providesSingleton()

        bind<Gson>().toInstance { Gson() }
        bind<QuotesSocketWrapper>().toClass<QuotesSocketWrapperImpl>()
        bind<QuotesRepository>().toClass<QuotesRepositoryImpl>().singleton()

        val cicerone = Cicerone.create()
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.getNavigatorHolder())
    }
}
