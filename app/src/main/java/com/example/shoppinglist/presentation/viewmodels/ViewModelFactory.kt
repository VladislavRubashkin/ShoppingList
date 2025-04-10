package com.example.shoppinglist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopListViewModel::class.java)) {
            return ShopListViewModel() as T
        } else if (modelClass.isAssignableFrom(ShopItemViewModel::class.java)) {
            return ShopItemViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}