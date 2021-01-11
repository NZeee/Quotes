package com.nzeeei.quotes.data.quotes.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteSubscriptionDao {
    @Query("SELECT * FROM quote_subscription ORDER BY id")
    fun observeAllSubscriptions(): Flow<List<QuoteSubscriptionEntity>>

    @Query("SELECT * FROM quote_subscription")
    suspend fun getAllSubscriptions(): List<QuoteSubscriptionEntity>

    @Transaction
    suspend fun subscribe(quoteId: String) {
        if (getSubscription(quoteId) == null) {
            insert(QuoteSubscriptionEntity(quoteId))
        }
    }

    @Query("DELETE FROM quote_subscription WHERE id = :quoteId")
    suspend fun unsubscribe(quoteId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuoteSubscriptionEntity)

    @Query("SELECT * FROM quote_subscription WHERE id = :quoteId")
    suspend fun getSubscription(quoteId: String): QuoteSubscriptionEntity?
}
