package com.example.shoppinglist.presentation.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityContainerBinding
/*
TODO:
 1. Add animation navigation back ShopItemFragment
 2. Replace TextWatcher with TextWatcherAdapter()
 3. Add Settings Activity
 4. LiveData replace Flow
 5. Add Dependency Injection
 6. Add Data base
 */
class ContainerActivity : AppCompatActivity() {

    private val binding: ActivityContainerBinding by lazy {
        ActivityContainerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        launchFragment()
    }

    private fun launchFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_container, ShopListFragment.newInstance())
            .commit()
    }
}