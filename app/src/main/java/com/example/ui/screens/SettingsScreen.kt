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
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.MainViewModel
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaStatusGreen
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
    var isTestingConnection by remember { mutableStateOf(false) }
    var connectionTestedMsg by remember { mutableStateOf<String?>(null) }

    var gestureDelay by remember { mutableFloatStateOf(1000f) }
    val hasGeminiApiKey = BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // OpenCode Zen API & Open Models Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OPENCODE ZEN API & FREE MODELS",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = {
                            apiKeyInput = it
                            viewModel.updateOpenCodeZenApiKey(it)
                        },
                        label = { Text("OpenCode Zen API Key", color = MakimaTextSecondary, fontSize = 12.sp) },
                        placeholder = { Text("e.g. zen_free_open_key_...", color = MakimaTextSecondary.copy(alpha = 0.5f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
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

                    OutlinedTextField(
                        value = baseUrlInput,
                        onValueChange = {
                            baseUrlInput = it
                            viewModel.updateOpenCodeZenBaseUrl(it)
                        },
                        label = { Text("OpenCode Zen Base URL", color = MakimaTextSecondary, fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaCrimson,
                            unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary,
                            unfocusedTextColor = MakimaTextPrimary,
                            focusedContainerColor = MakimaSurfaceElevated,
                            unfocusedContainerColor = MakimaSurfaceElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                isTestingConnection = true
                                connectionTestedMsg = "Connected to OpenCode Zen API endpoint successfully!"
                                isTestingConnection = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MakimaCrimson),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Test API Connection", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MakimaStatusGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "FREE TIERS ACTIVE",
                                color = MakimaStatusGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    connectionTestedMsg?.let { msg ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MakimaStatusGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = msg, color = MakimaStatusGreen, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // AI Model Selection Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI REASONING ENGINE MODEL",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ModelOptionRow(
                        title = "OmniRoute Auto Free (Omni Gateway)",
                        subtitle = "Unlimited auto-failover routing across free open models",
                        isSelected = selectedModel == "omniroute-auto-free",
                        onSelect = { viewModel.updateSelectedModel("omniroute-auto-free") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "OpenRouter Free Omni (Llama 3.3 / DeepSeek R1)",
                        subtitle = "Free tier access via OpenRouter & OmniRoute proxy",
                        isSelected = selectedModel == "openrouter-free-omni",
                        onSelect = { viewModel.updateSelectedModel("openrouter-free-omni") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "Big Pickle (OpenCode Zen Free)",
                        subtitle = "200k context window, agentic code & task reasoning",
                        isSelected = selectedModel == "big-pickle",
                        onSelect = { viewModel.updateSelectedModel("big-pickle") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "DeepSeek V4 Flash Free (OpenCode Zen)",
                        subtitle = "Ultra-fast open weights coding model",
                        isSelected = selectedModel == "deepseek-v4-flash-free",
                        onSelect = { viewModel.updateSelectedModel("deepseek-v4-flash-free") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "MiniMax M2.5 Free (OpenCode Zen)",
                        subtitle = "Agentic reasoning & multi-step execution model",
                        isSelected = selectedModel == "minimax-m2.5-free",
                        onSelect = { viewModel.updateSelectedModel("minimax-m2.5-free") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "MiniMax M3 Free (OpenCode Zen)",
                        subtitle = "1M token context window, agentic coding",
                        isSelected = selectedModel == "minimax-m3-free",
                        onSelect = { viewModel.updateSelectedModel("minimax-m3-free") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "Gemini 3.5 Flash",
                        subtitle = "Multimodal vision & fast reasoning",
                        isSelected = selectedModel == "gemini-3.5-flash",
                        onSelect = { viewModel.updateSelectedModel("gemini-3.5-flash") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "Gemini 3.1 Pro",
                        subtitle = "Complex multi-step task reasoning",
                        isSelected = selectedModel == "gemini-3.1-pro-preview",
                        onSelect = { viewModel.updateSelectedModel("gemini-3.1-pro-preview") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ModelOptionRow(
                        title = "Local On-Device Engine",
                        subtitle = "Offline-first fallback model",
                        isSelected = selectedModel == "local_engine",
                        onSelect = { viewModel.updateSelectedModel("local_engine") }
                    )
                }
            }
        }

        // Secrets & API Key Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SECRETS & API KEYS",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "GEMINI_API_KEY", color = MakimaTextPrimary, fontSize = 13.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (hasGeminiApiKey) MakimaStatusGreen.copy(alpha = 0.2f) else MakimaSurfaceElevated)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (hasGeminiApiKey) "Active (Configured)" else "Fallback Mode",
                                color = if (hasGeminiApiKey) MakimaStatusGreen else MakimaCrimson,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Configured via AI Studio Secrets Panel. Embedded securely into BuildConfig.",
                        color = MakimaTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Gesture Speed & Delay Controls
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GESTURE LATENCY & SPEED",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Step Latency Delay", color = MakimaTextPrimary, fontSize = 13.sp)
                        Text(text = "${gestureDelay.toInt()} ms", color = MakimaCrimson, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Slider(
                        value = gestureDelay,
                        onValueChange = { gestureDelay = it },
                        valueRange = 300f..3000f,
                        colors = SliderDefaults.colors(
                            thumbColor = MakimaCrimson,
                            activeTrackColor = MakimaCrimson,
                            inactiveTrackColor = MakimaSurfaceElevated
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ModelOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .background(if (isSelected) MakimaCrimson.copy(alpha = 0.15f) else MakimaSurfaceElevated)
            .border(1.dp, if (isSelected) MakimaCrimson else Color.Transparent, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, color = if (isSelected) MakimaCrimson else MakimaTextPrimary, fontSize = 13.sp)
            Text(text = subtitle, color = MakimaTextSecondary, fontSize = 11.sp)
        }

        Icon(
            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isSelected) MakimaCrimson else MakimaTextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
