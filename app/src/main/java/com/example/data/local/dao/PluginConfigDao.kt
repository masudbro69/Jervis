package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.PluginConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PluginConfigDao {
    @Query("SELECT * FROM plugin_configs")
    fun getAllPluginConfigs(): Flow<List<PluginConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfig(config: PluginConfigEntity)
}
