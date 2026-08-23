package com.ais.symon.tools.wifiscan

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

data class WiFiNetworkInfo(
    val ssid: String,
    val bssid: String,
    val signalLevel: Int,       // dBm
    val signalPercent: Int,     // 0-100
    val frequency: Int,
    val channel: Int,
    val securityType: String,
    val capabilities: String,
    val vendor: String,
    val distanceEstimate: String
)

class WiFiScanner(private val context: Context) {
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    companion object {
        private val MAC_VENDORS = mapOf(
            "00:1A:2B" to "Intel", "00:14:BF" to "Broadcom",
            "00:23:68" to "Cisco", "00:26:BB" to "Dell",
            "A0:F3:C1" to "TP-Link", "14:CF:A2" to "TP-Link",
            "E0:20:96" to "Tenda", "C0:4A:00" to "Xiaomi",
            "18:FE:34" to "ASUS", "04:18:D6" to "Raspberry Pi",
            "00:17:DF" to "D-Link", "A4:2B:8C" to "Huawei",
            "00:24:A5" to "Netgear", "F4:F2:6D" to "MikroTik",
            "50:C7:BF" to "ZTE", "08:10:78" to "Samsung",
            "AC:84:C6" to "Mi", "18:1B:EB" to "Google/Nest",
            "8C:8B:83" to "Hikvision", "DC:4F:22" to "Ubiquiti"
        )
    }

    fun scanNetworks(): Flow<List<WiFiNetworkInfo>> = callbackFlow {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context, intent: android.content.Intent) {
                val results = wifiManager.scanResults
                val networks = results.map { it.toNetworkInfo() }
                    .distinctBy { it.bssid }
                    .sortedByDescending { it.signalLevel }
                trySend(networks)
            }
        }
        context.registerReceiver(receiver, android.content.IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()
        awaitClose { context.unregisterReceiver(receiver) }
    }

    private fun ScanResult.toNetworkInfo(): WiFiNetworkInfo {
        val freqMHz = frequency
        val ch = if (freqMHz in 2412..2484) (freqMHz - 2412) / 5 + 1
            else if (freqMHz in 5170..5825) (freqMHz - 5170) / 5 + 34
            else 0
        val level = level
        val percent = (level + 100).coerceIn(0, 100)
        val security = when {
            capabilities.contains("WPA3") -> "WPA3"
            capabilities.contains("WPA2") -> "WPA2"
            capabilities.contains("WPA") -> "WPA"
            capabilities.contains("WEP") -> "WEP"
            else -> "OPEN"
        }
        val vendor = MAC_VENDORS.entries.firstOrNull { bssid.uppercase().startsWith(it.key) }?.value ?: "Unknown"
        val dist = when {
            level >= -50 -> "নিকটে (<5m)"
            level >= -67 -> "কাছাকাছি (5-15m)"
            level >= -80 -> "মাঝারি (15-30m)"
            else -> "দূরে (>30m)"
        }
        return WiFiNetworkInfo(ssid, bssid, level, percent, freqMHz, ch, security, capabilities, vendor, dist)
    }
}
