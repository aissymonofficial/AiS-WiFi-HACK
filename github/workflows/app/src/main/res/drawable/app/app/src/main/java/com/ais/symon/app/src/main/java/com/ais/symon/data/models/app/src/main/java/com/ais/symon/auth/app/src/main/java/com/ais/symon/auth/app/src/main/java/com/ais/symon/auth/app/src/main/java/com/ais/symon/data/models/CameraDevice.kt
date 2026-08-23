package com.ais.symon.data.models

@kotlinx.serialization.Serializable
data class CameraDevice(
    val ip: String = "",
    val macAddress: String = "N/A",
    val vendor: String = "Unknown",
    val port: Int = 0,
    val protocol: String = "RTSP",
    val username: String = "N/A",
    val password: String = "N/A",
    val isDefaultCredential: Boolean = false,
    val modelInfo: String = "Unknown",
    val responseTime: Long = 0
)

@kotlinx.serialization.Serializable
data class ScanProgress(
    val currentIp: String = "",
    val totalIps: Int = 0,
    val scannedIps: Int = 0,
    val devicesFound: Int = 0,
    val isScanning: Boolean = false,
    val isComplete: Boolean = false
)
