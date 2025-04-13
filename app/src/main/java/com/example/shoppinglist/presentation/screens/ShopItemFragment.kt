package com.example.shoppinglist.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.presentation.constans.Constants
import com.example.shoppinglist.presentation.viewmodels.ShopItemViewModel
import com.example.shoppinglist.presentation.viewmodels.ViewModelFactory
import com.google.android.material.internal.TextWatcherAdapter

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private val args by navArgs<ShopItemFragmentArgs>()

    private val shopItemViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(requireActivity().application))[ShopItemViewModel::class.java]
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
        shopItemViewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        binding.saveButton.setOnClickListener {
            shopItemViewModel.editShopItem(binding.etName.text.toString(), binding.etCount.text.toString())
        }
    }

    private fun observeViewModel() {
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            val errorInputName = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = errorInputName
        }

        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
            val errorInputCount = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = errorInputCount
        }

        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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