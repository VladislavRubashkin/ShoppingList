package com.example.shoppinglist.data.repository

import com.example.shoppinglist.data.database.ShopItemDbModel
import com.example.shoppinglist.data.database.ShopListDao
import com.example.shoppinglist.data.mapper.Mapper
import com.example.shoppinglist.domain.entity.ShopItemEntity
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShopListRepositoryImplTest {

    private lateinit var shopListDao: ShopListDao
    private lateinit var mapper: Mapper
    private lateinit var testShopListRepository: ShopListRepositoryImpl

    @BeforeEach
    fun setUp() {
        shopListDao = mockk()
        mapper = mockk()
        testShopListRepository = ShopListRepositoryImpl(shopListDao, mapper, CoroutineScope(Dispatchers.Default))
    }

    @AfterEach
    fun tearDown() {
        clearMocks(shopListDao, mapper)
    }

    @Test
    fun `method getShopItem() should return mapped ShopItemEntity when ShopItemDbModel exists`() = runTest {
        val itemId = TEST_SHOP_ITEM_ID
        val shopItemDbModel = TEST_SHOP_ITEM_DB_MODEL
        val expectedShopItemEntity = TEST_SHOP_ITEM_ENTITY
        coEvery { shopListDao.getShopItem(shopItemId = itemId) } returns shopItemDbModel
        every { mapper.mapDbModelToEntity(shopItemDbModel = shopItemDbModel) } returns expectedShopItemEntity

        val actualShopItemEntity = testShopListRepository.getShopItem(shopItemId = itemId)

        Assertions.assertEquals(expectedShopItemEntity, actualShopItemEntity)
        coVerify(exactly = 1) { shopListDao.getShopItem(shopItemId = itemId) }
        verify(exactly = 1) { mapper.mapDbModelToEntity(shopItemDbModel = shopItemDbModel) }
    }

    @Test
    fun `method addShopItem() calls shopListDao with mapped item`() = runTest {
        val shopItemEntity = TEST_SHOP_ITEM_ENTITY
        val shopItemDbModel = TEST_SHOP_ITEM_DB_MODEL
        coEvery { shopListDao.addShopItem(shopItemDbModel = shopItemDbModel) } just runs
        every { mapper.mapEntityToDbModel(shopItemEntity = shopItemEntity) } returns shopItemDbModel

        testShopListRepository.addShopItem(shopItemEntity = shopItemEntity)

        coVerify(exactly = 1) { shopListDao.addShopItem(shopItemDbModel = shopItemDbModel) }
        verify(exactly = 1) { mapper.mapEntityToDbModel(shopItemEntity = shopItemEntity) }
    }

    @Test
    fun `method editShopItem() calls shopListDao with mapped item`() = runTest {
        val shopItemEntity = TEST_SHOP_ITEM_ENTITY
        val shopItemDbModel = TEST_SHOP_ITEM_DB_MODEL
        coEvery { shopListDao.editShopItem(shopItemDbModel = shopItemDbModel) } just runs
        every { mapper.mapEntityToDbModel(shopItemEntity = shopItemEntity) } returns shopItemDbModel

        testShopListRepository.editShopItem(shopItemEntity = shopItemEntity)

        verify(exactly = 1) { mapper.mapEntityToDbModel(shopItemEntity = shopItemEntity) }
        coVerify(exactly = 1) { shopListDao.editShopItem(shopItemDbModel = shopItemDbModel) }
    }

    @Test
    fun `method deleteShopItem() calls shopListDao with mapped item`() = runTest {
        val itemId = TEST_SHOP_ITEM_ID
        val shopItemEntity = TEST_SHOP_ITEM_ENTITY
        coEvery { shopListDao.deleteShopItem(shopItemId = itemId) } just runs

        testShopListRepository.deleteShopItem(shopItemEntity = shopItemEntity)

        coVerify(exactly = 1) { shopListDao.deleteShopItem(shopItemId = itemId) }
    }

    @Test
    fun `should return mapped shop items when shop list is retrieved from dao`() = runTest {
        val dbModelList = listOf(TEST_SHOP_ITEM_DB_MODEL)
        val expectedEntityList = listOf(TEST_SHOP_ITEM_ENTITY)
        coEvery { shopListDao.getShopList() } returns flow { emit(dbModelList) }
        every { mapper.mapListDbModelToListEntity(list = dbModelList) } returns expectedEntityList

        val actualFlow = testShopListRepository.listShopItem
        val actualResult = actualFlow.first()

        Assertions.assertEquals(expectedEntityList, actualResult)
        coVerify(exactly = 1) { shopListDao.getShopList() }
        coVerify(exactly = 1) { mapper.mapListDbModelToListEntity(list = dbModelList) }
    }

    @Test
    fun `should handle empty list case gracefully`() = runTest {
        val emptyDbModelList = emptyList<ShopItemDbModel>()
        val emptyEntityList = emptyList<ShopItemEntity>()
        coEvery { shopListDao.getShopList() } returns flow { emit(emptyDbModelList) }
        every { mapper.mapListDbModelToListEntity(list = emptyDbModelList) } returns emptyEntityList

        val resultFlow = testShopListRepository.listShopItem
        val actualResult = resultFlow.first()

        Assertions.assertEquals(emptyList<ShopItemEntity>(), actualResult)
        coVerify(exactly = 1) { shopListDao.getShopList() }
        coVerify(exactly = 1) { mapper.mapListDbModelToListEntity(list = emptyDbModelList) }
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )

        private val TEST_SHOP_ITEM_DB_MODEL = ShopItemDbModel(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )

        private const val TEST_SHOP_ITEM_ID = 1
    }
}