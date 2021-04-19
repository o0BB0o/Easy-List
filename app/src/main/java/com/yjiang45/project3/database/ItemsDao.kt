package com.yjiang45.project3.database
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(items:Items)

    @Query("DELETE FROM item_table")
    fun deleteAll()

    @Delete
    fun deleteItem(items: Items)

    @Query("SELECT * FROM item_table LIMIT 1")
    fun getAnyItem(): Array<Items>

    @Query("SELECT * FROM item_table ORDER BY id DESC")
    fun getAllItems(): LiveData<List<Items>>

    @Query("SELECT * FROM item_table WHERE NOT checked")
    fun getUnchecked():LiveData<List<Items>>

    @Update
    fun updateItem(items: Items)
}