package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entity.ShopItemEntity
import io.mockk.called
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteShopItemUseCaseTest {

    private lateinit var testShopListRepository: ShopListRepositoryImpl
    private lateinit var deleteShopItemUseCase: DeleteShopItemUseCase

    @BeforeEach
    fun setUp() {
        testShopListRepository = mockk(relaxed = true)
        deleteShopItemUseCase = DeleteShopItemUseCase(repository = testShopListRepository)
    }

    @Test
    fun `should delete shop item with valid data`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY

        deleteShopItemUseCase(shopItem = testItem)

        coVerify(exactly = 1) { testShopListRepository.deleteShopItem(shopItemEntity = testItem) }
    }

    @Test
    fun `should call the repository with the correct item`() = runBlocking {
        val testItem = TEST_SHOP_ITEM_ENTITY

        deleteShopItemUseCase(shopItem = testItem)

        coVerify {
            testShopListRepository.deleteShopItem(
                shopItemEntity = match {
                    it.id == 1 && it.name == "Apple" && it.count == 1.0 && it.enabled == true
                }
            )
        }
    }

    @Test
    fun `should not call repository if id is negative`() = runTest {
        val testItemNegativeID = TEST_SHOP_ITEM_NEGATIVE_ID

        try {
            deleteShopItemUseCase(shopItem = testItemNegativeID)
        } catch (e: IllegalArgumentException) {
        }

        coVerify { testShopListRepository wasNot called }
    }

    @Test
    fun `should throw IllegalArgumentException when item id is negative`() = runTest {
        val testItemNegativeID = TEST_SHOP_ITEM_NEGATIVE_ID

        assertThrows<IllegalArgumentException> { deleteShopItemUseCase(shopItem = testItemNegativeID) }
    }

    @Test
    fun `should allow concurrent delete calls without error`() = runTest {
        val testItem1 = TEST_SHOP_ITEM_ENTITY
        val testItem2 = TEST_SHOP_ITEM_ENTITY_2

        val job1 = launch { deleteShopItemUseCase(shopItem = testItem1) }
        val job2 = launch { deleteShopItemUseCase(shopItem = testItem2) }
        joinAll(job1, job2)

        coVerify { testShopListRepository.deleteShopItem(shopItemEntity = testItem1) }
        coVerify { testShopListRepository.deleteShopItem(shopItemEntity = testItem2) }
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )

        private val TEST_SHOP_ITEM_ENTITY_2 = ShopItemEntity(
            id = 2,
            name = "Bread",
            count = 1.5,
            enabled = false
        )

        private val TEST_SHOP_ITEM_NEGATIVE_ID = ShopItemEntity(
            id = -1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )
    }
}