package com.example.shoppinglist.domain.entity

data class ShopItemEntity(
    var id: Int = UNDEFINED_ID,
    val name: String,
    val count: Double,
    val enabled: Boolean
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}