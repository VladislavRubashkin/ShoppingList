package com.example.shoppinglist.domain.useCases

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository

class GetShopItemUseCase(
    private val repository: ShopListRepository
) {

    suspend operator fun invoke(shopItemId: Int): ShopItemEntity {
        return repository.getShopItem(shopItemId)
    }
}