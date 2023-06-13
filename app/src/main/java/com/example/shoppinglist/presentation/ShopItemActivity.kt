package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        val id = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, -1)
        Log.d("ShopItemActivity", "${mode.toString()} $id")
    }

    companion object {
        /**
        TODO #17
        Константы делаем приватными.
        Создаём два статических метода, каждый из которых возвращает intent.
        При нажатии на кнопку buttonAddItem -> newIntentAddItem() - открываем экран в режиме добавления элемента.
        При нажатии на элемент списка -> newIntentEditItem() - открываем экран в режиме редактирования элемента,
        помимо режима кладём в intent id элемента.

         */
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}