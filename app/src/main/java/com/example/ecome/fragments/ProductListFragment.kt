package com.example.ecome.fragments

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecome.adapters.ProductAdapter
import com.example.ecome.models.Product

/**
 * This class is refactored to be a simple utility to configure a RecyclerView
 * with the correct horizontal layout and product adapter, making it reusable.
 */
class ProductListFragment(
    private val products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) {
    // Expose the configured adapter for direct use in the Activity
    val adapter: ProductAdapter
        get() = ProductAdapter(products, onAddToCart)

    // Helper function to set up a RecyclerView with this content
    fun setupRecyclerView(context: Context, recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }
}