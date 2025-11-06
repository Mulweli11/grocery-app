// File: com.example.ecome/HomeActivity.kt
package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecome.adapters.BannerAdapter
import com.example.ecome.adapters.CategoryAdapter
import com.example.ecome.fragments.ProductListFragment
import com.example.ecome.models.Category
import com.example.ecome.models.Product
import com.example.ecome.utils.CartManager
import com.example.ecome.utils.TranslationManager
import com.google.firebase.firestore.FirebaseFirestore
import me.relex.circleindicator.CircleIndicator3

class HomeActivity : AppCompatActivity() {

    // Views
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var bannerIndicator: CircleIndicator3
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var handler: Handler

    // Banner
    private var bannerPosition = 0

    // Firestore
    private val db = FirebaseFirestore.getInstance()

    // Sample categories
    private val sampleCategories = listOf(
        Category("Fruits", R.drawable.ic_fruits),
        Category("Vegetables", R.drawable.ic_vegetables),
        Category("Breads & Sweets", R.drawable.ic_bakery),
        Category("Beverages", R.drawable.ic_beverages),
        Category("Meat", R.drawable.ic_meat),
        Category("Dairy", R.drawable.dairy1)
    )

    private val categoryViews = mapOf(
        "Dairy" to Pair(R.id.dairyRecyclerView, R.id.dairyLabel),
        "Meat" to Pair(R.id.meatRecyclerView, R.id.meatLabel),
        "Produce" to Pair(R.id.produceRecyclerView, R.id.produceLabel),
        "Beverages" to Pair(R.id.beveragesRecyclerView, R.id.beveragesLabel),
        "Bakery" to Pair(R.id.bakeryRecyclerView, R.id.bakeryLabel),
        "Fruits" to Pair(R.id.fruitsRecyclerView, R.id.fruitsLabel),
        "Snacks" to Pair(R.id.snacksRecyclerView, R.id.snacksLabel),
        "Vegetables" to Pair(R.id.vegetablesRecyclerView, R.id.vegetablesLabel)
    )

    // --- Lambdas ---
    private val onAddToCart: (Product) -> Unit = { product ->
        CartManager.addToCart(product)
        val currentItem = CartManager.cartItems.find { it.product.id == product.id }

        if (currentItem != null) {
            Toast.makeText(
                this,
                "${TranslationManager.getTranslation("added")} 1x ${product.name}. " +
                        "${TranslationManager.getTranslation("total_in_cart")}: ${currentItem.quantity}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "${TranslationManager.getTranslation("failed_add")} ${product.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val onCategoryClick: (Category) -> Unit = { category ->
        Toast.makeText(
            this,
            "${TranslationManager.getTranslation("filtering_by")}: ${category.name}",
            Toast.LENGTH_SHORT
        ).show()
        // TODO: Filter products by category
    }

    // --- Lifecycle ---
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bannerViewPager = findViewById(R.id.bannerViewPager)
        bannerIndicator = findViewById(R.id.bannerIndicator)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
        handler = Handler(Looper.getMainLooper())

        setupBannerSlider()
        setupMenuIcon()
        setupCategoryList()

        // Load content
        loadProductsByCategory("Trending", R.id.trendingRecyclerView, null)
        categoryViews.forEach { (categoryName, ids) ->
            loadProductsByCategory(categoryName, ids.first, ids.second)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh UI text based on selected language
        setupCategoryList()
        setupMenuIcon()
    }

    // --- Setup Functions ---
    private fun setupCategoryList() {
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryAdapter(sampleCategories, onCategoryClick)
        categoryRecyclerView.adapter = adapter

        // Update category labels dynamically
        categoryViews.forEach { (categoryName, ids) ->
            val label = findViewById<TextView>(ids.second)
            label.text = TranslationManager.getTranslation(categoryName.toLowerCase())
        }
    }

    private fun setupMenuIcon() {
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.main_menu, popup.menu)

            // Dynamically update menu item titles
            popup.menu.findItem(R.id.action_cart).title = TranslationManager.getTranslation("cart")
            popup.menu.findItem(R.id.action_account).title = TranslationManager.getTranslation("account")
            popup.menu.findItem(R.id.action_settings).title = TranslationManager.getTranslation("settings")

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_cart -> startActivity(Intent(this, CartActivity::class.java)).let { true }
                    R.id.action_account -> startActivity(Intent(this, AccountActivity::class.java)).let { true }
                    R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java)).let { true }
                    else -> false
                }
            }
            popup.show()
        }
    }

    private fun loadProductsByCategory(categoryName: String, recyclerViewId: Int, labelId: Int?) {
        db.collection("products")
            .whereEqualTo("category", categoryName)
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.apply { id = doc.id }
                }

                if (products.isNotEmpty()) {
                    val recyclerView = findViewById<RecyclerView>(recyclerViewId)
                    labelId?.let { findViewById<TextView>(it).visibility = View.VISIBLE }
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = ProductListFragment(products, onAddToCart).adapter
                } else {
                    labelId?.let { findViewById<TextView>(it).visibility = View.GONE }
                    findViewById<RecyclerView>(recyclerViewId).visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.w("HomeActivity", "Error loading $categoryName products: ", exception)
                Toast.makeText(this, "Failed to load $categoryName.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupBannerSlider() {
        val bannerImages = listOf(R.drawable.slide1, R.drawable.slide2, R.drawable.slide3)
        val adapter = BannerAdapter(bannerImages)
        bannerViewPager.adapter = adapter
        bannerViewPager.post { bannerIndicator.setViewPager(bannerViewPager) }
        autoSlideBanners(bannerImages.size)
    }

    private fun autoSlideBanners(total: Int) {
        val runnable = object : Runnable {
            override fun run() {
                bannerPosition = (bannerPosition + 1) % total
                bannerViewPager.setCurrentItem(bannerPosition, true)
                handler.postDelayed(this, 4000)
            }
        }
        handler.postDelayed(runnable, 4000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
