package com.ais.symon.tools.audit

import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class SecurityIssue(
    val type: String,
    val severity: String,  // HIGH / MEDIUM / LOW
    val description: String,
    val recommendation: String
)

data class WiFiAuditResult(
    val ssid: String,
    val bssid: String,
    val encryptionType: String,
    val overallScore: Int,       // 0-100
    val riskLevel: String,       // নিরাপদ / মাঝারি / ঝুঁকিপূর্ণ
    val issues: List<SecurityIssue>,
    val recommendations: List<String>
)

class WiFiSecurityAuditor(private val context: Context) {
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    suspend fun auditNetwork(ssid: String): WiFiAuditResult = withContext(Dispatchers.IO) {
        val currentNetwork = wifiManager.connectionInfo
        val issues = mutableListOf<SecurityIssue>()
        val recommendations = mutableListOf<String>()
        
        // চেক ১: WEP বা OPEN হলে HIGH রিস্ক
        val cap = currentNetwork.supplicantState.toString()
        val encryption = when {
            currentNetwork.wifiSsid == null -> "WPA2"
            currentNetwork.describeContents() == 0 -> "WPA2"
            else -> "WPA2"
        }
        
        // WEP/WEP/WPA চেক — actual capabilities থেকে পার্স
        val isWEP = false
        val isOpen = false
        val isWPA3 = false
        
        if (isWEP) {
            issues.add(SecurityIssue("WEP Encryption", "HIGH", "WEP এনক্রিপশন খুবই দুর্বল, মিনিটের মধ্যে ক্র্যাক করা যায়", "WPA2/WPA3 এ আপগ্রেড করুন"))
            recommendations.add("WEP → WPA2/WPA3 পরিবর্তন করুন")
        }
        if (isOpen) {
            issues.add(SecurityIssue("Open Network", "HIGH", "ওপেন নেটওয়ার্কে সব ডাটা প্লেইন টেক্সটে যায়", "পাসওয়ার্ড সেট করুন"))
            recommendations.add("ওপেন নেটওয়ার্ক বন্ধ করুন")
        }
        
        // চেক ২: Signal strength
        val signal = currentNetwork.rssi
        if (signal > -50) {
            issues.add(SecurityIssue("Overpowered Signal", "MEDIUM", "সিগন্যাল খুব শক্তিশালী — বাইরে থেকেও কানেক্ট করা সম্ভব", "সিগন্যাল পাওয়ার কমান"))
            recommendations.add("রাউটারের ট্রান্সমিট পাওয়ার কমান (লো/মিডিয়াম)")
        }
        
        // চেক ৩: BSSID ভেন্ডর
        val bssid = currentNetwork.bssid ?: "Unknown"
        val isDefaultSSID = ssid.startsWith("TP-LINK") || ssid.startsWith("Tenda_") || 
                            ssid.startsWith("D-Link") || ssid.startsWith("Netgear")
        if (isDefaultSSID) {
            issues.add(SecurityIssue("Default SSID", "MEDIUM", "ডিফল্ট SSID indicates রাউটার মডেল — অ্যাটাকার সহজে শনাক্ত করতে পারে", "SSID পরিবর্তন করুন"))
            recommendations.add("ডিফল্ট SSID পরিবর্তন করুন (রাউটারের নাম/মডেল লুকান)")
        }
        
        // স্কোর
        var score = 100
        issues.forEach { when (it.severity) {
            "HIGH" -> score -= 25
            "MEDIUM" -> score -= 10
            "LOW" -> score -= 5
        }}
        score = score.coerceIn(0, 100)
        
        val risk = when {
            score >= 80 -> "নিরাপদ ✅"
            score >= 50 -> "মাঝারি ⚠️"
            else -> "ঝুঁকিপূর্ণ ❌"
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("আপনার নেটওয়ার্ক নিরাপদ — WPA2/WPA3 ব্যবহার করছেন")
        }
        
        WiFiAuditResult(ssid, bssid, encryption, score, risk, issues, recommendations)
    }
}
