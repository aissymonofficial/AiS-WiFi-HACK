package com.ais.symon.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onRegisterSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B1220),
                        Color(0xFF1A237E)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF4FC3F7)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "নতুন অ্যাকাউন্ট",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("আপনার নাম") },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF90CAF9)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4FC3F7), unfocusedBorderColor = Color(0xFF37474F),
                            focusedLabelColor = Color(0xFF4FC3F7), unfocusedLabelColor = Color(0xFF90CAF9)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("ইমেইল") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF90CAF9)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4FC3F7), unfocusedBorderColor = Color(0xFF37474F),
                            focusedLabelColor = Color(0xFF4FC3F7), unfocusedLabelColor = Color(0xFF90CAF9)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("পাসওয়ার্ড (৬+ অক্ষর)") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF90CAF9)) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color(0xFF90CAF9))
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4FC3F7), unfocusedBorderColor = Color(0xFF37474F),
                            focusedLabelColor = Color(0xFF4FC3F7), unfocusedLabelColor = Color(0xFF90CAF9)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("পাসওয়ার্ড নিশ্চিত করুন") },
                        leadingIcon = { Icon(Icons.Default.LockReset, null, tint = Color(0xFF90CAF9)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4FC3F7), unfocusedBorderColor = Color(0xFF37474F),
                            focusedLabelColor = Color(0xFF4FC3F7), unfocusedLabelColor = Color(0xFF90CAF9)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    errorMsg?.let {
                        Text(it, color = Color(0xFFFF5252), fontSize = 12.sp)
                    }
                    state.error?.let {
                        Text(it, color = Color(0xFFFF5252), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            when {
                                name.isBlank() -> errorMsg = "নাম দিন"
                                email.isBlank() -> errorMsg = "ইমেইল দিন"
                                password.length < 6 -> errorMsg = "পাসওয়ার্ড কমপক্ষে ৬ অক্ষর"
                                password != confirmPassword -> errorMsg = "পাসওয়ার্ড মিলছে না"
                                else -> {
                                    errorMsg = null
                                    viewModel.register(email.trim(), password, name.trim())
                                }
                            }
                        },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7))
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else Text("রেজিস্টার করুন", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("ইতিমধ্যে অ্যাকাউন্ট আছে? লগইন করুন",
                color = Color(0xFF4FC3F7), fontSize = 14.sp,
                modifier = Modifier.clickable { viewModel.clearError(); onNavigateToLogin() })

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
