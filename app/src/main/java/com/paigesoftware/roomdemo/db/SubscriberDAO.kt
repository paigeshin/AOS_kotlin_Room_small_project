package com.paigesoftware.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber): Long //by default, returns `long` id, or -1

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int //by default, returns `int` number of rows updated, or 0

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int //by default, returns `int` number of rows deleted, or 0

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll(): Int //by default, returns `int` number of rows deleted, or 0

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>

}