package com.gps.phonetracker.numberlocator.family.tracklocation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gps.phonetracker.numberlocator.family.tracklocation.R
import com.gps.phonetracker.numberlocator.family.tracklocation.adapter.AdapterFriends
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivityMainBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivitySearchBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.FriendsFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class SearchActivity : AppCompatActivity(), InterfaceFireBase.View.Friends {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var rcy: RecyclerView
    private lateinit var adapter: AdapterFriends
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
        val friendsFireBase = FriendsFireBase(this, null)
        with(binding) {
            btnSearch.setOnClickListener { friendsFireBase.search(edtPhoneNumber.text.toString()) }
            btnFriends.setOnClickListener { friendsFireBase.getListFriends() }
            btnReceivedFriendRequest.setOnClickListener { friendsFireBase.getListReceivedFriendRequest() }
            btnSentFriendRequest.setOnClickListener { friendsFireBase.getListSentFriendRequest() }
        }
    }

    private fun setAdapter() {
        adapter = AdapterFriends(emptyList(), FriendsFireBase(null, null))
        rcy = binding.rcy
        val layoutManager = LinearLayoutManager(this)
        rcy.layoutManager = layoutManager
        rcy.adapter = adapter
    }

    override fun onSuccess(result: List<Users>) {
        adapter.setData(result)
    }

    override fun onError(e: String) {
        ShareData.Toast(this, e)
    }
}