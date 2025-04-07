package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.repository.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = sortedSetOf<ShopItemEntity>({ o1, o2 -> o1.id.compareTo(o2.id) })
    private val shopListLiveData = MutableLiveData<List<ShopItemEntity>>()
    private var autoIncrementId = 0

    init {
        repeat(15) {
            val item = ShopItemEntity(name = "name $it", count = it.toDouble(), enabled = Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun getShopItem(shopItemId: Int): ShopItemEntity {
        return shopList.find { it.id == shopItemId } ?: notFoundItem(shopItemId)
    }

    override fun addShopItem(shopItem: ShopItemEntity) {
        if (shopItem.id == ShopItemEntity.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItemEntity) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItemEntity) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun getListShopItem(): LiveData<List<ShopItemEntity>> {
        return shopListLiveData
    }

    private fun updateList() {
        shopListLiveData.value = shopList.toList()
    }

    private fun notFoundItem(shopItemId: Int): Nothing {
        throw RuntimeException("Element with $shopItemId not found")
    }
}