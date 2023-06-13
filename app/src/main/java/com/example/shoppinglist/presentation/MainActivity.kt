package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shoppingAdapter: ShopListAdapter
    private lateinit var buttonAddItem: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shoppingAdapter.submitList(it)
        }

        buttonAddItem = findViewById(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
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
            Log.d("onShopItemClickListener", it.toString())
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
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