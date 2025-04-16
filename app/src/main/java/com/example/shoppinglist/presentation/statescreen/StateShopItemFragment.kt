package com.example.shoppinglist.presentation.statescreen

import com.example.shoppinglist.domain.entity.ShopItemEntity

sealed class StateShopItemFragment {
    data class Result(val shopItemEntity: ShopItemEntity) : StateShopItemFragment()
    data class ErrorInputName(val isError: Boolean) : StateShopItemFragment()
    data class ErrorInputCount(val isError: Boolean) : StateShopItemFragment()
    object ShouldCloseScreen : StateShopItemFragment()
    object Loading : StateShopItemFragment()
}