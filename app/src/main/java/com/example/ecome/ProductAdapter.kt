package com.example.ecome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ecome.R










class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // FIX: Updated IDs to match the item_product.xml layout structure.
        val imageProduct: ImageView = itemView.findViewById(R.id.ivProductImage)
        val textName: TextView = itemView.findViewById(R.id.tvProductName)
        val textPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val textWeight: TextView = itemView.findViewById(R.id.tvProductUnit) // Corresponds to the unit/weight
        val btnAdd: ImageView = itemView.findViewById(R.id.ivAddButton) // Changed from ImageButton to ImageView based on common practice/layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // NOTE: Ensure R.layout.item_product_card or R.layout.item_product exists and is correct.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false) // Assuming 'item_product' from previous turn
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        // Binding data to views
        holder.imageProduct.setImageResource(product.imageRes)
        holder.textName.text = product.name
        holder.textWeight.text = product.weight // Using 'weight' property
        holder.textPrice.text = "R ${String.format("%.2f", product.price)}" // Added currency and formatting

        holder.btnAdd.setOnClickListener {
            CartManager.addToCart(product)
            Toast.makeText(
                holder.itemView.context,
                "${product.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}