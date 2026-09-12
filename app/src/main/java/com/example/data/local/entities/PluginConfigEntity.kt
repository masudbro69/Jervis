package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plugin_configs")
data class PluginConfigEntity(
    @PrimaryKey val pluginId: String,
    val name: String,
    val isEnabled: Boolean = true,
    val apiKey: String? = null,
    val customSettingsJson: String = "{}"
)
