package com.ais.symon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun WiFiAuditScreen(onBack: () -> Unit) {
    var ssid by remember { mutableStateOf("") }
    var audited by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Security Audit") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F1923)).verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Shield, null, modifier = Modifier.size(56.dp), tint = Color(0xFF4FC3F7))
            Spacer(Modifier.height(8.dp))
            Text("WiFi সিকিউরিটি অডিট", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = ssid, onValueChange = { ssid = it },
                label = { Text("নেটওয়ার্ক নাম (SSID)") },
                leadingIcon = { Icon(Icons.Default.Wifi, null, tint = Color(0xFF4FC3F7)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF4FC3F7), unfocusedBorderColor = Color(0xFF37474F)),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { audited = true },
                enabled = ssid.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7))
            ) { Icon(Icons.Default.Security, null); Spacer(Modifier.width(8.dp)); Text("অডিট শুরু করুন", fontWeight = FontWeight.Bold) }

            if (audited) {
                Spacer(Modifier.height(20.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1)), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$ssid - সিকিউরিটি রিপোর্ট", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("স্কোর: ৭৫/১০০", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFA726))
                        Text("ঝুঁকি স্তর: মাঝারি ⚠️", color = Color(0xFFFFA726))
                        Spacer(Modifier.height(12.dp))
                        Divider(color = Color(0xFF37474F))
                        Spacer(Modifier.height(12.dp))
                        listOf("✅ WPA2 এনক্রিপশন সক্রিয়", "⚠️ ডিফল্ট SSID পরিবর্তন করুন", "⚠️ সিগন্যাল পাওয়ার খুব বেশি", "ℹ️ ফার্মওয়্যার আপডেট চেক করুন").forEach {
                            Row(Modifier.padding(vertical = 3.dp)) {
                                Text(it, color = Color(0xFFB0BEC5), fontSize = 13.sp)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), shape = RoundedCornerShape(8.dp)) {
                            Text("রিপোর্ট সেভ করুন", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
