// File: com.example.ecome.utils/CartManager.kt

package com.example.ecome.utils

import com.example.ecome.models.CartItem
import com.example.ecome.models.Product

object CartManager {

    // IMPORTANT: cartItems must be mutable for removal and addition
    val cartItems = mutableListOf<CartItem>()

    // --- Core Operations ---

    fun addToCart(product: Product, quantity: Int = 1) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            cartItems.add(CartItem(product, quantity))
        }
    }

    /**
     * Removes a CartItem completely based on its product ID.
     * Returns true if an item was successfully removed.
     */
    fun removeFromCart(productId: String): Boolean {
        return cartItems.removeIf { it.product.id == productId }
    }

    // NOTE: updateQuantity is implicitly handled by CartActivity accessing cartItems directly.

    fun getCartTotal(): Double {
        return cartItems.sumOf { it.product.price * it.quantity }
    }

    fun clearCart() {
        cartItems.clear()
    }
}