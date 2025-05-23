package com.example.shoppinglist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.usecase.AddShopItemUseCase
import com.example.shoppinglist.domain.usecase.EditShopItemUseCase
import com.example.shoppinglist.domain.usecase.GetShopItemUseCase
import com.example.shoppinglist.presentation.statescreen.StateShopItemFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase: GetShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow<StateShopItemFragment>(StateShopItemFragment.Initial)
    val state: StateFlow<StateShopItemFragment>
        get() = _state.asStateFlow()


    fun getShopItem(shopItemId: Int) = viewModelScope.launch {
        loading()
        _state.value = StateShopItemFragment.Result(getShopItemUseCase(shopItemId = shopItemId))
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            viewModelScope.launch {
                val shopItem = ShopItemEntity(name = name, count = count, enabled = true)
                addShopItemUseCase(shopItem)
                loading()
                finish()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?, shopItemId: Int) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            viewModelScope.launch {
                loading()
                val oldShopItem = getShopItemUseCase(shopItemId)
                val editShopItem = oldShopItem.copy(name = name, count = count)
                editShopItemUseCase(editShopItem)
                finish()
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Double {
        return try {
            inputCount?.trim()?.toDouble() ?: 0.0
        } catch (_: NumberFormatException) {
            0.0
        }
    }

    private fun validateInput(name: String, count: Double): Boolean {
        var isValid = true
        if (name.isBlank()) {
            isValid = false
            _state.value = StateShopItemFragment.ErrorInputName(isError = true)
        }
        if (count <= 0) {
            isValid = false
            _state.value = StateShopItemFragment.ErrorInputCount(isError = true)
        }
        return isValid
    }

    private fun finish() {
        _state.value = StateShopItemFragment.ShouldCloseScreen
    }

    fun resetErrorInputName() {
        _state.value = StateShopItemFragment.ErrorInputName(isError = false)
    }

    fun resetErrorInputCount() {
        _state.value = StateShopItemFragment.ErrorInputCount(isError = false)
    }

    private fun loading() {
        _state.value = StateShopItemFragment.Loading
    }
}