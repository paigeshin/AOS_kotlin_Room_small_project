package com.paigesoftware.roomdemo.db

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel(), Observable {

    val subscribers: LiveData<List<Subscriber>> = repository.subscribers

    @Bindable
    val inputName: MutableLiveData<String> = MutableLiveData<String>()

    @Bindable
    val inputEmail: MutableLiveData<String> = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText: MutableLiveData<String> = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText: MutableLiveData<String> = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0, name, email))
        inputName.value = null
        inputEmail.value = null
    }

    fun clearAllOrDelete() {
        clearAll()
    }

    fun insert(subscriber: Subscriber) {
        viewModelScope.launch {
            repository.insert(subscriber)
        }
    }

    //    fun insert(subscriber: Subscriber) = viewModelScope.launch { repository.insert(subscriber)}

    fun update(subscriber: Subscriber) {
        viewModelScope.launch {
            repository.update(subscriber)
        }
    }

    fun delete(subscriber: Subscriber) {
        viewModelScope.launch {
            repository.delete(subscriber)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}
