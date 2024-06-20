package com.herdialfachri.pangankita.ui.data.home_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.home.DetailFragment

class MortyAdapter(private val dataMorty: List<DataItem?>?) :
    RecyclerView.Adapter<MortyAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMorty = view.findViewById<ImageView>(R.id.recImage)
        val nameMorty = view.findViewById<TextView>(R.id.recTitle)
        val statusMorty = view.findViewById<TextView>(R.id.recPriority)
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
            // Buka DetailFragment saat item di RecyclerView diklik
            val activity = holder.itemView.context as FragmentActivity
            val fragment = DetailFragment.newInstance(
                currentItem?.name,
                currentItem?.photo,
                currentItem?.whatsappNumber,
                currentItem?.description
            )
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return dataMorty?.size ?: 0
    }
}
