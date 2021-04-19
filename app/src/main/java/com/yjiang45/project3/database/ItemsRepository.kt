package com.yjiang45.project3.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

class ItemsRepository private constructor(context: Context) {

    private val database: ItemsDatabase = Room.databaseBuilder(
        context.applicationContext,
        ItemsDatabase::class.java,
        "friend_database"
    )
        .addMigrations(migration_1_2)
        .build()

    private val itemsDao = database.itemsDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllItems(): LiveData<List<Items>> = itemsDao.getAllItems()

    fun insert(items: Items) {
        executor.execute {
            itemsDao.insert(items)
        }
    }

    fun deleteItem(items: Items) {
        executor.execute {
            itemsDao.deleteItem(items)
        }
    }

    fun deleteAll(){
        executor.execute {
            itemsDao.deleteAll()
        }
    }

    fun updateItem(items: Items){
        executor.execute {
            itemsDao.updateItem(items)
        }
    }

    fun getUnchecked():LiveData<List<Items>> = itemsDao.getUnchecked()

    companion object {

        private var INSTANCE: ItemsRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ItemsRepository(context)
            }
        }

        fun get(): ItemsRepository {
            return INSTANCE
                ?: throw IllegalStateException("ItemsRepository must be initialized.")
        }
    }
}