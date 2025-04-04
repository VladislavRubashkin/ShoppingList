package com.example.shoppinglist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglist.domain.entity.ShopItemEntity

class ShopListDiffUtilCallback: DiffUtil.ItemCallback<ShopItemEntity>() {
    override fun areItemsTheSame(oldItem: ShopItemEntity, newItem: ShopItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItemEntity, newItem: ShopItemEntity): Boolean {
        return oldItem == newItem
    }

}