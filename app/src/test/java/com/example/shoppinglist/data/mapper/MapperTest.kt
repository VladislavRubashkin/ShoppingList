package com.example.shoppinglist.data.mapper

import com.example.shoppinglist.data.database.ShopItemDbModel
import com.example.shoppinglist.domain.entity.ShopItemEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf

class MapperTest {

    private val mapperTest = Mapper()

    @Test
    fun `should map ShopItemEntity with ShopItemDbModel`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY

        val actualShopItem = mapperTest.mapEntityToDbModel(shopItemEntity = testItem)

        assertInstanceOf<ShopItemDbModel>(actualShopItem)
    }

    @Test
    fun `should map ShopItemEntity with ShopItemDbModel without changing the properties`() = runTest {
        val expectedItem = TEST_SHOP_ITEM_ENTITY

        val actualItem = mapperTest.mapEntityToDbModel(shopItemEntity = expectedItem)

        Assertions.assertEquals(expectedItem.id, actualItem.id)
        Assertions.assertEquals(expectedItem.name, actualItem.name)
        Assertions.assertEquals(expectedItem.count, actualItem.count)
        Assertions.assertEquals(expectedItem.enabled, actualItem.enabled)
    }

    @Test
    fun `should map ShopItemDbModel with ShopItemEntity`() = runTest {
        val testItem = TEST_SHOP_ITEM_DB_MODEL

        val actualItem = mapperTest.mapDbModelToEntity(shopItemDbModel = testItem)

        assertInstanceOf<ShopItemEntity>(actualItem)
    }

    @Test
    fun `should map ShopItemDbModel with ShopItemEntity without changing the properties`() = runTest {
        val expectedItem = TEST_SHOP_ITEM_DB_MODEL

        val actualItem = mapperTest.mapDbModelToEntity(shopItemDbModel = expectedItem)

        Assertions.assertEquals(expectedItem.id, actualItem.id)
        Assertions.assertEquals(expectedItem.name, actualItem.name)
        Assertions.assertEquals(expectedItem.count, actualItem.count)
        Assertions.assertEquals(expectedItem.enabled, actualItem.enabled)
    }

    @Test
    fun `should map ListShopItemDbModel with ListShopItemEntity`() = runTest {
        val testListItem = listOf<ShopItemDbModel>(TEST_SHOP_ITEM_DB_MODEL)

        val actualShopItem = mapperTest.mapListDbModelToListEntity(
            list = testListItem
        )

        assertInstanceOf<List<ShopItemEntity>>(actualShopItem)
    }

    @Test
    fun `should map ListShopItemDbModel with ListShopItemEntity without changing the properties`() = runTest {
        val expectedListItem = listOf<ShopItemDbModel>(
            TEST_SHOP_ITEM_DB_MODEL, TEST_SHOP_ITEM_DB_MODEL)

        val actualListItem = mapperTest.mapListDbModelToListEntity(
            list = expectedListItem
        )

        Assertions.assertEquals(expectedListItem[0].id, actualListItem[0].id)
        Assertions.assertEquals(expectedListItem[0].name, actualListItem[0].name)
        Assertions.assertEquals(expectedListItem[0].count, actualListItem[0].count)
        Assertions.assertEquals(expectedListItem[0].enabled, actualListItem[0].enabled)
        Assertions.assertEquals(expectedListItem[1].id, actualListItem[1].id)
        Assertions.assertEquals(expectedListItem[1].name, actualListItem[1].name)
        Assertions.assertEquals(expectedListItem[1].count, actualListItem[1].count)
        Assertions.assertEquals(expectedListItem[1].enabled, actualListItem[1].enabled)
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )

        private val TEST_SHOP_ITEM_DB_MODEL = ShopItemDbModel(
            id = 2,
            name = "Bread",
            count = 1.5,
            enabled = false
        )
    }
}