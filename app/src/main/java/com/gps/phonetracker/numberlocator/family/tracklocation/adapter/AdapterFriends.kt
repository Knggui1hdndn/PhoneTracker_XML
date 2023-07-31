package com.gps.phonetracker.numberlocator.family.tracklocation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gps.phonetracker.numberlocator.family.tracklocation.R
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ItemFriendsBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.FriendsFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class AdapterFriends(private var list: List<Users>, private val friendsFireBase: FriendsFireBase) :
    RecyclerView.Adapter<AdapterFriends.AdapterFriendsViewHolder>() {
    inner class AdapterFriendsViewHolder(private val binding: ItemFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(users: Users) {
            with(binding) {
                val context = binding.root.context
                val currentUser=ShareData.currentUser!!
                val checkFriends = currentUser.friends.contains(users.id)
                val checkSentFriendRequest = currentUser.sentFriendRequest.contains(users.id)
                val checkReceivedFriendRequest = currentUser.receivedFriendRequest.contains(users.id)
                mLinearReciver.visibility =
                    if (checkReceivedFriendRequest) View.VISIBLE else View.GONE
                btnCancel.visibility = if (checkSentFriendRequest) View.VISIBLE else View.GONE
                imgOpstion.setImageDrawable(
                    if (checkFriends)
                        context.getDrawable(R.drawable.baseline_menu_24)
                    else
                        context.getDrawable(R.drawable.baseline_send_24)
                )

                btnCancel.setOnClickListener {
                    remove(ShareData.uid,users.id)
                }

                btnDelete.setOnClickListener {
                    remove(users.id,ShareData.uid)
                }

                btnAccept.setOnClickListener {
                    remove(users.id,ShareData.uid)
                    friendsFireBase.addFriends(users.id)
                }

                ShareData.setImage(users.avatar, imgAvatar)
                txtName.text = users.title

                imgOpstion.setOnClickListener {
                    friendsFireBase.addReceivedFriendRequest(users.id)
                    friendsFireBase.addSentFriendRequest(ShareData.uid)
                }
            }
        }

        private fun remove(p: String, p0: String) {
            friendsFireBase.removeReceivedFriendRequest(p)
            friendsFireBase.removeSentFriendRequest(p0)
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

    fun setData(list: List<Users>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterFriendsViewHolder, position: Int) {
        holder.bind(list[position])
    }
}