package com.yjiang45.project3.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

@Database(entities = [Items::class], version = 1)
@TypeConverters(Converters::class)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemsDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        val defaultUUID: String = UUID.randomUUID().toString()
        database.execSQL(
            "ALTER TABLE items_table ADD COLUMN uuid TEXT NOT NULL DEFAULT '$defaultUUID'"
        )
    }
}