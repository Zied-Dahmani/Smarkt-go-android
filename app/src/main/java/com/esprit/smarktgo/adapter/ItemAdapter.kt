package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.view.ItemsActivity
import com.esprit.smarktgo.view.SupermarketActivity

class ItemAdapter(val mActivity: ItemsActivity) : RecyclerView.Adapter<ItemViewHolder>() {

    private var list = ArrayList<Item>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Item>) {
        this.list = list as ArrayList<Item>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
        return ItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Glide.with(holder.itemView).load("http://192.168.1.4:9090/img/" + list[position].image).into(holder.imageV)
        holder.nameTV.text = list[position].name
        holder.descriptionTV.text = list[position].description
        holder.priceTV.text = "${list[position].price} TND"
        holder.iconCardView.setOnClickListener {
            mActivity.orderDialog.item = list[position]
            mActivity.orderDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTV : TextView
    val priceTV : TextView
    val descriptionTV : TextView
    val imageV : ImageView
    val iconCardView : CardView

    init {
        imageV = itemView.findViewById(R.id.itemImage)
        nameTV = itemView.findViewById(R.id.itemName)
        priceTV = itemView.findViewById(R.id.itemPrice)
        descriptionTV = itemView.findViewById(R.id.itemDescription)
        iconCardView = itemView.findViewById(R.id.iconCardView)
    }

}