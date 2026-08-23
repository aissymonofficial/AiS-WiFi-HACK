package com.ais.symon.tools.deauth

import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.NetworkInterface

class WiFiDeauther(private val context: Context) {
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    data class DeauthResult(
        val success: Boolean,
        val targetBssid: String,
        val targetSsid: String,
        val method: String,
        val details: String
    )

    /** নন-রুটে শুধু ডিসকানেক্ট ফোর্স করতে পারে (Wi-Fi disconnect command) */
    suspend fun forceDisconnect(bssid: String, ssid: String): DeauthResult = withContext(Dispatchers.IO) {
        try {
            // MAC-based disconnect attempt via WiFiManager
            val configured = wifiManager.configuredNetworks
            val target = configured.firstOrNull { it.SSID.replace("\"", "") == ssid }
            if (target != null) {
                wifiManager.removeNetwork(target.networkId)
                wifiManager.saveConfiguration()
                DeauthResult(true, bssid, ssid, "Disconnect", "${ssid} ডিসকানেক্ট করা হয়েছে")
            } else {
                DeauthResult(false, bssid, ssid, "Disconnect", "নেটওয়ার্ক কনফিগারেশন খুঁজে পাওয়া যায়নি")
            }
        } catch (e: Exception) {
            DeauthResult(false, bssid, ssid, "Disconnect", e.message ?: "ত্রুটি")
        }
    }
}
