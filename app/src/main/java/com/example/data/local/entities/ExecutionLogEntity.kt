package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "execution_logs")
data class ExecutionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val command: String,
    val workflowName: String,
    val status: String, // COMPLETED, FAILED, CANCELLED
    val startedAt: Long,
    val durationMs: Long,
    val totalSteps: Int,
    val completedSteps: Int,
    val aiThoughtsJson: String, // Serialized list of AI Thoughts
    val logsDetailJson: String, // Serialized step-by-step logs
    val errorMessage: String? = null
)
