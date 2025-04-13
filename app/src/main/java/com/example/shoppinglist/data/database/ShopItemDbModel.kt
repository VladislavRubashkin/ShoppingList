package com.example.shoppinglist.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("count")
    val count: Double,

    @ColumnInfo("enabled")
    val enabled: Boolean
)