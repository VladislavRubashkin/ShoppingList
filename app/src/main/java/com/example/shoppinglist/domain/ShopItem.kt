package com.example.shoppinglist.domain

/**
    TODO#1

    Domain-слой - бизнес логика

    God-object - объект, который делает всё(напр. Activity внутри которой происходит загрузка из сети, добавление в БД
    и отображение данных) - НЕДОПУСТИМО в чистой архитектуре.

    Неразрывно с чистой архитектурой идут принципы SOLID.
    S - single responsibility(принцип единой ответственности) - класс должен отвечать только за что-то одно.

    В чистой архитектуре - для каждого метода создаётся отдельный класс.
    Каждое действие бизнес логики, которое может вызвать пользователь, называется Interactor(UseCase).
    Interactor(UseCase) - класс, который отвечает за одно действие в бизнес логике.
 */

data class ShopItem(
    val name: String,
    val count: Double,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object{
        const val UNDEFINED_ID = -1
    }
}
