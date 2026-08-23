package com.ais.symon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiFiScannerScreen(onBack: () -> Unit) {
    var isScanning by remember { mutableStateOf(false) }
    val networks = remember { mutableStateListOf(
        listOf("HOME_WiFi", "AC:F3:C1:2B:4D:8E", "-45", "WPA2", "TP-Link", "নিকটে"),
        listOf("NeighborNet", "14:CF:A2:11:33:55", "-62", "WPA2", "TP-Link", "কাছাকাছি"),
        listOf("FreeWiFi", "E0:20:96:AA:BB:CC", "-78", "OPEN ⚠️", "Tenda", "মাঝারি"),
        listOf("Office_Guest", "A0:F3:C1:DD:EE:FF", "-88", "WPA2", "TP-Link", "দূরে"),
        listOf("AndroidAP", "0A:1B:2C:3D:4E:5F", "-52", "WPA2", "Samsung", "নিকটে")
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WiFi Scanner") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                actions = { IconButton(onClick = { isScanning = !isScanning }) { Icon(if (isScanning) Icons.Default.Refresh else Icons.Default.Wifi, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F1923))) {
            if (isScanning) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF4FC3F7))
            }
            LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                items(networks) { net ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Wifi, null, tint = if (net[3] == "OPEN ⚠️") Color(0xFFFF5252) else Color(0xFF4FC3F7))
                                Spacer(Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(net[0], color = Color.White, fontWeight = FontWeight.SemiBold)
                                    Text("BSSID: ${net[1]} | ${net[3]}", color = Color(0xFF90CAF9), fontSize = 11.sp)
                                    Text("সিগন্যাল: ${net[2]}dBm | ভেন্ডর: ${net[4]} | ${net[5]}", color = Color(0xFF607D8B), fontSize = 10.sp)
                                }
                                Text("${net[2]}dBm", color = Color(0xFF90CAF9), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
