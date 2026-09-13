package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.UIMemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UIMemoryDao {
    @Query("SELECT * FROM ui_memory ORDER BY lastUpdated DESC")
    fun getAllUIMemories(): Flow<List<UIMemoryEntity>>

    @Query("SELECT * FROM ui_memory WHERE appPackage = :appPackage")
    suspend fun getMemoriesForApp(appPackage: String): List<UIMemoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: UIMemoryEntity): Long
}
