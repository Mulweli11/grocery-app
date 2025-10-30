package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ecome.adapters.BannerAdapter
import com.example.ecome.adapters.CategoryAdapter // NEW: Import CategoryAdapter
import com.example.ecome.fragments.ProductListFragment
import com.example.ecome.models.Category // NEW: Import Category Model
import com.example.ecome.models.Product
import com.example.ecome.utils.CartManager
import com.google.firebase.firestore.FirebaseFirestore
import me.relex.circleindicator.CircleIndicator3

class HomeActivity : AppCompatActivity() {

    // --- View Properties ---
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var bannerIndicator: CircleIndicator3
    private lateinit var categoryRecyclerView: RecyclerView // NEW: Category RecyclerView
    private lateinit var handler: Handler

    // --- Data Properties ---
    private var bannerPosition = 0
    private val db = FirebaseFirestore.getInstance()

    // Sample category data (Ensure these drawable resources exist!)
    private val sampleCategories = listOf(
        Category("Fruits", R.drawable.ic_fruits),
        Category("Vegetables", R.drawable.ic_vegetables),
        Category("Breads & Sweets", R.drawable.ic_bakery), // Changed name to match image
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
                "Added 1x ${product.name}. Total in cart: ${currentItem.quantity}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "Failed to add ${product.name} to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    private val onCategoryClick: (Category) -> Unit = { category ->
        // This is the click handler for the new category cards
        Toast.makeText(this, "Filtering by category: ${category.name}", Toast.LENGTH_SHORT).show()
        // TODO: Implement actual product filtering or navigation to a dedicated Category page
    }

    // --- Lifecycle ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Shared Views
        bannerViewPager = findViewById(R.id.bannerViewPager)
        bannerIndicator = findViewById(R.id.bannerIndicator)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView) // NEW: Initialization
        handler = Handler(Looper.getMainLooper())

        setupBannerSlider()
        setupMenuIcon()
        setupCategoryList() // NEW: Set up the horizontal category cards

        // Load content
        loadProductsByCategory("Trending", R.id.trendingRecyclerView, null)

        categoryViews.forEach { (categoryName, ids) ->
            loadProductsByCategory(categoryName, ids.first, ids.second)
        }
    }

    // --- Setup Functions ---

    private fun setupCategoryList() {
        // Horizontal layout for the categories
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryAdapter(sampleCategories, onCategoryClick)
        categoryRecyclerView.adapter = adapter
    }

    private fun setupMenuIcon() {
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)

        menuIcon.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.main_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_cart -> {
                        // Navigation to CartActivity
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.action_account -> {
                        // Navigation to AccountActivity
                        val intent = Intent(this, AccountActivity::class.java)
                        startActivity(intent)
                        true
                    }
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

                    // Note: Assuming ProductListFragment is an adapter wrapper
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