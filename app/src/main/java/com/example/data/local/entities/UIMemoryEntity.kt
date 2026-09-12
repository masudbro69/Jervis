package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ui_memory")
data class UIMemoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val appPackage: String,
    val appName: String,
    val appVersion: String,
    val elementKey: String, // e.g. download_button, search_bar, publish_btn
    val boundsJson: String, // left, top, right, bottom ratios
    val className: String,
    val textLabel: String,
    val timesUsed: Int = 1,
    val successRate: Float = 1.0f,
    val lastUpdated: Long = System.currentTimeMillis()
)
