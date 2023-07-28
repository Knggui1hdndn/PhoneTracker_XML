package com.gps.phonetracker.numberlocator.family.tracklocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gps.phonetracker.numberlocator.family.tracklocation.R
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ItemReceivedFriendsRequestBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class AdapterReceivedFriendRequest(private val list: List<Users>) :
    RecyclerView.Adapter<AdapterReceivedFriendRequest.AdapterReceivedFriendRequestViewHolder>() {
    class AdapterReceivedFriendRequestViewHolder(private val binding: ItemReceivedFriendsRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(users: Users) {
            with(binding) {
                ShareData.setImage(users.avatar, imgAvatar)
                txtName.text = users.title
                btnAccept.setOnClickListener {}
                btnDelete.setOnClickListener {}
             }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterReceivedFriendRequestViewHolder {
        return AdapterReceivedFriendRequestViewHolder(
            ItemReceivedFriendsRequestBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_received_friends_request, null, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterReceivedFriendRequestViewHolder, position: Int) {
        holder.bind(list[position])
    }
}