package com.example.shoppinglist.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.useCases.DeleteShopItemUseCase
import com.example.shoppinglist.domain.useCases.EditShopItemUseCase
import com.example.shoppinglist.domain.useCases.GetShopListUseCase
import kotlinx.coroutines.launch

class ShopListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)


    val shopList = getShopListUseCase()

    fun editShopItem(shopItemEntity: ShopItemEntity) {
        viewModelScope.launch {
            val newItem = shopItemEntity.copy(enabled = !shopItemEntity.enabled)
            editShopItemUseCase(newItem)
        }
    }

    fun deleteShopItem(shopItemEntity: ShopItemEntity) {
        viewModelScope.launch {
            deleteShopItemUseCase(shopItemEntity)
        }
    }


}