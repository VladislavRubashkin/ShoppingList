package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    /**
    TODO#15
    Объект, передачи ошибки ввода имени.
    Так как доступа к activity здесь нет а логика проверки на ошибку здесь, создаётся объект LiveData и в случае
    ошибки передаёт на activity какое-то значение, activity подписывается на эту LiveData и получает это значение
    и в нужный момент показывает ошибку.

    !!!!! Необходимо чтобы когда мы обращаемся к этому объекту из activity мы НЕ ИМЕЛИ возможности записывать в него
    значения, а только подписаться на него и получать значения. А во ViewModel была бы возможность и записывать в
    него значения и подписываться на него.
    С мутабельной и приватной переменной работаем внутри ViewModel, а с имутабельной и открытой переменной работаем
    из activity(её геттер возвращает значение, которое мы положили в мутабельную переменную).
    !!!!!!!
     */
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    /**
    TODO#15.1
    Объект, передачи ошибки ввода количества.
     */
    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    /**
    TODO#12.1
    Объект, передачи shopItem.
     */
    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    /**
    TODO#16
    После того как мы добавили новый элемент или отредактировали его, необходимо закрыть экран и вернуться на
    предыдущее activity.
    Вызвать в activity метод finish() после методов добавления или редактирования - НЕ ПРАВИЛЬНО.
    Делается через LiveData так как добавления или редактирования вполне могут работать в
    другом потоке и есть вероятность закрыть экран до того как эти методы завершат свою работу.
     */
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    /**
    TODO#12
    Получаем элемент по id. Кладём элемент в LiveData.
     */
    fun getShopItem(shopItemId: Int) {
        val shopItem = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = shopItem
    }

    /**
    TODO#13
    Добавляем элемент.
    Всю валидацию и парсинг надо делать во ViewModel а не в Activity. Activity должна только
    отображать данные и отправлять их во ViewModel.
    Парсим поля, валидируем поля, если валидация успешна - создаём объект

     */
    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }
    }

    /**
    TODO#14
    Редактируем элемент.
    Парсим поля, валидируем поля, если валидация успешна - получаем старый объект из LiveData и
    заменяем у него нужные поля
     */
    fun editShopItem(inputName: String?, inputCount: String?) {
        val newName = parseName(inputName)
        val newCount = parseCount(inputCount)
        val fieldsValid = validateInput(newName, newCount)
        if (fieldsValid) {
            _shopItem.value.let {
                val newShopItem = it?.copy(name = newName, count = newCount)!!
                editShopItemUseCase.editShopItem(newShopItem)
                finishWork()
            }
        }
    }

    /**
    TODO#13.1
    Парсим нулабельный String к не нулабельному String, обрезаем пробелы и если string равен null,
    возвращаем пустую строку
     */
    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    /**
    TODO#13.2
    Парсим нулабельный String к не нулабельному Double, обрезаем пробелы и если string равен null,
    возвращаем 0.0. Если привести строку к числу не получилось - бросаем исключение и возвращаем 0.0.
     */
    private fun parseCount(inputCount: String?): Double {
        return try {
            inputCount?.trim()?.toDouble() ?: 0.0
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    /**
    TODO#13.3
    Валидация. Что имя не пустое а количество больше нуля. В случае если поля не валидны - записываем в LiveData
    определённое значение, а в activity его примем и в зависимости от него покажем ошибку.
     */
    private fun validateInput(name: String, count: Double): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    /**
    TODO#15.2
    Сбрасываем значение об ошибке в поле Name.
    Если в поле ввода отобразилась ошибка и пользователь продолжил вводить данные, то ошибку нужно убрать.
     */
    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    /**
    TODO#15.3
    Сбрасываем значение об ошибке в поле Count.
     */
    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}