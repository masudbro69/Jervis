package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
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
import com.example.agent.models.TaskSlot
import com.example.agent.models.TaskStatus
import com.example.agent.models.WorkflowStep
import com.example.ui.MainViewModel
import com.example.ui.components.LogItemView
import com.example.ui.components.MakimaCharacterPanel
import com.example.ui.components.ScreenPreviewCard
import com.example.ui.theme.MakimaBorderGlow
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaDarkRed
import com.example.ui.theme.MakimaGold
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaStatusOrange
import com.example.ui.theme.MakimaStatusRed
import com.example.ui.theme.MakimaSurfaceDark
import com.example.ui.theme.MakimaSurfaceElevated
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary
import kotlin.math.sin

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

    // Voice state
    val isListening by viewModel.isListening.collectAsState()
    val isWakeWordActive by viewModel.isWakeWordActive.collectAsState()
    val partialText by viewModel.partialVoiceText.collectAsState()
    val voiceAmplitude by viewModel.voiceAmplitude.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    // Multitask state
    val taskSlots by viewModel.taskSlots.collectAsState()
    val activeTaskCount by viewModel.activeTaskCount.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 3D Animated Makima Character Panel
        item { MakimaCharacterPanel() }

        // ── Voice Waveform Bar ───────────────────────────────────
        item {
            VoiceWaveformBar(
                isListening = isListening,
                isWakeWordActive = isWakeWordActive,
                isSpeaking = isSpeaking,
                amplitude = voiceAmplitude,
                partialText = partialText,
                onToggleListening = { viewModel.toggleVoiceListening() },
                onToggleWakeWord = { viewModel.toggleWakeWordMode() }
            )
        }

        // ── Command Input Card ───────────────────────────────────
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("command_input_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaCrimson)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MakimaCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COMMAND AGENT OS",
                            fontWeight = FontWeight.Bold,
                            color = MakimaTextPrimary,
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
                                color = MakimaTextSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakimaCrimson,
                            unfocusedBorderColor = MakimaBorderGlow,
                            focusedTextColor = MakimaTextPrimary,
                            unfocusedTextColor = MakimaTextPrimary
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
                            text = if (isListening) "🎙️ Listening..." else "One command. Autonomous execution.",
                            fontSize = 11.sp,
                            color = if (isListening) MakimaCrimson else MakimaTextSecondary
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Mic button
                            IconButton(
                                onClick = {
                                    viewModel.toggleVoiceListening()
                                },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clip(CircleShape)
                                    .background(if (isListening) MakimaCrimson else MakimaSurfaceElevated)
                                    .border(1.dp, if (isListening) MakimaCrimson else MakimaBorderGlow, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Voice Input",
                                    tint = if (isListening) Color.White else MakimaCrimson,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Execute button
                            IconButton(
                                onClick = {
                                    if (commandInput.isNotBlank()) {
                                        viewModel.runPromptCommand(commandInput)
                                    }
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MakimaCrimson)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .testTag("run_command_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Execute",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Execute",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Multitask Panel ──────────────────────────────────────
        if (taskSlots.isNotEmpty()) {
            item {
                MultitaskPanel(
                    taskSlots = taskSlots,
                    activeCount = activeTaskCount,
                    onCancelTask = { viewModel.cancelTask(it) },
                    onCancelAll = { viewModel.cancelAllTasks() },
                    onClearFinished = { viewModel.clearFinishedTasks() }
                )
            }
        }

        // ── Quick Preset Workflows ───────────────────────────────
        item {
            Text(
                text = "FEATURED PIPELINES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MakimaTextSecondary,
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

        // ── Live Screen Preview ──────────────────────────────────
        item { ScreenPreviewCard(screenFrame = screenFrame) }

        // ── Active Pipeline ──────────────────────────────────────
        if (activeSteps.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            Text(
                                text = "AUTOMATION PIPELINE (${activeSteps.size} STEPS)",
                                fontWeight = FontWeight.Bold,
                                color = MakimaTextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Step ${currentStepIndex + 1}/${activeSteps.size}",
                                color = MakimaCrimson,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        val progress = if (activeSteps.isEmpty()) 0f else (currentStepIndex + 1).toFloat() / activeSteps.size
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = MakimaCrimson,
                            trackColor = MakimaSurfaceElevated
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

        // ── Execution Logs ───────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "REAL-TIME EXECUTION LOGS",
                        fontWeight = FontWeight.Bold,
                        color = MakimaTextPrimary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    if (logs.isEmpty()) {
                        Text("Waiting for agent command...", color = MakimaTextSecondary, fontSize = 12.sp)
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

// ── Voice Waveform Bar ───────────────────────────────────────────
@Composable
private fun VoiceWaveformBar(
    isListening: Boolean,
    isWakeWordActive: Boolean,
    isSpeaking: Boolean,
    amplitude: Float,
    partialText: String,
    onToggleListening: () -> Unit,
    onToggleWakeWord: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2832f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    AnimatedVisibility(visible = isListening || isWakeWordActive || isSpeaking) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isListening) MakimaCrimson else MakimaBorderGlow
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isListening) MakimaCrimson
                                    else if (isSpeaking) MakimaGold
                                    else MakimaStatusGreen
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when {
                                isSpeaking -> "🔊 Makima Speaking..."
                                isListening -> "🎙️ Listening..."
                                isWakeWordActive -> "👁️ Wake Word Active — Say \"Hey Makima\""
                                else -> ""
                            },
                            color = MakimaTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row {
                        IconButton(onClick = onToggleWakeWord, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Wake Word",
                                tint = if (isWakeWordActive) MakimaGold else MakimaTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Waveform visualization
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MakimaSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val centerY = h / 2
                        val barCount = 40
                        val barWidth = w / barCount
                        val effectiveAmp = if (isListening) amplitude else if (isSpeaking) 0.5f else 0.1f

                        for (i in 0 until barCount) {
                            val x = i * barWidth + barWidth / 2
                            val wave = sin(wavePhase + i * 0.3).toFloat()
                            val barHeight = (h * 0.15f + h * 0.35f * effectiveAmp * (0.5f + 0.5f * wave)).coerceIn(2f, h * 0.9f)

                            drawRect(
                                color = if (isListening) MakimaCrimson.copy(alpha = 0.6f + 0.4f * effectiveAmp)
                                else MakimaGold.copy(alpha = 0.4f + 0.3f * effectiveAmp),
                                topLeft = androidx.compose.ui.geometry.Offset(x - 2f, centerY - barHeight / 2),
                                size = androidx.compose.ui.geometry.Size(4f, barHeight)
                            )
                        }
                    }
                }

                // Partial text
                if (partialText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"$partialText\"",
                        color = MakimaCrimson,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ── Multitask Panel ──────────────────────────────────────────────
@Composable
private fun MultitaskPanel(
    taskSlots: List<TaskSlot>,
    activeCount: Int,
    onCancelTask: (String) -> Unit,
    onCancelAll: () -> Unit,
    onClearFinished: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MakimaSurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, MakimaGold.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MULTITASK QUEUE",
                        fontWeight = FontWeight.Bold,
                        color = MakimaTextPrimary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "$activeCount active / ${taskSlots.size} total",
                        color = MakimaTextSecondary,
                        fontSize = 11.sp
                    )
                }
                Row {
                    IconButton(onClick = onClearFinished, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.CheckCircle, "Clear", tint = MakimaStatusGreen, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onCancelAll, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Cancel, "Cancel All", tint = MakimaStatusRed, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            taskSlots.forEach { task ->
                TaskSlotRow(task = task, onCancel = { onCancelTask(task.id) })
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun TaskSlotRow(task: TaskSlot, onCancel: () -> Unit) {
    val statusColor = when (task.status) {
        TaskStatus.RUNNING -> MakimaCrimson
        TaskStatus.COMPLETED -> MakimaStatusGreen
        TaskStatus.FAILED -> MakimaStatusRed
        TaskStatus.CANCELLED -> MakimaTextSecondary
        TaskStatus.QUEUED -> MakimaStatusOrange
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MakimaSurfaceElevated)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MakimaTextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = task.status.name,
                    fontSize = 10.sp,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (task.status == TaskStatus.RUNNING) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { task.progress },
                    modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
                    color = MakimaCrimson,
                    trackColor = MakimaBorderGlow
                )
            }
        }
        if (task.status == TaskStatus.RUNNING || task.status == TaskStatus.QUEUED) {
            IconButton(onClick = onCancel, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Stop, "Cancel", tint = MakimaStatusRed, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ── Quick Command Card ───────────────────────────────────────────
@Composable
private fun QuickCommandCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MakimaSurfaceElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, MakimaBorderGlow)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = MakimaTextPrimary, fontSize = 13.sp)
                Text(subtitle, color = MakimaTextSecondary, fontSize = 11.sp)
            }
            Icon(Icons.Default.PlayArrow, null, tint = MakimaCrimson, modifier = Modifier.size(20.dp))
        }
    }
}

// ── Pipeline Step Row ────────────────────────────────────────────
@Composable
private fun PipelineStepRow(step: WorkflowStep) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (step.isCurrent) MakimaCrimson.copy(alpha = 0.15f) else MakimaSurfaceElevated)
            .border(1.dp, if (step.isCurrent) MakimaCrimson else Color.Transparent, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Icon(
            imageVector = if (step.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (step.isCompleted) MakimaStatusGreen else if (step.isCurrent) MakimaCrimson else MakimaTextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "${step.stepIndex}. ${step.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (step.isCurrent) MakimaCrimson else MakimaTextPrimary
            )
            Text("${step.appName} • ${step.description}", fontSize = 11.sp, color = MakimaTextSecondary)
        }
    }
}
