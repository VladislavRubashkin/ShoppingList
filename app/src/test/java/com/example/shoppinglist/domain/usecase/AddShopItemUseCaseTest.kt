package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entity.ShopItemEntity
import io.mockk.called
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddShopItemUseCaseTest {

    private lateinit var testShopListRepository: ShopListRepositoryImpl
    private lateinit var addShopItemUseCase: AddShopItemUseCase

    @BeforeEach
    fun setUp() {
        testShopListRepository = mockk(relaxed = true)
        addShopItemUseCase = AddShopItemUseCase(testShopListRepository)
    }

    @Test
    fun `should add shop item to repository`() = runBlocking {
        val testItem = TEST_SHOP_ITEM_ENTITY

        addShopItemUseCase(shopItem = testItem)

        coVerify(exactly = 1) { testShopListRepository.addShopItem(shopItemEntity = testItem) }
    }

    @Test
    fun `should call the repository with the correct item`() = runBlocking {
        val testItem = TEST_SHOP_ITEM_ENTITY

        addShopItemUseCase(shopItem = testItem)

        coVerify {
            testShopListRepository.addShopItem(
                shopItemEntity = match {
                    it.id == 1 && it.name == "Apple" && it.count == 1.0 && it.enabled == true
                }
            )
        }
    }

    @Test
    fun `should not call repository if id is negative`() = runTest {
        val testItem = TEST_SHOP_ITEM_NEGATIVE_ID

        try {
            addShopItemUseCase(shopItem = testItem)
        } catch (e: IllegalArgumentException) {
        }

        coVerify { testShopListRepository wasNot called }
    }

    @Test
    fun `should throw IllegalArgumentException when item id is negative`() = runTest {
        val testItem = TEST_SHOP_ITEM_NEGATIVE_ID

        assertThrows<IllegalArgumentException> { addShopItemUseCase(shopItem = testItem) }
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )

        private val TEST_SHOP_ITEM_NEGATIVE_ID = ShopItemEntity(
            id = -1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )
    }
}