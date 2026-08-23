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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⭐ প্রিমিয়াম") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color(0xFFFFD700))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0F1923))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Icon(Icons.Default.Star, null, modifier = Modifier.size(72.dp), tint = Color(0xFFFFD700))
            Spacer(Modifier.height(12.dp))
            Text("NetPulse প্রিমিয়াম", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("আনলক করুন সব প্রিমিয়াম ফিচার", color = Color(0xFF90CAF9))

            Spacer(Modifier.height(24.dp))

            listOf(
                "🚀 হাই-স্পিড স্ক্যান মোড",
                "📊 ডিটেইলড সিকিউরিটি রিপোর্ট",
                "📁 CSV/JSON/PDF রিপোর্ট এক্সপোর্ট",
                "🔍 প্রিমিয়াম ক্রেডেনশিয়াল ডেটাবেস",
                "⚡ এডভান্সড WiFi অ্যানালাইসিস",
                "❌ কোনো অ্যাডভারটাইজমেন্ট"
            ).forEach { feature ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(feature, color = Color.White, fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("পেমেন্ট করুন", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(12.dp))
                    Text("বিকাশ / নগদ", fontSize = 16.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    // These will load from Firebase Admin
                    Text("বিকাশ: 01XXXXXXXXX", color = Color.White, fontSize = 14.sp)
                    Text("নগদ: 01XXXXXXXXX", color = Color.White, fontSize = 14.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("পেমেন্ট করার后在ামেইল সহ টেলিগ্রামে জানান:", color = Color(0xFFB0BEC5), fontSize = 12.sp, textAlign = TextAlign.Center)
                    Text("@AiS_SYMON", color = Color(0xFF4FC3F7), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { /* Copy number to clipboard */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text("নাম্বার কপি করুন", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
