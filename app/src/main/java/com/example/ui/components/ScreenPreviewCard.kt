package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.models.ScreenFrame
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaDarkRed
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaSurfaceElevated
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary

@Composable
fun ScreenPreviewCard(
    screenFrame: ScreenFrame?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaPulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("screen_preview_card"),
        shape = RoundedCornerShape(20.dp),
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
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = "Device View",
                        tint = MakimaCrimson,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE SCREEN & VISION AI",
                        fontWeight = FontWeight.Bold,
                        color = MakimaTextPrimary,
                        fontSize = 14.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MakimaStatusGreen.copy(alpha = alphaPulse))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = screenFrame?.appName ?: "Standby",
                        color = MakimaCrimson,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Device Display Mock Frame with Vision Node Bounding Boxes
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MakimaSurfaceElevated)
                    .border(1.dp, MakimaCrimson.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawRect(color = Color(0xFF0D0A12))

                    drawRect(
                        color = Color(0xFF1E1520),
                        size = androidx.compose.ui.geometry.Size(width, height * 0.18f)
                    )

                    screenFrame?.nodes?.forEachIndexed { index, node ->
                        val left = node.bounds.left / 400f * width
                        val top = node.bounds.top / 700f * height
                        val right = node.bounds.right / 400f * width
                        val bottom = node.bounds.bottom / 700f * height

                        drawRect(
                            color = if (index == 1) Color(0xFFE63946) else Color(0xFF4ADE80),
                            topLeft = androidx.compose.ui.geometry.Offset(left, top),
                            size = androidx.compose.ui.geometry.Size(right - left, bottom - top),
                            style = Stroke(width = 3f)
                        )
                    }

                    drawCircle(
                        color = Color(0xFFE63946).copy(alpha = alphaPulse),
                        center = androidx.compose.ui.geometry.Offset(width * 0.75f, height * 0.65f),
                        radius = 24f * alphaPulse
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "App: ${screenFrame?.appName ?: "Android System"}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "UI Nodes Detected: ${screenFrame?.nodes?.size ?: 0}",
                        color = MakimaCrimson,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = screenFrame?.statusText ?: "Vision OCR Active • Observing UI Hierarchy",
                color = MakimaTextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
