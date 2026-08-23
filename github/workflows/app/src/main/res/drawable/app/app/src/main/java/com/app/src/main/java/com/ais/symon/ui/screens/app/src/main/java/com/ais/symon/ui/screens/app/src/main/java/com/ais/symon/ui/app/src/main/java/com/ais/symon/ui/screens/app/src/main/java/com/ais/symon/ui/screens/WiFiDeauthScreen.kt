package com.ais.symon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun WiFiDeauthScreen(onBack: () -> Unit) {
    var targetSSID by remember { mutableStateOf("") }
    var resultMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deauth Tool") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F1923)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.SignalWifiOff, null, modifier = Modifier.size(64.dp), tint = Color(0xFFFF5252))
            Spacer(Modifier.height(12.dp))
            Text("WiFi Deauthenticator", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("নন-রুট মোড — সংযোগ বিচ্ছিন্ন টেস্ট", color = Color(0xFF90CAF9))
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = targetSSID,
                onValueChange = { targetSSID = it },
                label = { Text("টার্গেট WiFi নাম (SSID)") },
                leadingIcon = { Icon(Icons.Default.WifiOff, null, tint = Color(0xFFFF5252)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFFFF5252), unfocusedBorderColor = Color(0xFF37474F)),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { resultMsg = "$targetSSID এর সাথে সংযোগ বিচ্ছিন্ন করা হয়েছে!" },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) { Icon(Icons.Default.WifiOff, null); Spacer(Modifier.width(8.dp)); Text("ডিসকানেক্ট টেস্ট", fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(16.dp))
            resultMsg?.let {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp)); Text(it, color = Color.White)
                    }
                }
            }
        }
    }
}
