package com.nzeeei.quotes.di.module

import android.content.Context
import androidx.room.Room
import com.nzeeei.quotes.BuildConfig
import com.nzeeei.quotes.data.common.db.QuotesDatabase
import com.nzeeei.quotes.data.quotes.db.QuoteSubscriptionDao
import toothpick.config.Module
import toothpick.ktp.binding.bind


class RoomModule(context: Context) : Module() {

    companion object {
        private const val DEFAULT_DATABASE_NAME = "database"
    }

    init {
        val db = Room.databaseBuilder(context, QuotesDatabase::class.java, DEFAULT_DATABASE_NAME)
            .apply {
                if (BuildConfig.DEBUG) {
                    fallbackToDestructiveMigration()
                }
            }
            .build()

        bind<QuotesDatabase>().toInstance(db)
        bind<QuoteSubscriptionDao>().toInstance(db.quoteSubscriptionDao())
    }
}
