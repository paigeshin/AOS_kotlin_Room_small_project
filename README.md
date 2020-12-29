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