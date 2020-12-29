package com.paigesoftware.roomdemo.db

import androidx.lifecycle.LiveData

class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscribers: LiveData<List<Subscriber>> = dao.getAllSubscribers() //LiveData, use coroutines

    suspend fun insert(subscriber: Subscriber): Long {
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber): Int {
        return dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber): Int {
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }

}