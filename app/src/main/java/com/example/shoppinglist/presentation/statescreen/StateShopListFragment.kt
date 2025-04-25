package com.example.shoppinglist.presentation.statescreen

import com.example.shoppinglist.domain.entity.ShopItemEntity

sealed class StateShopListFragment {
    object Loading : StateShopListFragment()
    object Error : StateShopListFragment()
    data class Result(val listShopItem: List<ShopItemEntity>) : StateShopListFragment()
}
