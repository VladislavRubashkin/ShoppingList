package com.example.shoppinglist.presentation.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.presentation.ShoppingListApp
import com.example.shoppinglist.presentation.constans.Constants
import com.example.shoppinglist.presentation.statescreen.StateShopItemFragment
import com.example.shoppinglist.presentation.viewmodels.ShopItemViewModel
import com.example.shoppinglist.presentation.viewmodels.ViewModelFactory
import com.google.android.material.internal.TextWatcherAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private val args by navArgs<ShopItemFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val shopItemViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
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
        _binding = FragmentShopItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchRightMode()
        observeViewModel()
        addChangeListener()
    }

    private fun launchRightMode() {
        when (args.screenMode) {
            Constants.MODE_ADD -> addShopItem()
            Constants.MODE_EDIT -> editShopItem()
        }
    }

    private fun addShopItem() {
        binding.saveButton.setOnClickListener {
            shopItemViewModel.addShopItem(binding.etName.text.toString(), binding.etCount.text.toString())
        }
    }

    private fun editShopItem() {
        shopItemViewModel.getShopItem(args.shopItemId)
        binding.saveButton.setOnClickListener {
            shopItemViewModel.editShopItem(
                binding.etName.text.toString(),
                binding.etCount.text.toString(),
                args.shopItemId
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            shopItemViewModel.state
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .onStart {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                .collectLatest {
                    binding.pbLoading.visibility = View.GONE
                    when (it) {
                        is StateShopItemFragment.ErrorInputCount -> {
                            val errorInputCount = if (it.isError) {
                                getString(R.string.error_input_count)
                            } else {
                                null
                            }
                            binding.tilCount.error = errorInputCount
                        }

                        is StateShopItemFragment.ErrorInputName -> {
                            val errorInputName = if (it.isError) {
                                getString(R.string.error_input_name)
                            } else {
                                null
                            }
                            binding.tilName.error = errorInputName
                        }

                        is StateShopItemFragment.Result -> {
                            binding.etName.setText(it.shopItemEntity.name)
                            binding.etCount.setText(it.shopItemEntity.count.toString())
                        }

                        is StateShopItemFragment.ShouldCloseScreen -> {
                            findNavController().popBackStack()
                        }

                        is StateShopItemFragment.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }

                        is StateShopItemFragment.Initial -> {
                            binding.pbLoading.visibility = View.GONE
                        }
                    }
                }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun addChangeListener() {
        binding.etName.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetErrorInputName()
            }
        })

        binding.etCount.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetErrorInputCount()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}