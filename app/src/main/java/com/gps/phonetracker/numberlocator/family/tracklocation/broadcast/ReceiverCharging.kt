package com.gps.phonetracker.numberlocator.family.tracklocation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

interface CurrentBattery {
    fun onCurrentBattery(int: Int)
}

class ReceiverCharging(private val currentBattery:CurrentBattery) : BroadcastReceiver()   {
    override fun onReceive(p0: Context?, intent: Intent?) {
        val level: Int = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        currentBattery.onCurrentBattery((level * 100 / scale.toFloat()).toInt())
    }
}