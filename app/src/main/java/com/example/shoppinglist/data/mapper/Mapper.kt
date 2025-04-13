package com.example.shoppinglist.data.mapper

import com.example.shoppinglist.data.database.ShopItemDbModel
import com.example.shoppinglist.domain.entity.ShopItemEntity
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapEntityToDbModel(shopItemEntity: ShopItemEntity) =
        ShopItemDbModel(
            id = shopItemEntity.id,
            name = shopItemEntity.name,
            count = shopItemEntity.count,
            enabled = shopItemEntity.enabled
        )

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) =
        ShopItemEntity(
            id = shopItemDbModel.id,
            name = shopItemDbModel.name,
            count = shopItemDbModel.count,
            enabled = shopItemDbModel.enabled
        )

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}