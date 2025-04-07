package com.example.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.entity.ShopItemEntity
import kotlinx.coroutines.flow.Flow

interface ShopListRepository {

    fun getShopItem(shopItemId: Int): ShopItemEntity

    fun addShopItem(shopItem: ShopItemEntity)

    fun editShopItem(shopItem: ShopItemEntity)

    fun deleteShopItem(shopItem: ShopItemEntity)

    fun getListShopItem(): LiveData<List<ShopItemEntity>>
}