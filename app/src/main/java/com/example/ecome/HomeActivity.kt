package com.example.ecome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView






class HomeActivity : AppCompatActivity() {

    // 1. Declare lateinit variables for views (to replace synthetic imports)
    private lateinit var recyclerExclusive: RecyclerView
    private lateinit var recyclerBestSelling: RecyclerView

    // Product lists (using the corrected Product structure)
    private val exclusiveProducts = listOf(
        Product(1, "Organic Bananas", 10.00, "7pcs, Priceg", R.drawable.banana),
        Product(2, "Red Apple", 15.50, "1kg, Priceg", R.drawable.redapple),
        Product(3, "Fresh Grapes", 8.99, "1kg, Priceg", R.drawable.freshgrapes),
        Product(4, "Broccoli Florets", 5.25, "500g, Priceg", R.drawable.broccoliflorets)
    )

    private val bestSellingProducts = listOf(
        Product(5, "Bell Peppers", 12.00, "4pcs, Priceg", R.drawable.bellpeppers),

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 2. Initialize RecyclerViews using findViewById
        recyclerExclusive = findViewById(R.id.recyclerExclusive)
        recyclerBestSelling = findViewById(R.id.recyclerBestSelling)

        // 3. Setup Exclusive Offer Recycler
        recyclerExclusive.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ProductAdapter(exclusiveProducts)
            isNestedScrollingEnabled = false
        }

        // 4. Setup Best Selling Recycler
        recyclerBestSelling.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ProductAdapter(bestSellingProducts)
            isNestedScrollingEnabled = false
        }
    }
}
