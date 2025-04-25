package com.example.shoppinglist.domain.entity

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ShopItemEntityTest {

    @Test
    fun `newInstance uses UNDEFINED_ID`() {
        val testItem = TEST_SHOP_ITEM_ENTITY

        val expectedShopItemId = ShopItemEntity.UNDEFINED_ID
        val actualShopItemId = testItem.id

        Assertions.assertEquals(expectedShopItemId, actualShopItemId)
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            name = "Apple",
            count = 1.0,
            enabled = true
        )
    }
}