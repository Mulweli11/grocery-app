// File: com.example.ecome/CheckoutActivity.kt

package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecome.utils.CartManager
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var subtotalText: TextView
    private lateinit var shippingText: TextView
    private lateinit var totalText: TextView
    private lateinit var placeOrderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        subtotalText = findViewById(R.id.checkoutSubtotalText)
        shippingText = findViewById(R.id.checkoutShippingText)
        totalText = findViewById(R.id.checkoutTotalText)
        placeOrderButton = findViewById(R.id.placeOrderButton)

        if (CartManager.cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty. Redirecting...", Toast.LENGTH_SHORT).show()
            finish() // Close the checkout if the cart is empty
            return
        }

        updateSummary()
        setupPlaceOrder()
    }

    private fun updateSummary() {
        val subtotal = CartManager.getCartTotal()
        val shippingFee = 0.00 // Example: Assume free shipping over a certain amount
        val total = subtotal + shippingFee

        // Use South African Locale (ZAR/Rand) for consistency
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

        subtotalText.text = currencyFormat.format(subtotal)
        shippingText.text = if (shippingFee == 0.0) "FREE" else currencyFormat.format(shippingFee)
        totalText.text = currencyFormat.format(total)
    }

    private fun setupPlaceOrder() {
        placeOrderButton.setOnClickListener {

            // --- SIMULATED PAYMENT SUCCESS ---

            // 1. Get the total amount before clearing the cart
            val orderTotal = CartManager.getCartTotal()
            val itemCount = CartManager.cartItems.sumOf { it.quantity }

            // 2. Clear the cart data (Crucial step after "payment")
            CartManager.clearCart()

            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show()

            // 3. Navigate to the Order Confirmation (Payment Card) screen
            val intent = Intent(this, OrderConfirmationActivity::class.java).apply {
                // Pass order details to the confirmation screen
                putExtra("ORDER_TOTAL", orderTotal)
                putExtra("ITEM_COUNT", itemCount)
            }
            startActivity(intent)
            finish() // Prevent returning to checkout screen
        }
    }
}