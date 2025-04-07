package com.example.shoppinglist.domain.useCases

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import kotlinx.coroutines.flow.Flow

class GetShopListUseCase(
    private val repository: ShopListRepository
) {

    operator fun invoke(): LiveData<List<ShopItemEntity>> {
        return repository.getListShopItem()
    }
}