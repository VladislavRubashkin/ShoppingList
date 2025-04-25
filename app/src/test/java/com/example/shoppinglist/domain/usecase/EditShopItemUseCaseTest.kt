package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entity.ShopItemEntity
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.coroutines.cancellation.CancellationException

class EditShopItemUseCaseTest {

    private lateinit var testShopListRepository: ShopListRepositoryImpl
    private lateinit var editShopItemUseCase: EditShopItemUseCase

    @BeforeEach
    fun setUp() {
        testShopListRepository = mockk(relaxed = true)
        editShopItemUseCase = EditShopItemUseCase(repository = testShopListRepository)
    }

    @Test
    fun `should add shop item to repository`() = runBlocking {
        val testItem = TEST_SHOP_ITEM_ENTITY

        editShopItemUseCase(shopItem = testItem)

        coVerify(exactly = 1) { testShopListRepository.editShopItem(shopItemEntity = testItem) }
    }

    @Test
    fun `should call the repository with the correct item`() = runBlocking {
        val testItem = TEST_SHOP_ITEM_ENTITY

        editShopItemUseCase(shopItem = testItem)

        coVerify {
            testShopListRepository.editShopItem(
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
            editShopItemUseCase(shopItem = testItemNegativeID)
        } catch (e: IllegalArgumentException) {
        }

        coVerify { testShopListRepository wasNot called }
    }

    @Test
    fun `should throw IllegalArgumentException when item id is negative`() = runTest {
        val testItemNegativeID = TEST_SHOP_ITEM_NEGATIVE_ID

        assertThrows<IllegalArgumentException> { editShopItemUseCase(shopItem = testItemNegativeID) }
    }

    @Test
    fun `invoke should handle concurrent calls correctly`() = runTest {
        val testItem1 = TEST_SHOP_ITEM_ENTITY
        val testItem2 = TEST_SHOP_ITEM_ENTITY_2

        val job1 = launch { editShopItemUseCase(shopItem = testItem1) }
        val job2 = launch { editShopItemUseCase(shopItem = testItem2) }
        joinAll(job1, job2)

        coVerify { testShopListRepository.editShopItem(shopItemEntity = testItem1) }
        coVerify { testShopListRepository.editShopItem(shopItemEntity = testItem2) }
    }

    @Test
    fun `multiple invocations should call repository multiple times`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY

        repeat(3) { editShopItemUseCase(shopItem = testItem) }

        coVerify(exactly = 3) { testShopListRepository.editShopItem(shopItemEntity = testItem) }
    }

    @Test
    fun `invoke should handle non-terminating coroutine gracefully`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        coEvery {
            testShopListRepository.editShopItem(shopItemEntity = testItem)
        } coAnswers { throw CancellationException() }

        assertThrows<CancellationException> { editShopItemUseCase(shopItem = testItem) }
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