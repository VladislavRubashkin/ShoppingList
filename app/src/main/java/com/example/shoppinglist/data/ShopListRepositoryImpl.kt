package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

/**
TODO#3

Data-слой - работа с данными

Data-слой предоставляет конкретную реализацию репозиторию.
Data-слой зависит от domain-слоя и знает о нём всё, НО НЕ НАОБОРОТ

Класс делается object(так как он Singleton) - это нужно чтобы не получилось что на одном экране мы работаем с одним
репозиторием, а на другом экране с другим репозиторием.
 */

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    /**
    TODO#3.4

    При добавлении элемента в коллекцию назначаем ему id(условно самоинкрементирующийся).
    Проверяем был ли уже установлен элементу id и если не был устанавливаем(необходимо для того чтобы при
    редактировании элемента, ему не назначалось новое id.

     */
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    /**
    TODO#3.3

    Находим старый элемент, удаляем его, вставляем новый элемент.
     */
    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    /**
    TODO#3.2

    Метод find() - Возвращает первый элемент, соответствующий заданному predicate , или null , если такой элемент
    не найден.
     */
    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    /**
    TODO#3.1

    Возвращать саму коллекцию неправильно так как мы сможем из других мест программы добавлять элементы в коллекцию
    или удалять их. Лучше вернуть копию этой коллекции.
     */
    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

}