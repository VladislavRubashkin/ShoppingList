package com.example.shoppinglist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.useCases.DeleteShopItemUseCase
import com.example.shoppinglist.domain.useCases.EditShopItemUseCase
import com.example.shoppinglist.domain.useCases.GetShopListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
) : ViewModel() {

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