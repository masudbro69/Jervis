package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.models.ExecutionState
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaGold
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaStatusOrange
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary

@Composable
fun JarvisHeader(
    executionState: ExecutionState,
    isFloatingHudActive: Boolean,
    onToggleHud: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor by animateColorAsState(
        targetValue = when (executionState) {
            ExecutionState.EXECUTING, ExecutionState.PLANNING, ExecutionState.REASONING -> MakimaCrimson
            ExecutionState.COMPLETED -> MakimaStatusGreen
            ExecutionState.PAUSED -> MakimaStatusOrange
            else -> MakimaTextSecondary
        },
        label = "statusColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MakimaSurfaceDark)
            .border(1.dp, MakimaBorderGlow, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MakimaCrimson.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Makima Core",
                            tint = MakimaCrimson,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "MAKIMA AGENT OS",
                            color = MakimaTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = executionState.name,
                                color = statusColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isFloatingHudActive) MakimaCrimson.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, if (isFloatingHudActive) MakimaCrimson else MakimaBorderGlow, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = onToggleHud,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("hud_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isFloatingHudActive) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                contentDescription = "Toggle Floating HUD",
                                tint = if (isFloatingHudActive) MakimaCrimson else MakimaTextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sub-status indicator pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderPill(icon = Icons.Default.Memory, label = "Memory Engine", value = "Online")
                HeaderPill(icon = Icons.Default.Layers, label = "Plugins Active", value = "10 Apps")
                HeaderPill(icon = Icons.Default.AutoAwesome, label = "Vision AI", value = "FPS 60")
            }
        }
    }
}

@Composable
private fun HeaderPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MakimaCrimson,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: ",
            fontSize = 11.sp,
            color = MakimaTextSecondary
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MakimaTextPrimary
        )
    }
}
