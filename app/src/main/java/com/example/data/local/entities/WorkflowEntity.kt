package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workflows")
data class WorkflowEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val category: String, // e.g. Content Creation, Social Media, Productivity
    val promptCommand: String,
    val stepsJson: String, // Serialized list of WorkflowSteps
    val isTemplate: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastRunAt: Long? = null,
    val runsCount: Int = 0,
    val successRate: Float = 1.0f
)
