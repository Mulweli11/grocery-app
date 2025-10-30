// File: com.example.ecome.adapters/CartAdapter.kt

package com.example.ecome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton // Import for ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecome.R
import com.example.ecome.models.CartItem
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    // This function will be called when the remove button is clicked
    private val onRemoveItem: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.productName)
        val itemImage: ImageView = itemView.findViewById(R.id.productImage)

        // New view for the remove button
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)

        val itemPrice: TextView = itemView.findViewById(R.id.itemPriceText)
        val itemTotal: TextView = itemView.findViewById(R.id.itemTotalText)
        val quantitySpinner: Spinner = itemView.findViewById(R.id.quantitySpinner)
        val itemDetailsPlaceholder: View = itemView.findViewById(R.id.productDetailsPlaceholder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        val product = item.product

        holder.itemName.text = product.name
        holder.itemPrice.text = currencyFormat.format(product.price)
        holder.itemTotal.text = currencyFormat.format(product.price * item.quantity)

        // Glide Image Loading
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .centerCrop()
            .into(holder.itemImage)

        setupQuantitySpinner(holder.quantitySpinner, item)

        // ** SETTING UP THE REMOVE BUTTON CLICK LISTENER **
        holder.removeButton.setOnClickListener {
            // Call the lambda function passed from CartActivity
            onRemoveItem(item)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    private fun setupQuantitySpinner(spinner: Spinner, item: CartItem) {
        val quantities = (1..10).map { it.toString() }.toTypedArray()

        val adapter = ArrayAdapter(
            spinner.context,
            android.R.layout.simple_spinner_item,
            quantities
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val currentQuantityIndex = item.quantity - 1
        if (currentQuantityIndex >= 0 && currentQuantityIndex < quantities.size) {
            spinner.setSelection(currentQuantityIndex)
        }

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val newQuantity = quantities[pos].toInt()
                if (newQuantity != item.quantity) {
                    onQuantityChanged(item, newQuantity)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }
}