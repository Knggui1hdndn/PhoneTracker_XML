package com.gps.phonetracker.numberlocator.family.tracklocation.application
import com.gps.phonetracker.numberlocator.family.tracklocation.BuildConfig
import android.os.Bundle
import com.access.pro.activity.BaseActivity
import com.access.pro.application.ProApplication
import com.access.pro.config.AdsConfigModel

class Application :ProApplication() {
    override fun onCreate() {
        AdsConfigModel.GG_APP_OPEN = BuildConfig.GG_APP_OPEN
        AdsConfigModel.GG_BANNER = BuildConfig.GG_BANNER
        AdsConfigModel.GG_NATIVE = BuildConfig.GG_NATIVE
        AdsConfigModel.GG_FULL = BuildConfig.GG_FULL
        AdsConfigModel.GG_REWARDED = BuildConfig.GG_REWARDED
        super.onCreate()
    }
}