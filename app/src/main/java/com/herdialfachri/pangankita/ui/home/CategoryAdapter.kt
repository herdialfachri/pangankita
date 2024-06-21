package com.herdialfachri.pangankita.ui.data.home_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.herdialfachri.pangankita.R

class CategoryAdapter(
    private val categories: List<CategoryItem?>,
    private val onCategoryClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: CategoryItem)
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImage: ImageView = view.findViewById(R.id.imgCategory)
        val categoryText: TextView = view.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        if (category != null) {
            holder.categoryText.text = category.name
        }

        Glide.with(holder.categoryImage.context)
            .load(category!!.photo)
            .into(holder.categoryImage)

        holder.itemView.setOnClickListener {
            if (category != null) {
                onCategoryClickListener.onCategoryClick(category)
            }
        }
    }

    override fun getItemCount() = categories.size
}
