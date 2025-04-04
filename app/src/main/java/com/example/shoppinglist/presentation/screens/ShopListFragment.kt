package com.example.shoppinglist.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopListBinding
import com.example.shoppinglist.domain.entity.ShopItemEntity
import com.example.shoppinglist.presentation.adapter.ShopListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShopListFragment : Fragment() {

    private var _binding: FragmentShopListBinding? = null
    private val binding: FragmentShopListBinding
        get() = _binding ?: throw RuntimeException("FragmentShopListBinding == null")

    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getShopItemList()
        addShopItem()
    }

    private fun getShopItemList() {
        val list = mutableListOf<ShopItemEntity>()
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(1, "name 1", 2.3, true))
        list.add(ShopItemEntity(2, "name 2", 2.3, false))
        list.add(ShopItemEntity(2, "name 2", 2.3, false))
        shopListAdapter.submitList(list)
    }

    private fun addShopItem() {
        binding.fabAdd.setOnClickListener {
            launchFragment()
        }
    }

    private fun launchFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.activity_container, ShopItemFragment.newInstance())
            .commit()
    }

    private fun initRecyclerView() {
        shopListAdapter = ShopListAdapter()
        with(binding) {
            rcViewShopList.adapter = shopListAdapter
            rcViewShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED, ShopListAdapter.RECYCLER_VIEW_POOL
            )
            rcViewShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED, ShopListAdapter.RECYCLER_VIEW_POOL
            )
        }

        setupClickListener()
        setupLongClickListener()
        setupSwipeListener(binding.rcViewShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            println(it.toString())
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            println("Long click $it")
        }
    }

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
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                println("Swipe $item")
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListFragment()
    }
}