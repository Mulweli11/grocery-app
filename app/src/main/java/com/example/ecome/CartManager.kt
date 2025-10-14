package com.example.ecome

import com.example.ecome.Product

object CartManager {
    private val cartItems = mutableListOf<Product>()

    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    fun getCartItems(): List<Product> = cartItems

    fun getCartCount(): Int = cartItems.size

    fun clearCart() {
        cartItems.clear()
    }
}