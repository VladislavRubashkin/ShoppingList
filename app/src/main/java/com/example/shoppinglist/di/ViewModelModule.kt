package com.example.shoppinglist.di

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.presentation.viewmodels.ShopItemViewModel
import com.example.shoppinglist.presentation.viewmodels.ShopListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShopListViewModel::class)
    fun bindShopListViewModel(shopListViewModel: ShopListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(shopItemViewModel: ShopItemViewModel): ViewModel
}