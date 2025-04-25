package com.example.shoppinglist.presentation.viewmodels

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.usecase.AddShopItemUseCase
import com.example.shoppinglist.domain.usecase.EditShopItemUseCase
import com.example.shoppinglist.domain.usecase.GetShopItemUseCase
import com.example.shoppinglist.presentation.statescreen.StateShopItemFragment
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
class ShopItemViewModelTest {

    private val getShopItemUseCase: GetShopItemUseCase = mockk()
    private val addShopItemUseCase: AddShopItemUseCase = mockk()
    private val editShopItemUseCase: EditShopItemUseCase = mockk()

    private lateinit var testViewModel: ShopItemViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        testViewModel = ShopItemViewModel(
            getShopItemUseCase = getShopItemUseCase,
            addShopItemUseCase = addShopItemUseCase,
            editShopItemUseCase = editShopItemUseCase
        )
    }

    @AfterEach
    fun tirDown() {
        Dispatchers.resetMain()
        clearMocks(getShopItemUseCase, addShopItemUseCase, editShopItemUseCase)
    }

    @Test
    fun `initial state should be Initial`() = runTest {
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.Initial, actualState)
    }

    @Test
    fun `after call getShopItem() and ended loading state should be Result`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val itemId = TEST_SHOP_ITEM_ID
        coEvery { getShopItemUseCase(shopItemId = itemId) } returns testItem

        testViewModel.getShopItem(shopItemId = itemId)
        advanceUntilIdle()
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.Result(testItem), actualState)
    }

    @Test
    fun `getShopItem() should return shopItem with correct item on success`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val itemId = TEST_SHOP_ITEM_ID
        coEvery { getShopItemUseCase(shopItemId = itemId) } returns testItem

        testViewModel.getShopItem(shopItemId = itemId)
        advanceUntilIdle()
        val expectedItem =
            (testViewModel.state.value as? StateShopItemFragment.Result)?.shopItemEntity

        Assertions.assertEquals(testItem, expectedItem)
    }

    @Test
    fun `getShopItem() should call use case with correct ID`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val itemId = TEST_SHOP_ITEM_ID
        coEvery { getShopItemUseCase(shopItemId = itemId) } returns testItem

        testViewModel.getShopItem(shopItemId = itemId)
        advanceUntilIdle()

        coVerify(exactly = 1) { getShopItemUseCase(shopItemId = itemId) }
    }

    @Test
    fun `addShopItem() should not proceed with field name is blank`() = runTest {
        val blankName = INVALID_NAME
        val validCount = VALID_COUNT

        testViewModel.addShopItem(inputName = blankName, inputCount = validCount)

        coVerify(exactly = 0) { addShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `in addShopItem() if field name is blank when state is ErrorInputName`() = runTest {
        val blankName = INVALID_NAME
        val validCount = VALID_COUNT

        testViewModel.addShopItem(inputName = blankName, inputCount = validCount)
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ErrorInputName(isError = true), actualState)
    }

    @Test
    fun `addShopItem() should not proceed with field count is blank`() = runTest {
        val validName = VALID_NAME
        val blankCount = INVALID_COUNT

        testViewModel.addShopItem(inputName = validName, inputCount = blankCount)

        coVerify(exactly = 0) { addShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `in addShopItem() if field count is blank when state is ErrorInputCount`() = runTest {
        val validName = VALID_NAME
        val blankCount = INVALID_COUNT

        testViewModel.addShopItem(inputName = validName, inputCount = blankCount)
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ErrorInputCount(isError = true), actualState)
    }

    @Test
    fun `addShopItem() should proceed with valid input`() = runTest {
        val validName = VALID_NAME
        val validCount = VALID_COUNT
        coEvery { addShopItemUseCase(shopItem = any()) } just runs

        testViewModel.addShopItem(inputName = validName, inputCount = validCount)
        advanceUntilIdle()

        coVerify(exactly = 1) { addShopItemUseCase(shopItem = any()) }
    }

//    @Test
//    fun `addShopItem() should set Loading state during operation`() = runTest {
//        val validName = VALID_NAME
//        val validCount = VALID_COUNT
//        coEvery { addShopItemUseCase(shopItem = any()) } just runs
//
//        testViewModel.addShopItem(inputName = validName, inputCount = validCount)
//        val actualState = testViewModel.state.value
//
//
//        Assertions.assertEquals(StateShopItemFragment.Loading, actualState)
//    }

    @Test
    fun `addShopItem() should set ShouldCloseScreen state after operation`() = runTest {
        val validName = VALID_NAME
        val validCount = VALID_COUNT
        coEvery { addShopItemUseCase(shopItem = any()) } just runs

        testViewModel.addShopItem(inputName = validName, inputCount = validCount)
        advanceUntilIdle()
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ShouldCloseScreen, actualState)
    }

    @ParameterizedTest
    @ValueSource(strings = ["1", "1.0", "0.1", "100", "1.05"])
    fun `addShopItem() should accept valid count values`(count: String) = runTest {
        val validName = VALID_NAME
        coEvery { addShopItemUseCase(shopItem = any()) } just runs

        testViewModel.addShopItem(inputName = validName, inputCount = count)
        advanceUntilIdle()

        coVerify { addShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `addShopItem() should create item with enabled true`() = runTest {
        val validName = VALID_NAME
        val validCount = VALID_COUNT
        val slot = slot<ShopItemEntity>()
        coEvery { addShopItemUseCase(capture(slot)) } just runs

        testViewModel.addShopItem(inputName = validName, inputCount = validCount)
        advanceUntilIdle()

        Assertions.assertTrue(slot.captured.enabled == true)
    }

    @Test
    fun `editShopItem() should not proceed with field name is blank`() = runTest {
        val itemId = TEST_SHOP_ITEM_ID
        val blankName = INVALID_NAME
        val validCount = VALID_COUNT

        testViewModel.editShopItem(inputName = blankName, inputCount = validCount, shopItemId = itemId)

        coVerify(exactly = 0) { editShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `in editShopItem() if field name is blank when state is ErrorInputName`() = runTest {
        val itemId = TEST_SHOP_ITEM_ID
        val blankName = INVALID_NAME
        val validCount = VALID_COUNT

        testViewModel.editShopItem(inputName = blankName, inputCount = validCount, shopItemId = itemId)
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ErrorInputName(isError = true), actualState)
    }

    @Test
    fun `editShopItem() should not proceed with field count is blank`() = runTest {
        val itemId = TEST_SHOP_ITEM_ID
        val validName = VALID_NAME
        val blankCount = INVALID_COUNT

        testViewModel.editShopItem(inputName = validName, inputCount = blankCount, shopItemId = itemId)

        coVerify(exactly = 0) { editShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `in editShopItem() if field count is blank when state is ErrorInputCount`() = runTest {
        val itemId = TEST_SHOP_ITEM_ID
        val validName = VALID_NAME
        val blankCount = INVALID_COUNT

        testViewModel.editShopItem(inputName = validName, inputCount = blankCount, shopItemId = itemId)
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ErrorInputCount(isError = true), actualState)
    }

    @Test
    fun `editShopItem() should proceed with valid input`() = runTest {
        val validName = VALID_NAME
        val validCount = VALID_COUNT
        val testItem = TEST_SHOP_ITEM_ENTITY
        val itemId = TEST_SHOP_ITEM_ID
        coEvery { getShopItemUseCase(shopItemId = any()) } returns testItem
        coEvery { editShopItemUseCase(shopItem = any()) } just runs

        testViewModel.editShopItem(inputName = validName, inputCount = validCount, shopItemId = itemId)
        advanceUntilIdle()

        coVerify(exactly = 1) { editShopItemUseCase(shopItem = any()) }
    }

//    @Test
//    fun `editShopItem() should set Loading state during operation`() = runTest {
//        val testItem = TEST_SHOP_ITEM_ENTITY
//        val itemId = TEST_SHOP_ITEM_ID
//        val validName = VALID_NAME
//        val validCount = VALID_COUNT
//        coEvery { getShopItemUseCase(shopItemId = any()) } returns testItem
//        coEvery { editShopItemUseCase(shopItem = any()) } just runs
//
//        testViewModel.editShopItem(inputName = validName, inputCount = validCount, shopItemId = itemId)
//        val actualState = testViewModel.state.value
//        advanceUntilIdle()
//
//        Assertions.assertEquals(StateShopItemFragment.Loading, actualState)
//    }

    @Test
    fun `editShopItem() should set ShouldCloseScreen state after operation`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val itemId = TEST_SHOP_ITEM_ID
        val validName = VALID_NAME
        val validCount = VALID_COUNT
        coEvery { getShopItemUseCase(shopItemId = any()) } returns testItem
        coEvery { editShopItemUseCase(shopItem = any()) } just runs

        testViewModel.editShopItem(inputName = validName, inputCount = validCount, shopItemId = itemId)
        advanceUntilIdle()
        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ShouldCloseScreen, actualState)
    }

    @ParameterizedTest
    @ValueSource(strings = ["1", "1.0", "0.1", "100", "1.05"])
    fun `editShopItem() should accept valid count values`(count: String) = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val itemId = TEST_SHOP_ITEM_ID
        val validName = VALID_NAME
        coEvery { getShopItemUseCase(testItem.id) } returns testItem
        coEvery { editShopItemUseCase(any()) } just runs

        testViewModel.editShopItem(inputName = validName, inputCount = count, shopItemId = itemId)
        advanceUntilIdle()

        coVerify { editShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `editShopItem() should create item with enabled true`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val validName = VALID_NAME
        val validCount = VALID_COUNT
        val itemId = TEST_SHOP_ITEM_ID
        val slot = slot<ShopItemEntity>()
        coEvery { getShopItemUseCase(itemId) } returns testItem
        coEvery { editShopItemUseCase(capture(slot)) } just runs

        testViewModel.editShopItem(inputName = validName, inputCount = validCount, shopItemId = itemId)
        advanceUntilIdle()

        Assertions.assertEquals(testItem.enabled, slot.captured.enabled)
        Assertions.assertEquals(testItem.id, slot.captured.id)
        Assertions.assertEquals(validName, slot.captured.name)
        Assertions.assertEquals(validCount.toDouble(), slot.captured.count)
    }

    @Test
    fun `resetErrorInputName should clear name error state`() {
        testViewModel.resetErrorInputName()

        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ErrorInputName(isError = false), actualState)
    }

    @Test
    fun `resetErrorInputCount should clear count error state`() {
        testViewModel.resetErrorInputCount()

        val actualState = testViewModel.state.value

        Assertions.assertEquals(StateShopItemFragment.ErrorInputCount(isError = false), actualState)
    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )
        private const val TEST_SHOP_ITEM_ID = 1
        private const val INVALID_NAME = "   "
        private const val INVALID_COUNT = "   "
        private const val VALID_NAME = "Apple"
        private const val VALID_COUNT = "1.0"
    }
}