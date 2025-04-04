package com.example.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.databinding.ShopItemDisabledBinding
import com.example.shoppinglist.databinding.ShopItemEnabledBinding
import com.example.shoppinglist.domain.entity.ShopItemEntity

class ShopListAdapter : ListAdapter<ShopItemEntity, ShopListViewHolder>(ShopListDiffUtilCallback()) {

    var onShopItemClickListener: ((ShopItemEntity) -> Unit)? = null
    var onShopItemLongClickListener: ((ShopItemEntity) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopListViewHolder {
        val binding = when (viewType) {
            VIEW_TYPE_ENABLED -> ShopItemEnabledBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            VIEW_TYPE_DISABLED -> ShopItemDisabledBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ShopListViewHolder,
        position: Int
    ) {
        val shopItem = getItem(position)
        val binding = holder.binding

        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }

        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        when (binding) {
            is ShopItemEnabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }

            is ShopItemDisabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return when {
            shopItem.enabled -> VIEW_TYPE_ENABLED
            else -> VIEW_TYPE_DISABLED
        }
    }

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1
        const val RECYCLER_VIEW_POOL = 10
    }
}