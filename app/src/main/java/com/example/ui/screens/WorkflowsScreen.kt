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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.local.entities.WorkflowEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.JarvisBorderGlow
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisStatusRed
import com.example.ui.theme.JarvisSurfaceDark
import com.example.ui.theme.JarvisSurfaceElevated
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary

@Composable
fun WorkflowsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val workflows by viewModel.workflows.collectAsState()

    var showBuilder by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newPrompt by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = JarvisSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, JarvisBorderGlow)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "WORKFLOW AUTOMATION ENGINE",
                            fontWeight = FontWeight.Bold,
                            color = JarvisTextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Create, save & run autonomous multi-app pipelines",
                            color = JarvisTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { showBuilder = !showBuilder },
                        colors = ButtonDefaults.buttonColors(containerColor = JarvisCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("create_workflow_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (showBuilder) "Cancel" else "New Plan", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Custom Workflow Builder Dialog/Section
        if (showBuilder) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = JarvisSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, JarvisCyan)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "BUILD CUSTOM WORKFLOW",
                            fontWeight = FontWeight.Bold,
                            color = JarvisCyan,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Workflow Name", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JarvisCyan,
                                unfocusedBorderColor = JarvisBorderGlow,
                                focusedTextColor = JarvisTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("Description", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JarvisCyan,
                                unfocusedBorderColor = JarvisBorderGlow,
                                focusedTextColor = JarvisTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newPrompt,
                            onValueChange = { newPrompt = it },
                            label = { Text("Agent Natural Language Command", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = JarvisCyan,
                                unfocusedBorderColor = JarvisBorderGlow,
                                focusedTextColor = JarvisTextPrimary
                            ),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (newTitle.isNotBlank() && newPrompt.isNotBlank()) {
                                    viewModel.saveCustomWorkflow(newTitle, newDesc, newPrompt)
                                    newTitle = ""
                                    newDesc = ""
                                    newPrompt = ""
                                    showBuilder = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = JarvisCyan, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Workflow to Memory", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Saved Workflows List
        items(workflows) { workflow ->
            WorkflowCardItem(
                workflow = workflow,
                onRun = { viewModel.runPromptCommand(workflow.promptCommand) },
                onDelete = { viewModel.deleteWorkflow(workflow.id) }
            )
        }
    }
}

@Composable
private fun WorkflowCardItem(
    workflow: WorkflowEntity,
    onRun: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = JarvisSurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, JarvisBorderGlow)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workflow.title,
                        fontWeight = FontWeight.Bold,
                        color = JarvisTextPrimary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = workflow.description,
                        color = JarvisTextSecondary,
                        fontSize = 12.sp
                    )
                }

                Row {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = JarvisStatusRed)
                    }

                    IconButton(
                        onClick = onRun,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(JarvisCyan)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run", tint = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(JarvisSurfaceElevated)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Prompt: \"${workflow.promptCommand}\"",
                    fontSize = 11.sp,
                    color = JarvisCyan
                )
            }
        }
    }
}
