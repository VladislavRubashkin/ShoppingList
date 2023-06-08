package com.example.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem

/**
    TODO#4

    Presentation слой отвечает за отображение и взаимодействие с пользователем.
    Presentation слой зависит от domain слоя.
    Методы бизнес логики вызываются из UseCase.
    С UseCase(domain-слой) взаимодействует viewModel.
    В чистой архитектуре presentation слой знает всё о domain слое, но ничего о data слое.
    Если во viewModel нужен context то наследуемся от AndroidViewModel(), если не нужен то наследуемся от ViewModel().

    private val repository = ShopListRepositoryImpl - НЕ ПРАВИЛЬНО, presentation слой не должен зависеть от data слоя.
 */

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    /**
        TODO#4.1

        Работаем через MutableLiveData, из activity подписываемся на эту LiveData.
        ВАЖНО: setValue(или просто value) - можно вызывать только из главного потока, postValue - из любого потока.
     */
    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)

    }

    fun changeEnableState(shopItem: ShopItem){
        val newShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newShopItem)
    }
}