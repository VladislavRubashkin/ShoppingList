package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(
    private val repository: ShopListRepository
) {

    operator fun invoke(): Flow<List<ShopItemEntity>> {
        return repository.listShopItem
    }
}