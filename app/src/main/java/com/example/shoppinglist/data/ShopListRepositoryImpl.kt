package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
TODO#3

    Data-слой - работа с данными

    Data-слой предоставляет конкретную реализацию репозиторию.
    Data-слой зависит от domain-слоя и знает о нём всё, НО НЕ НАОБОРОТ

    Класс делается object(так как он Singleton) - это нужно чтобы не получилось что на одном экране мы работаем с одним
    репозиторием, а на другом экране с другим репозиторием.
 */

object ShopListRepositoryImpl : ShopListRepository {

    /**
        TODO #5

        Меняем реализацию с mutableListOf() на sortedSetOf() и в конструктор передаём Comparator, для того чтобы
        при смене состояния элемента он не уходил в конец(поскольку в методе editShopItem() мы удаляем старый
        элемент и добавляем новый) , а сортировался по id.
     */
    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id)})
    private var autoIncrementId = 0
    private val shopListLiveData = MutableLiveData<List<ShopItem>>()

    init {
        for (i in 0 until 100){
            val item = ShopItem("Name $i", i.toDouble(),true)
            addShopItem(item)
        }
    }

    /**
    TODO#3.4

    При добавлении элемента в коллекцию назначаем ему id(условно самоинкрементирующийся).
    Проверяем был ли уже установлен элементу id и если не был устанавливаем(необходимо для того чтобы при
    редактировании элемента, ему не назначалось новое id.
     Обновляем MutableLiveData<List<ShopItem>>().
     */
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    /**
    TODO#3.6

    Удаляем элемент из листа.
    Обновляем MutableLiveData<List<ShopItem>>.
     */
    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
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
    TODO#3.5

    Возвращаем MutableLiveData<List<ShopItem>>.
     */
    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    /**
    TODO#3.1

    Кладём в MutableLiveData<List<ShopItem>> лист с ShopItem.
    Возвращать саму коллекцию неправильно так как мы сможем из других мест программы добавлять элементы в коллекцию
    или удалять их. Лучше вернуть копию этой коллекции.
     */
    private fun updateList(){
        shopListLiveData.value = shopList.toList()
    }

}