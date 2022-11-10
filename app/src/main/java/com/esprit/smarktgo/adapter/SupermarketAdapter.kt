package com.esprit.smarktgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Supermarket

class SupermarketAdapter() : RecyclerView.Adapter<SupermarketViewHolder>() {

    private var list = ArrayList<Supermarket>()

    fun setList(list: List<Supermarket>) {
        this.list = list as ArrayList<Supermarket>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupermarketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.supermarket_item, parent, false)
        return SupermarketViewHolder(view)
    }


    override fun onBindViewHolder(holder: SupermarketViewHolder, position: Int) {
        Glide.with(holder.itemView).load("http://192.168.1.4:9090/img/" + list[position].image).into(holder.image)
        holder.name.text = list[position].name
    }

    override fun getItemCount(): Int {
        return list.size
    }

}


class SupermarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image : ImageView
    val name : TextView

    init {
        image = itemView.findViewById(R.id.supermarketImage)
        name = itemView.findViewById(R.id.supermarketName)
    }

}