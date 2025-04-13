package com.example.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.entity.ShopItemEntity

interface ShopListRepository {

    suspend fun getShopItem(shopItemId: Int): ShopItemEntity

    suspend fun addShopItem(shopItem: ShopItemEntity)

    suspend fun editShopItem(shopItem: ShopItemEntity)

    suspend fun deleteShopItem(shopItem: ShopItemEntity)

    fun getListShopItem(): LiveData<List<ShopItemEntity>>
}