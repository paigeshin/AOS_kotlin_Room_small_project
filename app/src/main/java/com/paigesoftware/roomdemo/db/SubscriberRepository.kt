package com.paigesoftware.roomdemo.db

import androidx.lifecycle.LiveData

class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscribers: LiveData<List<Subscriber>> = dao.getAllSubscribers() //LiveData, use coroutines

    suspend fun insert(subscriber: Subscriber) {
        dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber) {
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber) {
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(subscriber: Subscriber) {
        dao.deleteAll()
    }

}