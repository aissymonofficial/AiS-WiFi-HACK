package com.ais.symon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(onBack: () -> Unit, onResults: (String) -> Unit) {
    var ip by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf("normal") }
    var selectedCategory by remember { mutableStateOf("all") }
    var isScanning by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IP Scanner") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F1923)).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Text("টার্গেট আইপি / সাবনেট", color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = ip,
                onValueChange = { ip = it },
                placeholder = { Text("যেমন: 192.168.1.1 বা 192.168.1.0/24", color = Color(0xFF607D8B)) },
                leadingIcon = { Icon(Icons.Default.Language, null, tint = Color(0xFF4FC3F7)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF4FC3F7), unfocusedBorderColor = Color(0xFF37474F)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))

            Text("স্ক্যান স্পীড", color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = selectedMode == "normal", onClick = { selectedMode = "normal" }, label = { Text("নরমাল") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF4FC3F7)))
                FilterChip(selected = selectedMode == "fast", onClick = { selectedMode = "fast" }, label = { Text("হাই স্পীড") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFF9800)))
            }
            Spacer(Modifier.height(16.dp))

            Text("ক্যাটাগরি", color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = selectedCategory == "all", onClick = { selectedCategory = "all" }, label = { Text("সব") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF4FC3F7)))
                FilterChip(selected = selectedCategory == "camera", onClick = { selectedCategory = "camera" }, label = { Text("শুধু ক্যামেরা") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF4CAF50)))
                FilterChip(selected = selectedCategory == "password", onClick = { selectedCategory = "password" }, label = { Text("পাসওয়ার্ড") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFF5252)))
            }
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    isScanning = true
                    onResults(selectedCategory)
                },
                enabled = ip.isNotBlank() && !isScanning,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7))
            ) {
                if (isScanning) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                else { Icon(Icons.Default.Search, null); Spacer(Modifier.width(8.dp)); Text("স্ক্যান শুরু করুন", fontWeight = FontWeight.Bold) }
            }
        }
    }
}
