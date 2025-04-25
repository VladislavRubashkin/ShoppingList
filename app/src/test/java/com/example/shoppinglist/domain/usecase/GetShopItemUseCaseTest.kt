package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entity.ShopItemEntity
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.assertThrows

class GetShopItemUseCaseTest {

    private lateinit var testShopListRepository: ShopListRepositoryImpl
    private lateinit var getShopItemUseCase: GetShopItemUseCase

    @BeforeEach
    fun setup() {
        testShopListRepository = mockk<ShopListRepositoryImpl>()
        getShopItemUseCase = GetShopItemUseCase(repository = testShopListRepository)
    }

    @Test
    fun `should return ShopItem when it exists`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        coEvery { getShopItemUseCase.invoke(shopItemId = testItem.id) } returns testItem

        val actualShopItem = getShopItemUseCase.invoke(shopItemId = testItem.id)

        Assertions.assertEquals(testItem, actualShopItem)
        coVerify(exactly = 1) { getShopItemUseCase.invoke(shopItemId = testItem.id) }
    }

    @Test
    fun `should return a class instance ShopItemEntity`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY

        coEvery { getShopItemUseCase.invoke(shopItemId = testItem.id) } returns testItem

        assertInstanceOf<ShopItemEntity>(getShopItemUseCase.invoke(shopItemId = testItem.id))
    }

    @Test
    fun `should return item with correct properties`() = runTest {
        val expectedShopItem = TEST_SHOP_ITEM_ENTITY
        coEvery { getShopItemUseCase.invoke(shopItemId = expectedShopItem.id) } returns expectedShopItem

        val actualShopItem = getShopItemUseCase(shopItemId = expectedShopItem.id)

        Assertions.assertEquals(expectedShopItem.id, actualShopItem.id)
        Assertions.assertEquals(expectedShopItem.name, actualShopItem.name)
        Assertions.assertEquals(expectedShopItem.count, actualShopItem.count)
        Assertions.assertEquals(expectedShopItem.enabled, actualShopItem.enabled)
    }

    @Test
    fun `should not call repository if id is negative`() = runTest {
        val testItemNegativeID = NEGATIVE_ID

        try {
            getShopItemUseCase(shopItemId = testItemNegativeID)
        } catch (e: IllegalArgumentException) { }

        coVerify { testShopListRepository wasNot called }
    }

    @Test
    fun `should throw IllegalArgumentException when item id is negative`() = runTest {
        val testItemNegativeID = NEGATIVE_ID

        assertThrows<IllegalArgumentException> { getShopItemUseCase(shopItemId = testItemNegativeID) }
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )

        private const val NEGATIVE_ID = -1
    }
}
