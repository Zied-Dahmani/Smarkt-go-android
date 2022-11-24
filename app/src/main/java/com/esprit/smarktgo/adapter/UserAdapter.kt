package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.view.CartGroupActivity


class UserAdapter(val mActivity: CartGroupActivity) : RecyclerView.Adapter<UserViewHolder>() {

    private var list = ArrayList<User>()


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<User>) {
        this.list = list as ArrayList<User>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.imageV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_baseline_person_24))
        holder.nameTV.text = list[position].fullName
        holder.addUserCardView.setOnClickListener {
            mActivity.addUser(list[position].id)
            list.removeAt(position)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTV : TextView
    val imageV : ImageView
    val addUserCardView : CardView

    init {
        imageV = itemView.findViewById(R.id.userImage)
        nameTV = itemView.findViewById(R.id.userName)
        addUserCardView = itemView.findViewById(R.id.addUserCardView)
    }

}