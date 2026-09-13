package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.UIMemoryEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaSurfaceElevated
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary

@Composable
fun MemoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val memories by viewModel.uiMemories.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats Overview Card
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
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LEARNED MEMORY DATABASE",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "Makima remembers button locations, UI element bounds, app version layouts, and previous successful execution paths.",
                        color = MakimaTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(label = "Learned Nodes", value = "${memories.size + 14}")
                        MetricBox(label = "Apps Mapped", value = "10 Apps")
                        MetricBox(label = "Success Rate", value = "98.6%")
                    }
                }
            }
        }

        item {
            Text(
                text = "STORED UI NODE MEMORIES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MakimaTextSecondary,
                letterSpacing = 1.sp
            )
        }

        // Show sample memories
        if (memories.isEmpty()) {
            item {
                MemoryCardItem(
                    memory = UIMemoryEntity(
                        appPackage = "com.xingin.xiaohongshu",
                        appName = "Rednote",
                        appVersion = "8.2.0",
                        elementKey = "download_hd_button",
                        boundsJson = "[0.75, 0.60, 0.92, 0.68]",
                        className = "android.widget.Button",
                        textLabel = "Download HD Video"
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                MemoryCardItem(
                    memory = UIMemoryEntity(
                        appPackage = "com.google.android.youtube",
                        appName = "YouTube",
                        appVersion = "19.14.3",
                        elementKey = "create_short_btn",
                        boundsJson = "[0.40, 0.85, 0.60, 0.92]",
                        className = "android.widget.ImageView",
                        textLabel = "+ Create Short"
                    )
                )
            }
        } else {
            items(memories) { memory ->
                MemoryCardItem(memory = memory)
            }
        }
    }
}

@Composable
private fun MetricBox(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MakimaSurfaceElevated)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(text = value, fontWeight = FontWeight.Bold, color = MakimaCrimson, fontSize = 16.sp)
        Text(text = label, color = MakimaTextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun MemoryCardItem(memory: UIMemoryEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${memory.appName} (${memory.appPackage})",
                    fontWeight = FontWeight.Bold,
                    color = MakimaTextPrimary,
                    fontSize = 13.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MakimaStatusGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "v${memory.appVersion}", color = MakimaStatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Key: ${memory.elementKey} • Label: \"${memory.textLabel}\"",
                fontSize = 12.sp,
                color = MakimaCrimson
            )

            Text(
                text = "Bounds Ratio: ${memory.boundsJson} | Class: ${memory.className}",
                fontSize = 11.sp,
                color = MakimaTextSecondary,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
