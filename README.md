# Entity - table

```kotlin
package com.paigesoftware.roomdemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data_table")
data class Subscriber (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subscriber_id")
    val id: Int,

    @ColumnInfo(name = "subscriber_name")
    val name: String,

    @ColumnInfo(name = "subscriber_email")
    val email: String

)
```

# Dao - Data Access Object

```kotlin
package com.paigesoftware.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber)

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber)

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>

}
```

⇒ Never use `MutableLiveData` when creating DAO

# Room Database Class

```kotlin
package com.paigesoftware.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase: RoomDatabase() {

    abstract val subscriberDAO: SubscriberDAO

    companion object {
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null

        fun getInstance(context: Context): SubscriberDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"
                    ).build()
                }
                return instance
            }
        }

    }

}
```

# Repository - MVVM

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5324c2cd-e104-48ef-9714-48ef598c387a/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5324c2cd-e104-48ef-9714-48ef598c387a/Untitled.png)

- repository

```kotlin
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
```

- ViewModel

```kotlin
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
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

}
```

- XML with data binding

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <!-- Declared ViewModel on layout -->
        <variable
            name="subscriberViewModel"
            type="com.paigesoftware.roomdemo.db.SubscriberViewModel"
            />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="15dp"
        android:orientation="vertical"
        tools:context=".MainActivity" >

        <!-- Two way Data Binding applied -->
        <EditText
            android:id="@+id/name_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="15dp"
            android:layout_marginBottom="5dp"
            android:ems="10"
            android:text="@={subscriberViewModel.inputName}"
            android:hint="Subscriber's name"
            android:inputType="textPersonName"
            android:textStyle="bold" />

        <!-- Two way Data Binding applied -->
        <EditText
            android:id="@+id/email_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="15dp"
            android:layout_marginTop="5dp"
            android:layout_marginBottom="5dp"
            android:ems="10"
            android:text="@={subscriberViewModel.inputEmail}"
            android:hint="Subscriber's email"
            android:inputType="textPersonName"
            android:textStyle="bold" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <!-- Two way Data Binding applied -->
            <Button
                android:id="@+id/save_or_update_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="@={subscriberViewModel.saveOrUpdateButtonText}"
                android:onClick="@{()->subscriberViewModel.saveOrUpdate()}"
                android:textSize="18sp"
                android:textStyle="bold" />

            <!-- Two way Data Binding applied -->
            <Button
                android:id="@+id/clear_all_or_delete_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="@={subscriberViewModel.clearAllOrDeleteButtonText}"
                android:onClick="@{()->subscriberViewModel.clearAllOrDelete()}"
                android:textSize="18sp"
                android:textStyle="bold" />

        </LinearLayout>

        <androidx.recyclerview.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/subscriber_recyclerView"
            android:layout_margin="5dp"
            />
    </LinearLayout>
</layout>
```

- ViewModelFactory

```kotlin
package com.paigesoftware.roomdemo.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SubscriberViewModelFactory(private val repository: SubscriberRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubscriberViewModel::class.java)) {
            return SubscriberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}
```

- MainActivity

```kotlin
package com.paigesoftware.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.paigesoftware.roomdemo.databinding.ActivityMainBinding
import com.paigesoftware.roomdemo.db.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao: SubscriberDAO = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.subscriberViewModel = subscriberViewModel
        binding.lifecycleOwner = this  //for LiveData Binding

    }

    private fun displaySubscribersList() {
        subscriberViewModel.subscribers.observe(this, Observer {

        })
    }

}
```