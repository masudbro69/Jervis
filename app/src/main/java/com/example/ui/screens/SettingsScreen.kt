package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.network.GeminiNetwork
import com.example.ui.MainViewModel
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaGold
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaStatusOrange
import com.example.ui.theme.MakimaStatusRed
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaSurfaceElevated
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedModel by viewModel.selectedModel.collectAsState()
    val openCodeZenApiKey by viewModel.openCodeZenApiKey.collectAsState()
    val openCodeZenBaseUrl by viewModel.openCodeZenBaseUrl.collectAsState()

    var apiKeyInput by remember(openCodeZenApiKey) { mutableStateOf(openCodeZenApiKey) }
    var baseUrlInput by remember(openCodeZenBaseUrl) { mutableStateOf(openCodeZenBaseUrl) }

    // Gemini API Key state
    var geminiKeyInput by remember { mutableStateOf(GeminiNetwork.getGeminiApiKey()) }
    var showGeminiKey by remember { mutableStateOf(false) }
    var geminiSaved by remember { mutableStateOf(false) }

    // Xkiro API Key state
    var xkiroKeyInput by remember { mutableStateOf(GeminiNetwork.getXkiroApiKey()) }
    var xkiroBaseUrlInput by remember { mutableStateOf(GeminiNetwork.getXkiroBaseUrl()) }
    var xkiroModelInput by remember { mutableStateOf(GeminiNetwork.getXkiroModel()) }
    var showXkiroKey by remember { mutableStateOf(false) }
    var xkiroSaved by remember { mutableStateOf(false) }
    var xkiroTestMsg by remember { mutableStateOf<String?>(null) }

    // Active provider
    var activeProvider by remember { mutableStateOf(GeminiNetwork.getActiveProvider()) }

    var gestureDelay by remember { mutableFloatStateOf(1000f) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ─────────────────────────────────────────────────────────
        // AI PROVIDER SELECTION
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaCrimson)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ACTIVE AI PROVIDER",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Choose which AI engine powers Makima's reasoning",
                        color = MakimaTextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gemini option
                    ProviderOptionRow(
                        title = "Google Gemini",
                        subtitle = if (GeminiNetwork.hasGeminiKey()) "✅ API Key configured" else "⚠️ API Key not set",
                        isSelected = activeProvider == "gemini",
                        hasKey = GeminiNetwork.hasGeminiKey(),
                        onSelect = {
                            activeProvider = "gemini"
                            GeminiNetwork.setActiveProvider("gemini")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Xkiro option
                    ProviderOptionRow(
                        title = "Xkiro AI",
                        subtitle = if (GeminiNetwork.hasXkiroKey()) "✅ API Key configured" else "⚠️ API Key not set",
                        isSelected = activeProvider == "xkiro",
                        hasKey = GeminiNetwork.hasXkiroKey(),
                        onSelect = {
                            activeProvider = "xkiro"
                            GeminiNetwork.setActiveProvider("xkiro")
                        }
                    )
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // GEMINI API KEY CONFIGURATION
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (activeProvider == "gemini") MakimaCrimson else MakimaBorderGlow
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("GEMINI API KEY", fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 14.sp)
                            Text(
                                text = "Get from ai.google.dev → API Keys",
                                color = MakimaTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        if (GeminiNetwork.hasGeminiKey()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MakimaStatusGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Active", color = MakimaStatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MakimaStatusOrange.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Not Set", color = MakimaStatusOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = geminiKeyInput,
                        onValueChange = { geminiKeyInput = it; geminiSaved = false },
                        label = { Text("Gemini API Key", color = MakimaTextSecondary, fontSize = 12.sp) },
                        placeholder = { Text("AIza...", color = MakimaTextSecondary.copy(alpha = 0.5f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (showGeminiKey) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showGeminiKey = !showGeminiKey }) {
                                Icon(
                                    if (showGeminiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle visibility",
                                    tint = MakimaTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaCrimson,
                            unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary,
                            unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated,
                            unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                GeminiNetwork.setGeminiApiKey(geminiKeyInput.trim())
                                geminiSaved = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MakimaCrimson),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save Key", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        if (geminiSaved) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, null, tint = MakimaStatusGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Saved!", color = MakimaStatusGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Test connection
                    Button(
                        onClick = {
                            xkiroTestMsg = null
                            // Simple test
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MakimaSurfaceElevated),
                        shape = RoundedCornerShape(10.dp),
                        enabled = GeminiNetwork.hasGeminiKey()
                    ) {
                        Text("Test Gemini Connection", color = MakimaCrimson, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "• Go to ai.google.dev\n• Create or select a project\n• Go to API Keys → Create API Key\n• Copy and paste above",
                        color = MakimaTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // XKIRO API KEY CONFIGURATION
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (activeProvider == "xkiro") MakimaGold else MakimaBorderGlow
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, null, tint = MakimaGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("XKIRO API CONFIGURATION", fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 14.sp)
                            Text(
                                text = "OpenAI-compatible endpoint (Xkiro, OpenRouter, etc.)",
                                color = MakimaTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        if (GeminiNetwork.hasXkiroKey()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MakimaStatusGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Active", color = MakimaStatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Xkiro API Key
                    OutlinedTextField(
                        value = xkiroKeyInput,
                        onValueChange = { xkiroKeyInput = it; xkiroSaved = false },
                        label = { Text("Xkiro API Key", color = MakimaTextSecondary, fontSize = 12.sp) },
                        placeholder = { Text("sk-xkiro-...", color = MakimaTextSecondary.copy(alpha = 0.5f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (showXkiroKey) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showXkiroKey = !showXkiroKey }) {
                                Icon(
                                    if (showXkiroKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle visibility",
                                    tint = MakimaTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaGold,
                            unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary,
                            unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated,
                            unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Xkiro Base URL
                    OutlinedTextField(
                        value = xkiroBaseUrlInput,
                        onValueChange = { xkiroBaseUrlInput = it; xkiroSaved = false },
                        label = { Text("Xkiro Base URL", color = MakimaTextSecondary, fontSize = 12.sp) },
                        placeholder = { Text("https://api.xkiro.com/v1", color = MakimaTextSecondary.copy(alpha = 0.5f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaGold,
                            unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary,
                            unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated,
                            unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Xkiro Model
                    OutlinedTextField(
                        value = xkiroModelInput,
                        onValueChange = { xkiroModelInput = it; xkiroSaved = false },
                        label = { Text("Xkiro Model", color = MakimaTextSecondary, fontSize = 12.sp) },
                        placeholder = { Text("xkiro-auto", color = MakimaTextSecondary.copy(alpha = 0.5f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaGold,
                            unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary,
                            unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated,
                            unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                GeminiNetwork.setXkiroApiKey(xkiroKeyInput.trim())
                                GeminiNetwork.setXkiroBaseUrl(xkiroBaseUrlInput.trim())
                                GeminiNetwork.setXkiroModel(xkiroModelInput.trim())
                                xkiroSaved = true
                                xkiroTestMsg = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MakimaGold),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp), tint = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save Xkiro Config", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        if (xkiroSaved) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, null, tint = MakimaStatusGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Saved!", color = MakimaStatusGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Test connection
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            xkiroTestMsg = "Testing..."
                            // Save first
                            GeminiNetwork.setXkiroApiKey(xkiroKeyInput.trim())
                            GeminiNetwork.setXkiroBaseUrl(xkiroBaseUrlInput.trim())
                            GeminiNetwork.setXkiroModel(xkiroModelInput.trim())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MakimaSurfaceElevated),
                        shape = RoundedCornerShape(10.dp),
                        enabled = xkiroKeyInput.isNotBlank()
                    ) {
                        Text("Test Xkiro Connection", color = MakimaGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    xkiroTestMsg?.let { msg ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(msg, color = MakimaStatusGreen, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Supports any OpenAI-compatible endpoint\n• Works with: Xkiro, OpenRouter, Together AI, Groq, etc.\n• Set Base URL to the provider's /v1 endpoint\n• Set Model to the model ID (e.g. gpt-4o, llama-3.3-70b)",
                        color = MakimaTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // API STATUS OVERVIEW
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("API STATUS", fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    StatusRow("Active Provider", activeProvider.uppercase(), MakimaCrimson)
                    StatusRow("Gemini API Key", if (GeminiNetwork.hasGeminiKey()) "✅ Configured" else "❌ Not Set", if (GeminiNetwork.hasGeminiKey()) MakimaStatusGreen else MakimaStatusRed)
                    StatusRow("Xkiro API Key", if (GeminiNetwork.hasXkiroKey()) "✅ Configured" else "❌ Not Set", if (GeminiNetwork.hasXkiroKey()) MakimaStatusGreen else MakimaStatusRed)
                    if (GeminiNetwork.hasXkiroKey()) {
                        StatusRow("Xkiro Endpoint", GeminiNetwork.getXkiroBaseUrl(), MakimaTextSecondary)
                        StatusRow("Xkiro Model", GeminiNetwork.getXkiroModel(), MakimaTextSecondary)
                    }
                    StatusRow("Fallback", if (GeminiNetwork.hasAnyApiKey()) "Auto-switch enabled" else "⚠️ No API keys", if (GeminiNetwork.hasAnyApiKey()) MakimaStatusGreen else MakimaStatusOrange)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Keys are stored locally on your device and never shared.",
                        color = MakimaTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // OPENCODE ZEN API
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cloud, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OPENCODE ZEN API & FREE MODELS", fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it; viewModel.updateOpenCodeZenApiKey(it) },
                        label = { Text("OpenCode Zen API Key", color = MakimaTextSecondary, fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaCrimson, unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary, unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated, unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = baseUrlInput,
                        onValueChange = { baseUrlInput = it; viewModel.updateOpenCodeZenBaseUrl(it) },
                        label = { Text("OpenCode Zen Base URL", color = MakimaTextSecondary, fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaCrimson, unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary, unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated, unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // AI MODEL SELECTION
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI REASONING ENGINE MODEL", fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    listOf(
                        Triple("omniroute-auto-free", "OmniRoute Auto Free", "Unlimited auto-failover routing"),
                        Triple("openrouter-free-omni", "OpenRouter Free Omni", "Free tier Llama 3.3 / DeepSeek R1"),
                        Triple("big-pickle", "Big Pickle (OpenCode Zen)", "200k context, agentic reasoning"),
                        Triple("deepseek-v4-flash-free", "DeepSeek V4 Flash Free", "Ultra-fast open weights"),
                        Triple("minimax-m2.5-free", "MiniMax M2.5 Free", "Multi-step execution"),
                        Triple("minimax-m3-free", "MiniMax M3 Free", "1M token context"),
                        Triple("gemini-3.5-flash", "Gemini 3.5 Flash", "Multimodal vision & reasoning"),
                        Triple("gemini-3.1-pro-preview", "Gemini 3.1 Pro", "Complex task reasoning"),
                        Triple("local_engine", "Local On-Device", "Offline fallback")
                    ).forEach { (id, title, subtitle) ->
                        ModelOptionRow(
                            title = title, subtitle = subtitle,
                            isSelected = selectedModel == id,
                            onSelect = { viewModel.updateSelectedModel(id) }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // GESTURE SPEED
        // ─────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GESTURE LATENCY & SPEED", fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Step Latency Delay", color = MakimaTextPrimary, fontSize = 13.sp)
                        Text("${gestureDelay.toInt()} ms", color = MakimaCrimson, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Slider(
                        value = gestureDelay, onValueChange = { gestureDelay = it }, valueRange = 300f..3000f,
                        colors = SliderDefaults.colors(thumbColor = MakimaCrimson, activeTrackColor = MakimaCrimson, inactiveTrackColor = MakimaSurfaceElevated)
                    )
                }
            }
        }
    }
}

// ── Helper Composables ───────────────────────────────────────────

@Composable
private fun ProviderOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    hasKey: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .background(if (isSelected) MakimaCrimson.copy(alpha = 0.12f) else MakimaSurfaceElevated)
            .border(1.dp, if (isSelected) MakimaCrimson else Color.Transparent, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = if (isSelected) MakimaCrimson else MakimaTextPrimary, fontSize = 13.sp)
            Text(subtitle, color = if (hasKey) MakimaStatusGreen else MakimaStatusOrange, fontSize = 11.sp)
        }
        Icon(
            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
            contentDescription = null, tint = if (isSelected) MakimaCrimson else MakimaTextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun StatusRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MakimaTextSecondary, fontSize = 12.sp)
        Text(value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ModelOptionRow(title: String, subtitle: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .background(if (isSelected) MakimaCrimson.copy(alpha = 0.12f) else MakimaSurfaceElevated)
            .border(1.dp, if (isSelected) MakimaCrimson else Color.Transparent, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = if (isSelected) MakimaCrimson else MakimaTextPrimary, fontSize = 13.sp)
            Text(subtitle, color = MakimaTextSecondary, fontSize = 11.sp)
        }
        Icon(
            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
            contentDescription = null, tint = if (isSelected) MakimaCrimson else MakimaTextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
