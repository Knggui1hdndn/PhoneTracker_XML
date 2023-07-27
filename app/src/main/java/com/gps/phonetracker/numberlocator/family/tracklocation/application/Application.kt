package com.gps.phonetracker.numberlocator.family.tracklocation.application

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.access.pro.application.ProApplication
import com.access.pro.config.AdsConfigModel
import com.google.gson.Gson
import com.gps.phonetracker.numberlocator.family.tracklocation.BuildConfig
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.Constraints
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class Application : ProApplication() {
    override fun onCreate() {
        AdsConfigModel.GG_APP_OPEN = BuildConfig.GG_APP_OPEN
        AdsConfigModel.GG_BANNER = BuildConfig.GG_BANNER
        AdsConfigModel.GG_NATIVE = BuildConfig.GG_NATIVE
        AdsConfigModel.GG_FULL = BuildConfig.GG_FULL
        AdsConfigModel.GG_REWARDED = BuildConfig.GG_REWARDED
        super.onCreate()
        val gson = Gson();
        val shared = getSharedPreferences(Constraints.CURRENT_USER, Context.MODE_PRIVATE)
        ShareData.currentUser= gson.fromJson(shared.getString(Constraints.CURRENT_USER,null), Users::class.java)
         LocationFireBase(null).getUsesCurrent {
            shared.edit {
                val json = gson.toJson(it);
                putString(Constraints.CURRENT_USER, json);
                apply()
            }
            ShareData.currentUser = it
        }

    }
}