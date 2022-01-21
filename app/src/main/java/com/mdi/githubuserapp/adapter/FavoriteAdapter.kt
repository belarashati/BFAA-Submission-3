package com.mdi.githubuserapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdi.githubuserapp.R
import com.mdi.githubuserapp.database.FavoriteDb

class FavoriteAdapter(private val mContext: Context, private val listUser: List<FavoriteDb>) : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_favorite_users, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(mContext)
            .load(listUser[position].avatarUrl)
            .into( holder.imgUser)
        holder.tvUsername.text = listUser[position].username
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgUser: ImageView = itemView.findViewById(R.id.iv_profile)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_username)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteDb?)
    }

}