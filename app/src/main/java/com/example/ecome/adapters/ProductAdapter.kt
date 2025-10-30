package com.example.ecome.adapters

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecome.R
import com.example.ecome.models.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Core fields
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val image: ImageView = view.findViewById(R.id.productImage)

        // Quantity fields (UPDATED: TextView replaces EditText for display)
        val quantityText: TextView = view.findViewById(R.id.productQtyText) // Use productQtyText
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus) // Now an ImageButton
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus) // Now an ImageButton

        // Add to Cart button (Now a LinearLayout acting as a button)
        val addToCart: LinearLayout = view.findViewById(R.id.btnAddToCart)

        // New display fields from the new layout
        val productUnit: TextView = view.findViewById(R.id.productUnit)
        val productRating: TextView = view.findViewById(R.id.productRating)
        val discountTag: TextView = view.findViewById(R.id.discountTag)
        // val favoriteButton: ImageButton = view.findViewById(R.id.favoriteButton) // Can be added for favorite logic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // --- Display Product Data ---
        holder.name.text = product.name
        holder.price.text = "R${String.format("%.2f", product.price)}"

        // New Fields
        holder.productUnit.text = product.unit // Display unit
        holder.productRating.text = String.format("%.1f", product.rating) // Display rating

        if (product.discount.isNullOrEmpty()) {
            holder.discountTag.visibility = View.GONE
        } else {
            holder.discountTag.text = "-${product.discount}%"
            holder.discountTag.visibility = View.VISIBLE
        }

        Glide.with(holder.image.context).load(product.image).into(holder.image)
        holder.quantityText.text = product.quantity.toString() // Use the TextView

        // --- Event Listeners (Quantity and Cart) ---

        holder.btnPlus.setOnClickListener {
            // Check stock before incrementing
            if (product.quantity < product.stock || product.stock == 0) {
                product.quantity++
                holder.quantityText.text = product.quantity.toString()
            } else {
                Toast.makeText(holder.itemView.context, "Max stock reached.", Toast.LENGTH_SHORT).show()
            }
        }

        holder.btnMinus.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.quantityText.text = product.quantity.toString()
            }
        }

        holder.addToCart.setOnClickListener {
            onAddToCart(product)
            // Reset quantity to 1 on the card after adding to cart
            product.quantity = 1
            holder.quantityText.text = "1"
        }
    }

    override fun getItemCount(): Int = products.size
}