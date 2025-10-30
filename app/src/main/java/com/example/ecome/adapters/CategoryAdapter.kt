// File: com.example.ecome.adapters/CategoryAdapter.kt

package com.example.ecome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecome.R
import com.example.ecome.models.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit // Click handler for navigation/filtering
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.categoryIcon)
        val name: TextView = itemView.findViewById(R.id.categoryName)
        val container: View = itemView.findViewById(R.id.categoryCardContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.icon.setImageResource(category.iconResId)
        holder.name.text = category.name

        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}