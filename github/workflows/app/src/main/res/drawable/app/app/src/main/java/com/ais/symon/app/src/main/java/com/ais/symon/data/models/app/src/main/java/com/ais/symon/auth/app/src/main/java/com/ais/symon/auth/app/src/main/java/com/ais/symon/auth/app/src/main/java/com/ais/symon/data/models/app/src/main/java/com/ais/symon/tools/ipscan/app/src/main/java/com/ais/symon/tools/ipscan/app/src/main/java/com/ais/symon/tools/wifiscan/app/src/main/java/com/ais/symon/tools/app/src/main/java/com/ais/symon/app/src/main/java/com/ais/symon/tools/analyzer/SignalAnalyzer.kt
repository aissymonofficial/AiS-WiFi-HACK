package com.ais.symon.tools.analyzer

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

data class ChannelInfo(
    val channel: Int,
    val frequency: Int,
    val networkCount: Int,
    val avgSignal: Int,
    val isCongested: Boolean,
    val networks: List<String>
)

class SignalAnalyzer(private val context: Context) {
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun analyzeChannels(): Flow<List<ChannelInfo>> = callbackFlow {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context, intent: android.content.Intent) {
                val results = wifiManager.scanResults
                val channelMap = mutableMapOf<Int, MutableList<ScanResult>>()
                
                for (r in results) {
                    val freqMHz = r.frequency
                    val ch = if (freqMHz in 2412..2484) (freqMHz - 2412) / 5 + 1
                        else if (freqMHz in 5170..5825) (freqMHz - 5170) / 5 + 34
                        else continue
                    channelMap.getOrPut(ch) { mutableListOf() }.add(r)
                }
                
                val channels = channelMap.map { (ch, nets) ->
                    val avgSig = nets.map { it.level }.average().toInt()
                    ChannelInfo(
                        channel = ch,
                        frequency = nets.first().frequency,
                        networkCount = nets.size,
                        avgSignal = avgSig,
                        isCongested = nets.size >= 5,
                        networks = nets.map { 
                            val ssid = if (it.ssid.isBlank()) "(Hidden:${it.bssid.takeLast(5)})" else it.ssid
                            "$ssid (${it.level}dBm)"
                        }
                    )
                }.sortedBy { it.channel }
                
                trySend(channels)
            }
        }
        context.registerReceiver(receiver, android.content.IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()
        awaitClose { context.unregisterReceiver(receiver) }
    }
}
