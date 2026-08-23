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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ToolItem(
    val name: String,
    val icon: ImageVector,
    val description: String,
    val color: Color,
    val route: String,
    val isPremium: Boolean = false,
    val badge: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToScanner: () -> Unit,
    onNavigateToWiFiScanner: () -> Unit,
    onNavigateToDeauth: () -> Unit,
    onNavigateToAnalyzer: () -> Unit,
    onNavigateToAudit: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onLogout: () -> Unit,
    isAdmin: Boolean
) {
    var showDrawer by remember { mutableStateOf(false) }

    val tools = listOf(
        ToolItem("IP Scanner", Icons.Default.WifiFind, "সাবনেট স্ক্যান + ক্যামেরা ডিটেক্ট + ক্রেডেনশিয়াল", Color(0xFF1A73E8), "scanner"),
        ToolItem("WiFi Scanner", Icons.Default.NearMe, "আশেপাশের সব WiFi দেখুন + সিগন্যাল + চ্যানেল", Color(0xFF34A853), "wifiscanner"),
        ToolItem("Deauth Tool", Icons.Default.SignalWifiOff, "নন-রুট WiFi ডিসকানেক্ট টেস্ট", Color(0xFFEA4335), "wifideauth"),
        ToolItem("Analyzer", Icons.Default.ShowChart, "চ্যানেল অ্যানালাইসিস + ক্রাউডেডনেস চেক", Color(0xFFFBBC04), "wifianalyzer"),
        ToolItem("Security Audit", Icons.Default.Security, "নেটওয়ার্ক সিকিউরিটি স্কোর + রেকমেন্ডেশন", Color(0xFF9C27B0), "wifiaudit")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NetPulse", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B1220),
                    titleContentColor = Color(0xFF4FC3F7)
                ),
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = onNavigateToAdmin) {
                            Icon(Icons.Default.AdminPanelSettings, null, tint = Color(0xFFFFD700))
                        }
                    }
                    IconButton(onClick = { showDrawer = !showDrawer }) {
                        Icon(Icons.Default.MoreVert, null, tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0F1923))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A237E))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Security, null, modifier = Modifier.size(48.dp), tint = Color(0xFF4FC3F7))
                        Spacer(Modifier.height(8.dp))
                        Text("NetPulse Auditor", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("v2.0 | AiS SYMON", fontSize = 12.sp, color = Color(0xFF90CAF9))
                        Spacer(Modifier.height(12.dp))
                        Text("পাঁচটি শক্তিশালী টুল — সব নন-রুট ফোনে কাজ করে", fontSize = 13.sp, color = Color(0xFFB0BEC5), textAlign = TextAlign.Center)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Tools Grid
                tools.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { tool ->
                            ToolCard(
                                tool = tool,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    when (tool.route) {
                                        "scanner" -> onNavigateToScanner()
                                        "wifiscanner" -> onNavigateToWiFiScanner()
                                        "wifideauth" -> onNavigateToDeauth()
                                        "wifianalyzer" -> onNavigateToAnalyzer()
                                        "wifiaudit" -> onNavigateToAudit()
                                    }
                                }
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(20.dp))

                // Bottom buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToPremium,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFD700))
                    ) {
                        Icon(Icons.Default.Star, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("প্রিমিয়াম")
                    }
                    OutlinedButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF90CAF9))
                    ) {
                        Icon(Icons.Default.Settings, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("সেটিংস")
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            // Drawer overlay
            AnimatedVisibility(
                visible = showDrawer,
                modifier = Modifier.fillMaxSize()
            ) {
                // ... drawer content (settings, logout, etc.)
            }
        }
    }
}

@Composable
fun ToolCard(tool: ToolItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(tool.color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(tool.icon, null, tint = tool.color, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(tool.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, textAlign = TextAlign.Center)
            Text(tool.description.take(30) + "...", color = Color(0xFF90CAF9), fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 2)
            if (tool.isPremium) {
                Spacer(Modifier.height(4.dp))
                Text("⭐ PREMIUM", color = Color(0xFFFFD700), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            tool.badge?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, color = Color(0xFF4CAF50), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
