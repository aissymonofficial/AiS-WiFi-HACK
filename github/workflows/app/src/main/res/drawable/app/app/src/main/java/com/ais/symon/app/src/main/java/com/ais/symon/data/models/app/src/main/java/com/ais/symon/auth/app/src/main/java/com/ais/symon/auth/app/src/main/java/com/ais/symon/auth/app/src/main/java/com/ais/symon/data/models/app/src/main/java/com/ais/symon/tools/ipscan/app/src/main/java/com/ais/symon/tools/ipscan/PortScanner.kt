package com.ais.symon.tools.ipscan

import com.ais.symon.data.models.CameraDevice
import com.ais.symon.data.models.ScanProgress
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import java.io.IOException
import java.net.*
import java.util.concurrent.atomic.AtomicInteger

class PortScanner(private val scope: CoroutineScope) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(2, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(3, java.util.concurrent.TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    companion object {
        val CAMERA_PORTS = listOf(
            80, 8080, 443, 8443,   // HTTP/HTTPS admin
            554, 8554,             // RTSP
            8899,                  // ONVIF
            37777, 34567, 34599,   // Dahua
            8000,                  // Hikvision
            9000,                  // Various
            7070,                  // RTSP alt
            1935                   // RTMP
        )
    }

    fun scanSubnet(cidr: String, scanMode: ScanMode = ScanMode.NORMAL): Flow<Pair<ScanProgress, List<CameraDevice>>> = flow {
        val (baseIp, prefix) = parseCIDR(cidr)
        val ips = generateIPs(baseIp, prefix)
        val totalIps = ips.size
        val scannedCount = AtomicInteger(0)
        val devices = mutableListOf<CameraDevice>()

        emit(ScanProgress(isScanning = true, totalIps = totalIps) to devices.toList())

        val chunks = if (scanMode == ScanMode.NORMAL) ips.chunked(20) else ips.chunked(50)

        for (chunk in chunks) {
            val jobs = chunk.map { ip ->
                scope.async(Dispatchers.IO) {
                    val scanned = scannedCount.incrementAndGet()
                    scanDevice(ip)?.also { device ->
                        synchronized(devices) { devices.add(device) }
                    }
                    scanned
                }
            }
            jobs.awaitAll()
            emit(
                ScanProgress(
                    currentIp = chunk.last(),
                    totalIps = totalIps,
                    scannedIps = scannedCount.get(),
                    devicesFound = devices.size,
                    isScanning = true
                ) to devices.toList()
            )
        }

        emit(
            ScanProgress(
                totalIps = totalIps,
                scannedIps = totalIps,
                devicesFound = devices.size,
                isScanning = false,
                isComplete = true
            ) to devices.toList()
        )
    }

    private fun scanDevice(ip: String): CameraDevice? {
        for (port in CAMERA_PORTS) {
            if (isPortOpen(ip, port, 1500)) {
                val protocol = when (port) {
                    554, 8554 -> "RTSP"
                    8899 -> "ONVIF"
                    80, 8080, 443, 8443 -> "HTTP"
                    else -> "UNKNOWN"
                }
                val vendor = tryIdentifyVendor(ip, port)
                val (found, username, password) = tryDefaultCredentials(ip, port, vendor)
                return CameraDevice(
                    ip = ip,
                    port = port,
                    protocol = protocol,
                    vendor = vendor,
                    username = username,
                    password = password,
                    isDefaultCredential = found
                )
            }
        }
        return null
    }

    private fun isPortOpen(ip: String, port: Int, timeout: Int = 1500): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), timeout)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun tryIdentifyVendor(ip: String, port: Int): String {
        return try {
            val url = if (port == 80 || port == 8080) "http://$ip" else "http://$ip:$port"
            val request = Request.Builder().url(url).header("User-Agent", "Mozilla/5.0").build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()?.lowercase() ?: ""
            response.close()

            when {
                body.contains("hikvision") -> "Hikvision"
                body.contains("dahua") || body.contains("dahua technology") -> "Dahua"
                body.contains("tp-link") || body.contains("tapocamera") -> "TP-Link"
                body.contains("reolink") -> "Reolink"
                body.contains("axis") -> "Axis"
                body.contains("bosch") || body.contains("dinion") -> "Bosch"
                body.contains("vivotek") -> "Vivotek"
                body.contains("samsung") -> "Samsung"
                body.contains("sony") || body.contains("snc-") -> "Sony"
                body.contains("panasonic") || body.contains("wv-") -> "Panasonic"
                body.contains("uniview") -> "Uniview"
                body.contains("geovision") -> "GeoVision"
                body.contains("honeywell") -> "Honeywell"
                body.contains("arecont") -> "ArecontVision"
                body.contains("mobotix") -> "Mobotix"
                body.contains("acti") -> "ACTi"
                body.contains("avtech") -> "AVTech"
                body.contains("cisco") -> "Cisco"
                else -> "Unknown"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun tryDefaultCredentials(ip: String, port: Int, vendor: String): Triple<Boolean, String, String> {
        val vendorCreds = CredentialDB.credentials.filter {
            vendor == "Unknown" || it.vendor == vendor
        }
        for (cred in vendorCreds) {
            try {
                val url = if (port == 80 || port == 8080) "http://$ip" else "http://$ip:$port"
                val credential = Credentials.basic(cred.username, cred.password)
                val request = Request.Builder()
                    .url(url)
                    .header("Authorization", credential)
                    .build()
                val response = client.newCall(request).execute()
                val code = response.code
                response.close()

                if (code in 200..399) {
                    return Triple(true, cred.username, cred.password)
                }
            } catch (_: Exception) { }
        }
        return Triple(false, "N/A", "N/A")
    }

    private fun parseCIDR(cidr: String): Pair<String, Int> {
        val parts = cidr.split("/")
        return parts[0] to (if (parts.size > 1) parts[1].toIntOrNull() ?: 24 else 24)
    }

    private fun generateIPs(baseIp: String, prefix: Int): List<String> {
        if (prefix >= 24) {
            val baseParts = baseIp.split(".")
            val subnet = "${baseParts[0]}.${baseParts[1]}.${baseParts[2]}"
            return (1..254).map { "$subnet.$it" }
        }
        return listOf(baseIp)
    }
}

enum class ScanMode { NORMAL, HIGH_SPEED }
