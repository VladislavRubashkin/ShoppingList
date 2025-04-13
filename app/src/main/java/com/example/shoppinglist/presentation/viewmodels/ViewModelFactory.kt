package com.example.shoppinglist.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopListViewModel::class.java)) {
            return ShopListViewModel(application) as T
        } else if (modelClass.isAssignableFrom(ShopItemViewModel::class.java)) {
            return ShopItemViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}