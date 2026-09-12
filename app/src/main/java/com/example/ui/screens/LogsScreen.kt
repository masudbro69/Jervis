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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ExecutionLogEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaStatusRed
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaSurfaceElevated
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LogsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val dbLogs by viewModel.logs.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = MakimaCrimson,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TASK HISTORY LOGS",
                                fontWeight = FontWeight.Bold,
                                color = MakimaTextPrimary,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = "Permanent audit log of all agent runs and reasoning",
                            color = MakimaTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    if (dbLogs.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearLogs() }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear History",
                                tint = MakimaStatusRed
                            )
                        }
                    }
                }
            }
        }

        if (dbLogs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recorded history yet. Run a command from Command Hub!",
                        color = MakimaTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(dbLogs) { log ->
                LogHistoryCardItem(log = log)
            }
        }
    }
}

@Composable
private fun LogHistoryCardItem(log: ExecutionLogEntity) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
    val dateString = dateFormat.format(Date(log.startedAt))

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
                    text = log.workflowName,
                    fontWeight = FontWeight.Bold,
                    color = MakimaTextPrimary,
                    fontSize = 14.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (log.status == "COMPLETED") MakimaStatusGreen.copy(alpha = 0.2f) else MakimaStatusRed.copy(
                                alpha = 0.2f
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = log.status,
                        color = if (log.status == "COMPLETED") MakimaStatusGreen else MakimaStatusRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Command: \"${log.command}\"",
                fontSize = 12.sp,
                color = MakimaCrimson
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Steps: ${log.completedSteps}/${log.totalSteps} Completed",
                    fontSize = 11.sp,
                    color = MakimaTextSecondary
                )
                Text(
                    text = "Duration: ${(log.durationMs / 1000f)}s • $dateString",
                    fontSize = 11.sp,
                    color = MakimaTextSecondary
                )
            }
        }
    }
}
