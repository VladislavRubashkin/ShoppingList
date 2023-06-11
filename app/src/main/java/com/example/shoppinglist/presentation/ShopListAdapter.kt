package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffUtilCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    /**
    TODO #7

    Чтобы устанавливать viewHolder-ам разные разметки переопределяем метод getItemViewType().
    В нём в зависимости от условия возвращаем какое-то значение, оно приходит в аргументы метода
    onCreateViewHolder() и здесь в зависимости от этого значения устанавливаем нужную разметку.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout.toString().toInt(), parent, false)
        return ShopItemViewHolder(view)
    }

    /**
    TODO #6

    В RecyclerView несколько элементов(ViewHolder) напр 15. Десять видны на экране ещё несколько сверху и снизу.
    При скроле списка элементы пере используются. Если мы делаем в методе onBindViewHolder() какое-то действие
    при выполнении условия, необходимо добавлять блок else, если это условие не выполнится.
    Иначе при пере использовании элементов в RecyclerView будут баги.

    Или переопределить метод onViewRecycled()
     */
    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1
        const val MAX_POOL_SIZE = 10
    }
}