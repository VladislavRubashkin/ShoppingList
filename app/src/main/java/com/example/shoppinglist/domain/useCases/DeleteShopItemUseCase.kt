package com.example.shoppinglist.domain.useCases

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository

class DeleteShopItemUseCase(
    private val repository: ShopListRepository
) {

    suspend operator fun invoke(shopItem: ShopItemEntity) {
        repository.deleteShopItem(shopItem)
    }
}