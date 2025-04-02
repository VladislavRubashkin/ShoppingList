package com.example.shoppinglist.domain.useCases

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import kotlinx.coroutines.flow.Flow

class GetListShopItemUseCase(
    private val repository: ShopListRepository
) {

    operator fun invoke(): Flow<List<ShopItemEntity>> {
        return repository.getListShopItem()
    }
}