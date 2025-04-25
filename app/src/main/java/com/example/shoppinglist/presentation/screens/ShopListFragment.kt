package com.example.shoppinglist.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.FragmentShopListBinding
import com.example.shoppinglist.presentation.ShoppingListApp
import com.example.shoppinglist.presentation.adapter.ShopListAdapter
import com.example.shoppinglist.presentation.constans.Constants
import com.example.shoppinglist.presentation.statescreen.StateShopListFragment
import com.example.shoppinglist.presentation.viewmodels.ShopListViewModel
import com.example.shoppinglist.presentation.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListFragment : Fragment() {

    private var _binding: FragmentShopListBinding? = null
    private val binding: FragmentShopListBinding
        get() = _binding ?: throw RuntimeException("FragmentShopListBinding == null")

    private lateinit var shopListAdapter: ShopListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val shopListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopListViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as ShoppingListApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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
        lifecycleScope.launch {
            shopListViewModel.state
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collectLatest {
                    when (it) {
                        StateShopListFragment.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }

                        is StateShopListFragment.Result -> {
                            shopListAdapter.submitList(it.listShopItem)
                            binding.pbLoading.visibility = View.GONE
                        }

                        is StateShopListFragment.Error -> {
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    private fun launchShopItemFragment(screenMode: String, shopItemId: Int) {
        findNavController().navigate(
            ShopListFragmentDirections.actionShopListFragmentToShopItemFragment(screenMode, shopItemId)
        )
    }

    private fun addShopItem() {
        binding.fabAdd.setOnClickListener {
            launchShopItemFragment(screenMode = Constants.MODE_ADD, shopItemId = Constants.UNDEFINED_ID)
        }
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
            launchShopItemFragment(screenMode = Constants.MODE_EDIT, shopItemId = it.id)
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
}