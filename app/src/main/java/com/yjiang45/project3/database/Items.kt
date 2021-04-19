package com.yjiang45.project3.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "item_table")
data class Items(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    var uuid: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "item_name")
    var name: String = "",


    @ColumnInfo(name = "checked")
    var checked: Boolean = false,

    @ColumnInfo(name = "category")
    var category: String = ""
){
    val photoFileName
        get() = "IMG_$uuid.jpg"
}