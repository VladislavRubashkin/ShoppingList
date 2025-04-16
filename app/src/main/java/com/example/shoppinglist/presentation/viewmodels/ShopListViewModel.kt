package com.example.shoppinglist.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.useCases.DeleteShopItemUseCase
import com.example.shoppinglist.domain.useCases.EditShopItemUseCase
import com.example.shoppinglist.domain.useCases.GetShopListUseCase
import com.example.shoppinglist.presentation.statescreen.StateShopListFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
) : ViewModel() {

    private val _state = getListShopItem()
    val state: LiveData<StateShopListFragment>
        get() = _state


    private fun getListShopItem(): MutableLiveData<StateShopListFragment> =
        MediatorLiveData<StateShopListFragment>().apply {
            addSource(getShopListUseCase()) {
                loading()
                value = StateShopListFragment.Result(it)
            }
        }

    private fun loading() {
        _state.value = StateShopListFragment.Loading
    }

    fun editShopItem(shopItemEntity: ShopItemEntity) {
        viewModelScope.launch {
            loading()
            val newItem = shopItemEntity.copy(enabled = !shopItemEntity.enabled)
            editShopItemUseCase(newItem)
        }
    }

    fun deleteShopItem(shopItemEntity: ShopItemEntity) {
        viewModelScope.launch {
            loading()
            deleteShopItemUseCase(shopItemEntity)
        }
    }
}