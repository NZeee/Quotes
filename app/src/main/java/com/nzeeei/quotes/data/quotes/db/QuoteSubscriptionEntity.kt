package com.nzeeei.quotes.data.quotes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote_subscription")
data class QuoteSubscriptionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String
)
