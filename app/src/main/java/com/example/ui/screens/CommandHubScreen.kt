package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.models.ExecutionState
import com.example.agent.models.WorkflowStep
import com.example.ui.MainViewModel
import com.example.ui.components.LogItemView
import com.example.ui.components.ScreenPreviewCard
import com.example.ui.theme.JarvisBorderGlow
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisElectricBlue
import com.example.ui.theme.JarvisStatusGreen
import com.example.ui.theme.JarvisSurfaceDark
import com.example.ui.theme.JarvisSurfaceElevated
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary

@Composable
fun CommandHubScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var commandInput by remember { mutableStateOf("") }
    val executionState by viewModel.executionState.collectAsState()
    val activeSteps by viewModel.activeSteps.collectAsState()
    val logs by viewModel.executionLogs.collectAsState()
    val currentStepIndex by viewModel.currentStepIndex.collectAsState()
    val screenFrame by viewModel.automationEngine.currentScreen.collectAsState()

    var isListeningVoice by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Express Command Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("command_input_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = JarvisSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, JarvisCyan)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = JarvisCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COMMAND AGENT OS",
                            fontWeight = FontWeight.Bold,
                            color = JarvisTextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedTextField(
                        value = commandInput,
                        onValueChange = { commandInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("main_prompt_input"),
                        placeholder = {
                            Text(
                                "e.g. Download Rednote video -> Generate Hindi voice -> Add subtitles -> Upload to YouTube",
                                fontSize = 12.sp,
                                color = JarvisTextSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JarvisCyan,
                            unfocusedBorderColor = JarvisBorderGlow,
                            focusedTextColor = JarvisTextPrimary,
                            unfocusedTextColor = JarvisTextPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "One command. Autonomous execution.",
                            fontSize = 11.sp,
                            color = JarvisTextSecondary
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    isListeningVoice = !isListeningVoice
                                    if (isListeningVoice) {
                                        commandInput = "Open WhatsApp and send message to Mom saying I am on my way"
                                    }
                                },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clip(CircleShape)
                                    .background(if (isListeningVoice) Color.Red else JarvisSurfaceElevated)
                                    .border(1.dp, if (isListeningVoice) Color.Red else JarvisCyan, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isListeningVoice) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Voice Input",
                                    tint = if (isListeningVoice) Color.White else JarvisCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (commandInput.isNotBlank()) {
                                        viewModel.runPromptCommand(commandInput)
                                    }
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(JarvisCyan)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .testTag("run_command_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Execute",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Execute",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Preset Workflows
        item {
            Text(
                text = "FEATURED PIPELINES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = JarvisTextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickCommandCard(
                    title = "Rednote to Hindi YouTube Short",
                    subtitle = "Download video -> Hindi TTS -> CapCut Subtitles -> YouTube Upload",
                    onClick = {
                        commandInput = "Download latest Rednote video, generate Hindi AI voice, replace audio, auto-burn subtitles, and upload to YouTube Shorts"
                        viewModel.runPromptCommand(commandInput)
                    }
                )
                QuickCommandCard(
                    title = "Gallery Vision AI Clean & Group",
                    subtitle = "Scan photos -> Detect duplicates & receipts -> Create albums",
                    onClick = {
                        commandInput = "Organize gallery photos using Vision AI into receipts, travel, and documents"
                        viewModel.runPromptCommand(commandInput)
                    }
                )
            }
        }

        // Live Device Screen & Vision AI
        item {
            ScreenPreviewCard(screenFrame = screenFrame)
        }

        // Active Workflow Steps Pipeline Progress
        if (activeSteps.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = JarvisSurfaceDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, JarvisBorderGlow)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AUTOMATION PIPELINE (${activeSteps.size} STEPS)",
                                fontWeight = FontWeight.Bold,
                                color = JarvisTextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Step ${currentStepIndex + 1}/${activeSteps.size}",
                                color = JarvisCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val progress = if (activeSteps.isEmpty()) 0f else (currentStepIndex + 1).toFloat() / activeSteps.size
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = JarvisCyan,
                            trackColor = JarvisSurfaceElevated
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        activeSteps.forEach { step ->
                            PipelineStepRow(step = step)
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        // Live Execution Logs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = JarvisSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, JarvisBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "REAL-TIME EXECUTION LOGS",
                        fontWeight = FontWeight.Bold,
                        color = JarvisTextPrimary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    if (logs.isEmpty()) {
                        Text(
                            text = "Waiting for agent command...",
                            color = JarvisTextSecondary,
                            fontSize = 12.sp
                        )
                    } else {
                        logs.takeLast(6).forEach { logMsg ->
                            LogItemView(logMessage = logMsg)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickCommandCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = JarvisSurfaceElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, JarvisBorderGlow)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = JarvisTextPrimary,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    color = JarvisTextSecondary,
                    fontSize = 11.sp
                )
            }
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = JarvisCyan,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PipelineStepRow(step: WorkflowStep) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (step.isCurrent) JarvisCyan.copy(alpha = 0.15f) else JarvisSurfaceElevated)
            .border(
                1.dp,
                if (step.isCurrent) JarvisCyan else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Icon(
            imageVector = if (step.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (step.isCompleted) JarvisStatusGreen else if (step.isCurrent) JarvisCyan else JarvisTextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "${step.stepIndex}. ${step.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (step.isCurrent) JarvisCyan else JarvisTextPrimary
            )
            Text(
                text = "${step.appName} • ${step.description}",
                fontSize = 11.sp,
                color = JarvisTextSecondary
            )
        }
    }
}
