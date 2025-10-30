package com.example.ecome.models

data class Product(
    val imageUrl: String = "",
    var id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val unit: String = "",
    val image: String = "",
    val rating: Double = 0.0,
    val discount: String? = null,
    var quantity: Int = 1

)