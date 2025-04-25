package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entity.ShopItemEntity
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetShopListUseCaseTest {

    private lateinit var testShopListRepository: ShopListRepositoryImpl
    private lateinit var getShopListUseCase: GetShopListUseCase

    @BeforeEach
    fun setUp() {
        testShopListRepository = mockk()
        getShopListUseCase = GetShopListUseCase(repository = testShopListRepository)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(testShopListRepository)
    }

    @Test
    fun `invoke should return correct data in flow from repository`() = runTest {
        val expectedListShopItem = listOf(TEST_SHOP_ITEM_ENTITY, TEST_SHOP_ITEM_ENTITY_2)
        val expectedFlow: Flow<List<ShopItemEntity>> = flow { emit(expectedListShopItem) }
        coEvery  { testShopListRepository.listShopItem } returns expectedFlow

        val actualListShopItem = getShopListUseCase().toList()

        Assertions.assertEquals(expectedListShopItem, actualListShopItem.first())
        coVerify { testShopListRepository.listShopItem }
    }

    @Test
    fun `invoke should handle multiple emissions from repository`() = runTest {
        val expectedList1 = listOf(TEST_SHOP_ITEM_ENTITY)
        val expectedList2 = listOf(TEST_SHOP_ITEM_ENTITY_2)
        val flow: Flow<List<ShopItemEntity>> = flow {
            emit(expectedList1)
            emit(expectedList2)
        }

        coEvery { testShopListRepository.listShopItem } returns flow
        val actualLists = getShopListUseCase().toList()

        Assertions.assertEquals(expectedList1, actualLists[0])
        Assertions.assertEquals(expectedList2, actualLists[1])
    }

    @Test
    fun `invoke should not throw when repository emits an error`() = runTest {
        val flow: Flow<List<ShopItemEntity>> = flow { throw Exception("Error") }
        coEvery { testShopListRepository.listShopItem } returns flow

        val result = runCatching { getShopListUseCase().toList() }

        assert(result.isFailure)
    }

    @Test
    fun `invoke should call repository listShopItem exactly once`() = runTest {
        val flow: Flow<List<ShopItemEntity>> = flow { emit(emptyList()) }
        coEvery { testShopListRepository.listShopItem } returns flow

        getShopListUseCase.invoke()

        coVerify(exactly = 1) { testShopListRepository.listShopItem }
    }

    @Test
    fun `should not interact with repository when use case not invoked`() {
        // Assert that no interactions occur
        coVerify (exactly = 0) { testShopListRepository.listShopItem }
    }

    @Test
    fun `invoke should return empty list when repository returns empty flow`() = runTest {
        val expectedList = emptyList<ShopItemEntity>()
        val flow: Flow<List<ShopItemEntity>> = flow { emit(expectedList) }
        coEvery { testShopListRepository.listShopItem } returns flow

        val actualList = getShopListUseCase().toList()

        Assertions.assertEquals(expectedList, actualList.first())
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
    }
}