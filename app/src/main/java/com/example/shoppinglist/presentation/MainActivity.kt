package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shoppingAdapter: ShopListAdapter
    private lateinit var buttonAddItem: FloatingActionButton
    private var shopItemContainer: FragmentContainerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shopItemContainer = findViewById(R.id.shop_item_frag_container)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shoppingAdapter.submitList(it)
        }

        buttonAddItem = findViewById(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }

    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    /**
    TODO #20
    Есть две разметки, одна предназначена для вертикальной ориентации, другая для горизонтальной.
    На вертикальной разметке элемента shopItemContainer НЕТ, соответственно его значение = null.
    На горизонтальной этот контейнер есть соответственно он будет проинициализирован в onCreate().
    Соответственно определяем ориентацию экрана исходя из значения этого элемента.
     */
    private fun isOnePaneMode(): Boolean {
        return shopItemContainer == null
    }

    /**
    TODO #21
    Запускаем фрагмент в контейнере shop_item_frag_container.
    addToBackStack() - добавляем фрагмент в backstack, чтобы при клике на кнопку "save" приложение не закрывалось.
    Так как мы находимся на первой активити и при клике на кнопку "save" вызывается метод либо editShopItem() либо
    addShopItem() из ShopItemViewModel, в которых вызывается метод finishWork() в котором onBackPressed() и текущая
    activity закрывается.
    popBackStack() - удаляет последний фрагмент из backstack.
     */
    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.shop_item_frag_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
    TODO #8
    recyclerView.recycledViewPool.setMaxRecycledViews() - устанавливаем вручную максимальное количество элементов
    в пуле recyclerView, для каждого типа viewHolder.
    При скроле - на экране видим 10 элементов, но есть ещё 5 элементов в пуле, чтобы не создавались новые элементы
    вызываем метод ниже.
     */
    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_shop_list)
        shoppingAdapter = ShopListAdapter()
        with(recyclerView) {
            adapter = shoppingAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED, ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED, ShopListAdapter.MAX_POOL_SIZE
            )
        }

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(recyclerView)
    }

    /**
    TODO #9
    Удаление элемента из списка с помощью свайпа вправо или влево.
     */
    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shoppingAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
    TODO #10
    Слушатель клика по элементу. Редактирование элемента.
     */
    private fun setupClickListener() {
        shoppingAdapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    /**
    TODO #11
    Слушатель долгого клика по элементу. Смена состояние элемента. Enabled or disabled.
     */
    private fun setupLongClickListener() {
        shoppingAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}