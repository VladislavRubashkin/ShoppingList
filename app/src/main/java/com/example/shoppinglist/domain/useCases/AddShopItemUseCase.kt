package com.example.shoppinglist.domain.useCases

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {

    suspend operator fun invoke(shopItem: ShopItemEntity) {
        repository.addShopItem(shopItem)
    }
}