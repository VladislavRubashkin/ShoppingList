package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopList() : LiveData<List<ShopItem>>{
        return shopListRepository.getShopList()
    }
}