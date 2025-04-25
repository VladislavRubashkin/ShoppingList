package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class GetShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {

    suspend operator fun invoke(shopItemId: Int): ShopItemEntity {
        if (shopItemId < 0) throw IllegalArgumentException()
        return repository.getShopItem(shopItemId)
    }
}