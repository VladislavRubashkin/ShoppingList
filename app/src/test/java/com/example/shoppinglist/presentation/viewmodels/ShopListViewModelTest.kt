package com.example.shoppinglist.presentation.viewmodels

import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.domain.usecase.DeleteShopItemUseCase
import com.example.shoppinglist.domain.usecase.EditShopItemUseCase
import com.example.shoppinglist.domain.usecase.GetShopListUseCase
import com.example.shoppinglist.presentation.statescreen.StateShopListFragment
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ShopListViewModelTest {

    private val getShopListUseCase: GetShopListUseCase = mockk()
    private val editShopItemUseCase: EditShopItemUseCase = mockk()
    private val deleteShopItemUseCase: DeleteShopItemUseCase = mockk()

    private lateinit var testViewModel: ShopListViewModel
    private val testFlow: Flow<List<ShopItemEntity>> = mockk()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        testViewModel = ShopListViewModel(
            getShopListUseCase = getShopListUseCase,
            editShopItemUseCase = editShopItemUseCase,
            deleteShopItemUseCase = deleteShopItemUseCase
        )
        coEvery  { getShopListUseCase() } returns testFlow
    }

    @AfterEach
    fun tirDown() {
        Dispatchers.resetMain()
        clearMocks(getShopListUseCase, editShopItemUseCase, deleteShopItemUseCase, testFlow)
    }

    @Test
    fun `method deleteShopItem() should invoke deleteShopItemUseCase`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        coEvery { deleteShopItemUseCase(testItem) } just runs

        testViewModel.deleteShopItem(shopItemEntity = testItem)
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteShopItemUseCase(shopItem = testItem) }
    }

    @Test
    fun `method editShopItem() should invoke editShopItemUseCase`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        coEvery { editShopItemUseCase(any()) } just runs

        testViewModel.editShopItem(shopItemEntity = testItem)
        advanceUntilIdle()

        coVerify(exactly = 1) { editShopItemUseCase(shopItem = any()) }
    }

    @Test
    fun `editShopItem should toggle enabled status and update via use case`() = runTest {
        val testItem = TEST_SHOP_ITEM_ENTITY
        val expectedItem = testItem.copy(enabled = false)
        coEvery { editShopItemUseCase(shopItem = expectedItem) } just runs

        testViewModel.editShopItem(shopItemEntity = testItem)
        advanceUntilIdle()

        coVerify(exactly = 1) { editShopItemUseCase(shopItem = expectedItem) }
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        val stateFlow = testViewModel.state

        Assertions.assertTrue(stateFlow.value is StateShopListFragment.Loading)
    }

    @Test
    fun `state should show error when shop list loading fails`() = runTest {
        coEvery { getShopListUseCase() } returns flow { throw Exception() }

        testViewModel.toString()
        advanceUntilIdle()

        Assertions.assertTrue(testViewModel.state.value is StateShopListFragment.Error)
    }

//    @Test
//    fun `state should be result after loading`() = runTest {
//        val testItems = listOf(TEST_SHOP_ITEM_ENTITY)
////        coEvery { getShopListUseCase() } returns flowOf(testItems)
//
//        testViewModel.toString()
//        advanceUntilIdle()
//
//        // Initial state is Loading (verified by state change)
//        Assertions.assertEquals(StateShopListFragment.Result(testItems), testViewModel.state.value)
//    }

    companion object {
        private val TEST_SHOP_ITEM_ENTITY = ShopItemEntity(
            id = 1,
            name = "Apple",
            count = 1.0,
            enabled = true
        )
    }
}