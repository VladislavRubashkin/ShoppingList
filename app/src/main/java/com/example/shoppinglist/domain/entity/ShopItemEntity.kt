package com.example.shoppinglist.domain.entity

data class ShopItemEntity(
    val id: Int,
    val name: String,
    val count: Double,
    val enabled: Boolean
)