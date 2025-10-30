// File: com.example.ecome.models/CartItem.kt

package com.example.ecome.models

data class CartItem(
    val product: Product,
    var quantity: Int // The amount the user wants to purchase
)