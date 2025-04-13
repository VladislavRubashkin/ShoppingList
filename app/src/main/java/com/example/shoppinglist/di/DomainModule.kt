package com.example.shoppinglist.di

import android.app.Application
import com.example.shoppinglist.data.database.AppDataBase
import com.example.shoppinglist.data.database.ShopListDao
import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.repository.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(shopListRepositoryImpl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideShopListDao(
            application: Application
        ): ShopListDao {
            return AppDataBase.newInstance(application).shopListDao()
        }
    }
}