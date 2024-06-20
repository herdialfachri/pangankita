package com.herdialfachri.pangankita.ui.data.home_api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.home.DetailFragment

class MortyAdapter(private var dataMorty: List<DataItem?>?) :
    RecyclerView.Adapter<MortyAdapter.MyViewHolder>(), android.widget.Filterable {

    private var dataMortyFull: List<DataItem?>? = ArrayList(dataMorty)

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMorty: ImageView = view.findViewById(R.id.recImage)
        val nameMorty: TextView = view.findViewById(R.id.recTitle)
        val statusMorty: TextView = view.findViewById(R.id.recPriority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataMorty?.get(position)
        holder.statusMorty.text = currentItem?.price?.let { String.format("%.2f", it) } ?: "N/A"
        holder.nameMorty.text = currentItem?.name

        Glide.with(holder.imgMorty)
            .load(currentItem?.photo)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgMorty)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(DetailFragment.ARG_NAME, currentItem?.name)
            bundle.putString(DetailFragment.ARG_IMAGE, currentItem?.photo)
            bundle.putString(DetailFragment.ARG_WA, currentItem?.whatsappNumber)
            bundle.putString(DetailFragment.ARG_DESC, currentItem?.description)
            bundle.putString(DetailFragment.ARG_CAT, currentItem?.category)
            holder.itemView.findNavController().navigate(R.id.action_navigation_home_to_detailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return dataMorty?.size ?: 0
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filteredList = mutableListOf<DataItem?>()
                if (charSequence == null || charSequence.isEmpty()) {
                    filteredList.addAll(dataMortyFull ?: emptyList())
                } else {
                    val filterPattern = charSequence.toString().toLowerCase().trim()
                    for (item in dataMortyFull ?: emptyList()) {
                        if (item?.name?.toLowerCase()?.contains(filterPattern) == true) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                dataMorty = filterResults?.values as List<DataItem?>
                notifyDataSetChanged()
            }
        }
    }
}
