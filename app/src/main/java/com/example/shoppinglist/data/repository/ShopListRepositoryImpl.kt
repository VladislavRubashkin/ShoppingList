package com.example.shoppinglist.data.repository

import com.example.shoppinglist.data.database.ShopListDao
import com.example.shoppinglist.data.mapper.Mapper
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: Mapper,
    coroutineScope: CoroutineScope
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

    override val listShopItem: Flow<List<ShopItemEntity>> = flow {
        shopListDao.getShopList()
            .map {
                mapper.mapListDbModelToListEntity(it)
            }
            .collect {
                emit(it)
            }
    }.shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
}