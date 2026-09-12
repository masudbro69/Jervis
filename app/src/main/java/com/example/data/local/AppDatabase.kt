package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ExecutionLogDao
import com.example.data.local.dao.PluginConfigDao
import com.example.data.local.dao.UIMemoryDao
import com.example.data.local.dao.WorkflowDao
import com.example.data.local.entities.ExecutionLogEntity
import com.example.data.local.entities.PluginConfigEntity
import com.example.data.local.entities.UIMemoryEntity
import com.example.data.local.entities.WorkflowEntity

@Database(
    entities = [
        WorkflowEntity::class,
        ExecutionLogEntity::class,
        UIMemoryEntity::class,
        PluginConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workflowDao(): WorkflowDao
    abstract fun executionLogDao(): ExecutionLogDao
    abstract fun uiMemoryDao(): UIMemoryDao
    abstract fun pluginConfigDao(): PluginConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jarvis_agent_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
