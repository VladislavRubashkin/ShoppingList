package com.example.shoppinglist.domain.useCases

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(
    private val repository: ShopListRepository
) {

    operator fun invoke(): LiveData<List<ShopItemEntity>> {
        return repository.getListShopItem()
    }
}