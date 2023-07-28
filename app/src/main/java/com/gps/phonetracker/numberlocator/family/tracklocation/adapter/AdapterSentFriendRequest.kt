package com.gps.phonetracker.numberlocator.family.tracklocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gps.phonetracker.numberlocator.family.tracklocation.R
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ItemSentFriendsRequestBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class AdapterSentFriendRequest(private val list: List<Users>) :
    RecyclerView.Adapter<AdapterSentFriendRequest.AdapterSentFriendRequestViewHolder>() {
    class AdapterSentFriendRequestViewHolder(private val binding: ItemSentFriendsRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(users: Users) {
            with(binding) {
                ShareData.setImage(users.avatar, imgAvatar)
                txtName.text = users.title
                btnCancel.setOnClickListener {}
                imgSent.setOnClickListener {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSentFriendRequestViewHolder {
        return AdapterSentFriendRequestViewHolder(
            ItemSentFriendsRequestBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_friends, null, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterSentFriendRequestViewHolder, position: Int) {
        holder.bind(list[position])
    }
}