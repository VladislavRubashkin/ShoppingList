package com.example.shoppinglist.di

import android.app.Application
import com.example.shoppinglist.presentation.screens.ShopItemFragment
import com.example.shoppinglist.presentation.screens.ShopListFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DomainModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(shopListFragment: ShopListFragment)

    fun inject(shopItemFragment: ShopItemFragment)


    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent

    }
}