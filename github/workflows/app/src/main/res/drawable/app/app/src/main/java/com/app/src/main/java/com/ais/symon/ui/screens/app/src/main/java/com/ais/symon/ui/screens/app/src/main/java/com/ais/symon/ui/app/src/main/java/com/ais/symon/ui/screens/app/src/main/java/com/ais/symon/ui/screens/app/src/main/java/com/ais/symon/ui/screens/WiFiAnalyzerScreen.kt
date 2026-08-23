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
fun WiFiAnalyzerScreen(onBack: () -> Unit) {
    val channels = remember { mutableStateListOf(
        listOf("1", "2412 MHz", "4", "-58", "⚠️ ভিড়"),
        listOf("6", "2437 MHz", "7", "-52", "❌ অনেক ভিড়"),
        listOf("11", "2462 MHz", "3", "-63", "✅ ভালো"),
        listOf("36", "5180 MHz", "1", "-72", "✅ ফাঁকা"),
        listOf("149", "5745 MHz", "2", "-68", "✅ ফাঁকা")
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WiFi Analyzer") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F1923)).padding(12.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1A237E)), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF4FC3F7))
                    Spacer(Modifier.width(8.dp))
                    Text("সেরা চ্যানেল: ১১ | খারাপ চ্যানেল: ৬", color = Color.White, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn {
                items(channels) { ch ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(10.dp)) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("CH ${ch[0]}", color = Color(0xFF4FC3F7), fontWeight = FontWeight.Bold, modifier = Modifier.width(50.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ch[1], color = Color(0xFFB0BEC5), fontSize = 11.sp)
                                Text("${ch[2]} নেটওয়ার্ক | গড় সিগন্যাল: ${ch[3]}dBm", color = Color(0xFF607D8B), fontSize = 10.sp)
                            }
                            Text(ch[4], color = if (ch[4].contains("ভিড়")) Color(0xFFFF5252) else Color(0xFF4CAF50), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
