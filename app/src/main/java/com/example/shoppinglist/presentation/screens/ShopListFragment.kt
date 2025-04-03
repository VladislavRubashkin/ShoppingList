package com.example.shoppinglist.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShopListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val test = view.findViewById<FloatingActionButton>(R.id.fab_add)
        test.setOnClickListener {
            launchFrag(ShopItemFragment.newInstance())
        }

    }

    private fun launchFrag(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.activity_container, fragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListFragment()
    }
}