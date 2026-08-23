package com.ais.symon.admin

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
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🔐 এডমিন প্যানেল", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E),
                    titleContentColor = Color(0xFF4FC3F7)
                )
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
            // Message
            state.message?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(msg, color = Color.White, fontSize = 14.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // ======== ইউজার ম্যানেজমেন্ট ========
            Text("👥 ইউজার ম্যানেজমেন্ট", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            
            state.users.forEach { user ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(user.displayName.ifBlank { "Unknown" }, color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text(user.email, color = Color(0xFF90CAF9), fontSize = 12.sp)
                            Text("UID: ${user.uid.take(12)}...", color = Color(0xFF607D8B), fontSize = 10.sp)
                            Row {
                                if (user.isAdmin) Text("👑 Admin • ", color = Color(0xFFFFD700), fontSize = 11.sp)
                                if (user.isPremium) Text("⭐ Premium • ", color = Color(0xFF4CAF50), fontSize = 11.sp)
                                if (user.isBanned) Text("🚫 Banned", color = Color(0xFFFF5252), fontSize = 11.sp)
                            }
                        }
                        if (!user.isAdmin) {
                            if (user.isBanned) {
                                IconButton(onClick = { viewModel.unbanUser(user.uid) }) {
                                    Icon(Icons.Default.LockOpen, null, tint = Color(0xFF4CAF50))
                                }
                            } else {
                                IconButton(onClick = { viewModel.banUser(user.uid) }) {
                                    Icon(Icons.Default.Block, null, tint = Color(0xFFFF5252))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ======== প্রিমিয়াম টুলস ========
            Text("⭐ প্রিমিয়াম টুলস", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            
            listOf("ipscanner", "wifi_scanner", "wifi_deauth", "wifi_analyzer", "wifi_audit").forEach { tool ->
                var isPremium by remember { mutableStateOf(false) }
                LaunchedEffect(tool) { /* check premium status from FB */ }
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(tool.replace("_", " ").uppercase(), color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = isPremium,
                        onCheckedChange = { viewModel.setPremium(tool, it) },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF4CAF50))
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ======== পেমেন্ট সেটিংস ========
            Text("💳 পেমেন্ট সেটিংস", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = state.bKashNumber,
                        onValueChange = { viewModel.updateBkash(it) },
                        label = { Text("বিকাশ নাম্বার") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        singleLine = true, modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.nagadNumber,
                        onValueChange = { viewModel.updateNagad(it) },
                        label = { Text("নগদ নাম্বার") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        singleLine = true, modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ======== কন্টাক্ট সেটিংস ========
            Text("📞 প্রশাসকের যোগাযোগ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = state.telegram,
                        onValueChange = { viewModel.updateTelegram(it) },
                        label = { Text("টেলিগ্রাম আইডি") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.whatsapp,
                        onValueChange = { viewModel.updateWhatsApp(it) },
                        label = { Text("হোয়াটসঅ্যাপ নাম্বার") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }
            }
            
            Spacer(Modifier.height(40.dp))
        }
    }
}
