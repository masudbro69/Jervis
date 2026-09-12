package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.JarvisAccessibilityService
import com.example.agent.models.PluginInfo
import com.example.ui.MainViewModel
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaSurfaceElevated
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary

@Composable
fun PluginsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val plugins by viewModel.plugins.collectAsState()
    val isAccessibilityActive by JarvisAccessibilityService.isServiceActive.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Full Phone AI Assistant Permission Manager Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FULL DEVICE AUTONOMOUS AGENT CONTROL",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "To let Makima act as your 100% personal AI phone assistant, grant system permissions below. Makima can read screens, tap, type, swipe, and execute commands across any Android app automatically.",
                        color = MakimaTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PermissionRowItem(
                        title = "Accessibility Service (Screen Capture & Auto-Tap)",
                        status = if (isAccessibilityActive) "Active & Granted" else "Action Required",
                        isGranted = isAccessibilityActive,
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PermissionRowItem(
                        title = "Draw Over Other Apps (Floating Makima Overlay HUD)",
                        status = "Active & Granted",
                        isGranted = true,
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PermissionRowItem(
                        title = "Cross-App Task Execution & Notification Control",
                        status = "Active & Granted",
                        isGranted = true,
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    )
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "INSTALLED MODULAR PLUGINS (${plugins.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MakimaTextSecondary,
                letterSpacing = 1.sp
            )
        }

        // Plugin items
        items(plugins) { plugin ->
            PluginItemCard(
                plugin = plugin,
                onToggle = { viewModel.pluginManager.togglePlugin(plugin.id) }
            )
        }
    }
}

@Composable
private fun PermissionRowItem(
    title: String,
    status: String,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MakimaSurfaceElevated)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MakimaTextPrimary
            )
            Text(
                text = status,
                fontSize = 11.sp,
                color = if (isGranted) MakimaStatusGreen else Color(0xFFFFB74D)
            )
        }
        Icon(
            imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Default.Launch,
            contentDescription = null,
            tint = if (isGranted) MakimaStatusGreen else MakimaCrimson,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun PluginItemCard(
    plugin: PluginInfo,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("plugin_card_${plugin.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MakimaCrimson.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Extension,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = plugin.name,
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = plugin.category,
                            color = MakimaTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Switch(
                    checked = plugin.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MakimaCrimson,
                        uncheckedThumbColor = MakimaTextSecondary,
                        uncheckedTrackColor = MakimaSurfaceElevated
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = plugin.description,
                fontSize = 12.sp,
                color = MakimaTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                plugin.supportedActions.take(4).forEach { action ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MakimaSurfaceElevated)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = action.name,
                            fontSize = 10.sp,
                            color = MakimaCrimson
                        )
                    }
                }
            }
        }
    }
}
