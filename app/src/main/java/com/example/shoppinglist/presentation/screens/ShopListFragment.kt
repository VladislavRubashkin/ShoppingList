package com.example.shoppinglist.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopListBinding
import com.example.shoppinglist.presentation.adapter.ShopListAdapter
import com.example.shoppinglist.presentation.viewmodels.ShopListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopListFragment : Fragment() {

    private var _binding: FragmentShopListBinding? = null
    private val binding: FragmentShopListBinding
        get() = _binding ?: throw RuntimeException("FragmentShopListBinding == null")

    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var shopListAdapter: ShopListAdapter
    private val shopListViewModel by lazy {
        ViewModelProvider(this)[ShopListViewModel::class.java]
    }

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
        shopListViewModel.shopList.observe(viewLifecycleOwner) {
            shopListAdapter.submitList(it)
        }
//        scope.launch {
//            shopListViewModel.shopList.collect {
////                Log.d("TagTag", "$it")
//                shopListAdapter.submitList(it)
//            }
//        }
    }

    private fun addShopItem() {
        binding.fabAdd.setOnClickListener {
            launchFragment(ShopItemFragment.newInstanceAddShopItem())
        }
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.activity_container, fragment)
            .addToBackStack(null)
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
            launchFragment(ShopItemFragment.newInstanceEditShopItem(it.id))
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            shopListViewModel.editShopItem(it)
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
                shopListViewModel.deleteShopItem(item)
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