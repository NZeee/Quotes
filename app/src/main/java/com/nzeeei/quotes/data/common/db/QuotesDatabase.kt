package com.nzeeei.quotes.data.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nzeeei.quotes.data.quotes.db.QuoteSubscriptionDao
import com.nzeeei.quotes.data.quotes.db.QuoteSubscriptionEntity

@Database(
    entities = [
        QuoteSubscriptionEntity::class,
    ],
    version = 1
)
abstract class QuotesDatabase : RoomDatabase() {
    abstract fun quoteSubscriptionDao(): QuoteSubscriptionDao
}
