package com.example.shoppinglist.domain

/**
    TODO#2

    Domain-слой не от чего не зависит, он не должен знать с чем он работает(напр. БД, сеть и пр).
    Для этих целей используется репозиторий - это просто "черный ящик" который умеет работать с данными.
    Domain-слой работает ТОЛЬКО с интерфейсом репозитория.
    Этот интерфейс передаётся в конструктор useCase, чтобы иметь возможность реализовывать его методы.
 */

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

    fun getShopList() : List<ShopItem>
}