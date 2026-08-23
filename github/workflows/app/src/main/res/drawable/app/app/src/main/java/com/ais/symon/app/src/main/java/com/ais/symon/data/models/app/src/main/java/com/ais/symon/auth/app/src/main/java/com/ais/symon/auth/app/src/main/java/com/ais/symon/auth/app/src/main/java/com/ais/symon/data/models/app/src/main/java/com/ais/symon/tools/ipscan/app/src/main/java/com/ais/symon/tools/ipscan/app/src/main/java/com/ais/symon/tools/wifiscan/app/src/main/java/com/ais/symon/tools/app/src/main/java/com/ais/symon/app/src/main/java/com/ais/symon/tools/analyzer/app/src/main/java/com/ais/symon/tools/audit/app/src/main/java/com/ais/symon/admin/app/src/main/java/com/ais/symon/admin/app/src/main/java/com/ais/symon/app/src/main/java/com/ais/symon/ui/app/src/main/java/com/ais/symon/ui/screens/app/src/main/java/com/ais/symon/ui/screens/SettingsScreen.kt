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
fun SettingsScreen(onBack: () -> Unit) {
    var isDarkMode by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("সেটিংস") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0F1923))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Theme
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎨 থিম", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode, null, tint = Color(0xFF90CAF9))
                        Spacer(Modifier.width(12.dp))
                        Text(if (isDarkMode) "ডার্ক মোড" else "লাইট মোড", color = Color.White, modifier = Modifier.weight(1f))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { isDarkMode = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF4FC3F7))
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📱 অ্যাপ তথ্য", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    listOf("NetPulse v2.0", "ডেভেলপার: AiS SYMON", "নির্মিত: Kotlin + Jetpack Compose").forEach {
                        Row {
                            Text("• ", color = Color(0xFF4FC3F7))
                            Text(it, color = Color(0xFFB0BEC5), fontSize = 14.sp)
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Contact
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📞 প্রশাসক", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("সমস্যা হলে প্রশাসকের সাথে যোগাযোগ করুন:", color = Color(0xFFB0BEC5), fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    // These will be loaded from Firebase Admin
                    Row { Icon(Icons.Default.Telegram, null, tint = Color(0xFF4FC3F7), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp)); Text("টেলিগ্রাম: @AiS_SYMON", color = Color.White, fontSize = 14.sp) }
                    Spacer(Modifier.height(4.dp))
                    Row { Icon(Icons.Default.Chat, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp)); Text("হোয়াটসঅ্যাপ: +8801XXXXXXXXX", color = Color.White, fontSize = 14.sp) }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Logout
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("লগআউট", fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(40.dp))
        }
    }
}
