package com.esprit.smarktgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.ProfileItem

class ProfileAdapter(val list: List<ProfileItem>) : RecyclerView.Adapter<ProfileViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(view)
    }


    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.image.setImageResource(list[position].image)
        holder.item.text = list[position].item
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val item : TextView
    val image : ImageView

    init {
        image = itemView.findViewById(R.id.profileIcon)
        item = itemView.findViewById(R.id.profileItem)
    }

}