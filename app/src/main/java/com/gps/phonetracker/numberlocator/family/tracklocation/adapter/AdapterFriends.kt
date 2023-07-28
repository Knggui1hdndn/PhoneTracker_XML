package com.gps.phonetracker.numberlocator.family.tracklocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gps.phonetracker.numberlocator.family.tracklocation.R
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ItemFriendsBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class AdapterFriends(private val list: List<Users>) :
    RecyclerView.Adapter<AdapterFriends.AdapterFriendsViewHolder>() {
    class AdapterFriendsViewHolder(private val binding: ItemFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(users: Users) {
            with(binding) {
                ShareData.setImage(users.avatar, imgAvatar)
                txtName.text = users.title
                imgMenu.setOnClickListener {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFriendsViewHolder {
        return AdapterFriendsViewHolder(
            ItemFriendsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_friends, null, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterFriendsViewHolder, position: Int) {
        holder.bind(list[position])
    }
}