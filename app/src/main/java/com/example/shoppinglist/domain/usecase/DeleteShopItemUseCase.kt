package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class DeleteShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {

    suspend operator fun invoke(shopItem: ShopItemEntity) {
        if (shopItem.id < 0) throw IllegalArgumentException()
        repository.deleteShopItem(shopItem)
    }
}