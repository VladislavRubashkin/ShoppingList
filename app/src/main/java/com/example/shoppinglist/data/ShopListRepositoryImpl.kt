package com.example.shoppinglist.data

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShopListRepositoryImpl : ShopListRepository {

    private val list = mutableListOf<ShopItemEntity>()
    private var id = 0

    override fun getShopItem(shopItemId: Int): ShopItemEntity {
        return list.find { it.id == shopItemId } ?: notFoundItem(shopItemId)
    }

    override fun addShopItem(shopItem: ShopItemEntity) {
        if (shopItem.id == ShopItemEntity.UNDEFINED_ID)
            shopItem.id = id++
        list.add(shopItem)
    }

    override fun editShopItem(shopItem: ShopItemEntity) {
        for (item in list) {
            if (item.id == shopItem.id) {
                item.copy(name = shopItem.name, count = shopItem.count, enabled = shopItem.enabled)
            }
        }
    }

    override fun deleteShopItem(shopItem: ShopItemEntity) {
        list.remove(shopItem)
    }

    override fun getListShopItem(): Flow<List<ShopItemEntity>> {
        return flow {
            emit(list)
        }
    }

    private fun notFoundItem(shopItemId: Int): Nothing {
        throw RuntimeException("Element with $shopItemId not found")
    }
}