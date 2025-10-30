// File: com.example.ecome/CartActivity.kt

package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecome.adapters.CartAdapter
import com.example.ecome.models.CartItem
import com.example.ecome.utils.CartManager
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var subtotalText: TextView
    private lateinit var totalText: TextView
    private lateinit var checkoutButton: Button
    private lateinit var cartAdapter: CartAdapter

    // Use South African Locale (ZAR/Rand) for currency formatting
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize Views
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        subtotalText = findViewById(R.id.subtotalText)
        totalText = findViewById(R.id.totalText)
        checkoutButton = findViewById(R.id.checkoutButton)

        setupCartList()
        updateSummary()

        checkoutButton.setOnClickListener {
            if (CartManager.cartItems.isNotEmpty()) {
                // Navigate to the CheckoutActivity
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Your cart is empty! Cannot proceed to checkout.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCartList() {
        val cartItems = CartManager.cartItems

        cartAdapter = CartAdapter(
            cartItems,
            onQuantityChanged = { item, newQuantity -> updateCartQuantity(item, newQuantity) },
            onRemoveItem = { item -> removeItemFromCart(item) }
        )
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateCartQuantity(item: CartItem, newQuantity: Int) {
        // Update the item quantity in the underlying list
        item.quantity = newQuantity

        // Notify the adapter to refresh the specific item's view (for price/total recalculation)
        val position = CartManager.cartItems.indexOf(item)
        if (position != -1) {
            cartAdapter.notifyItemChanged(position)
        } else {
            // Fallback if item position is somehow missed
            cartAdapter.notifyDataSetChanged()
        }

        updateSummary()
        Toast.makeText(this, "Updated ${item.product.name} to $newQuantity.", Toast.LENGTH_SHORT).show()
    }

    private fun removeItemFromCart(item: CartItem) {
        val position = CartManager.cartItems.indexOf(item)
        val removed = CartManager.removeFromCart(item.product.id)

        if (removed) {
            // Check position before notifying removal
            if (position != -1) {
                cartAdapter.notifyItemRemoved(position)
            } else {
                cartAdapter.notifyDataSetChanged()
            }

            updateSummary()
            Toast.makeText(this, "Removed ${item.product.name} from cart.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSummary() {
        val subtotal = CartManager.getCartTotal()
        val total = subtotal // Assuming total equals subtotal before checkout process

        subtotalText.text = currencyFormat.format(subtotal)
        totalText.text = currencyFormat.format(total)

        // Disable checkout button if cart is empty
        checkoutButton.isEnabled = CartManager.cartItems.isNotEmpty()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the cart list and summary every time the user returns (e.g., from product detail screen)
        cartAdapter.notifyDataSetChanged()
        updateSummary()
    }
}