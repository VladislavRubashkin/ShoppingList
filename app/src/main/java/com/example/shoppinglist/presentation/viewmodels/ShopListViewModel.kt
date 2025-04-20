package com.example.shoppinglist.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.useCases.DeleteShopItemUseCase
import com.example.shoppinglist.domain.useCases.EditShopItemUseCase
import com.example.shoppinglist.domain.useCases.GetShopListUseCase
import com.example.shoppinglist.presentation.statescreen.StateShopListFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow<StateShopListFragment>(StateShopListFragment.Loading)
    val state: StateFlow<StateShopListFragment>
        get() = _state.asStateFlow()


    init {
        loadListShopItem()
    }

    private fun loadListShopItem() {
        viewModelScope.launch {
            getShopListUseCase()
                .onStart {
                    Log.d("TAG", "Start")
                    loading()
                }
                .onCompletion {
                    Log.d("TAG", "Complete") // Не завершается при сворачивании App
                }
                .collectLatest {
                    _state.value = StateShopListFragment.Result(it)
                }
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