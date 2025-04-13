package com.example.shoppinglist.domain.useCases

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class GetShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {

    suspend operator fun invoke(shopItemId: Int): ShopItemEntity {
        return repository.getShopItem(shopItemId)
    }
}