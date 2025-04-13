package com.example.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.shoppinglist.data.database.ShopListDao
import com.example.shoppinglist.data.mapper.Mapper
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: Mapper
) : ShopListRepository {

    override suspend fun getShopItem(shopItemId: Int): ShopItemEntity {
        val shopItem = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(shopItem)
    }

    override suspend fun addShopItem(shopItemEntity: ShopItemEntity) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItemEntity))
    }

    override suspend fun editShopItem(shopItemEntity: ShopItemEntity) {
        shopListDao.editShopItem(mapper.mapEntityToDbModel(shopItemEntity))
    }

    override suspend fun deleteShopItem(shopItemEntity: ShopItemEntity) {
        shopListDao.deleteShopItem(shopItemEntity.id)
    }

    override fun getListShopItem(): LiveData<List<ShopItemEntity>> =
        MediatorLiveData<List<ShopItemEntity>>().apply {
            addSource(shopListDao.getShopList()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }
}