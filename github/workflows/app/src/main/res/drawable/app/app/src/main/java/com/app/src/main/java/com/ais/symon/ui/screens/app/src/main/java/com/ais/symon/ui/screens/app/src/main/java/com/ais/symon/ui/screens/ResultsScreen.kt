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
fun ResultsScreen(scanType: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("স্ক্যান ফলাফল — ${scanType.replaceFirstChar { it.uppercase() }}") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F1923)).padding(16.dp)
        ) {
            // Demo results
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color(0xFF4FC3F7))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("192.168.1.105", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Hikvision DS-2CD2042WD | পোর্ট: 554", color = Color(0xFF90CAF9), fontSize = 12.sp)
                            Text("ইউজার: admin | পাস: 12345 ✅ ডিফল্ট", color = Color(0xFF4CAF50), fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Videocam, null, tint = Color(0xFFFF9800))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("192.168.1.110", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Dahua IPC-HFW1431S | পোর্ট: 80", color = Color(0xFF90CAF9), fontSize = 12.sp)
                            Text("ইউজার: admin | পাস: admin ✅ ডিফল্ট", color = Color(0xFF4CAF50), fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, null, tint = Color(0xFFFF5252))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("192.168.1.200:8080", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("TP-Link Tapo C200 | পোর্ট: 8080", color = Color(0xFF90CAF9), fontSize = 12.sp)
                            Text("ইউজার: admin | পাস: admin ✅ ডিফল্ট", color = Color(0xFF4CAF50), fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("মোট ৩টি ডিভাইস পাওয়া গেছে", color = Color(0xFF90CAF9), fontSize = 14.sp)
        }
    }
}
