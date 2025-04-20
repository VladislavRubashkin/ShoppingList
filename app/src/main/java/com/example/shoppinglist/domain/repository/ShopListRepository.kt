package com.example.shoppinglist.domain.repository

import com.example.shoppinglist.domain.entity.ShopItemEntity
import kotlinx.coroutines.flow.Flow

interface ShopListRepository {

    suspend fun getShopItem(shopItemId: Int): ShopItemEntity

    suspend fun addShopItem(shopItem: ShopItemEntity)

    suspend fun editShopItem(shopItem: ShopItemEntity)

    suspend fun deleteShopItem(shopItem: ShopItemEntity)

    val listShopItem: Flow<List<ShopItemEntity>>
}