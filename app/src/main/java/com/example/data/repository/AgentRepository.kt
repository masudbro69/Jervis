package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entities.ExecutionLogEntity
import com.example.data.local.entities.PluginConfigEntity
import com.example.data.local.entities.UIMemoryEntity
import com.example.data.local.entities.WorkflowEntity
import kotlinx.coroutines.flow.Flow

class AgentRepository(private val db: AppDatabase) {

    val allWorkflows: Flow<List<WorkflowEntity>> = db.workflowDao().getAllWorkflows()
    val allLogs: Flow<List<ExecutionLogEntity>> = db.executionLogDao().getAllLogs()
    val allMemories: Flow<List<UIMemoryEntity>> = db.uiMemoryDao().getAllUIMemories()
    val allPluginConfigs: Flow<List<PluginConfigEntity>> = db.pluginConfigDao().getAllPluginConfigs()

    suspend fun saveWorkflow(workflow: WorkflowEntity): Long {
        return db.workflowDao().insertWorkflow(workflow)
    }

    suspend fun deleteWorkflow(id: Int) {
        db.workflowDao().deleteWorkflowById(id)
    }

    suspend fun clearLogs() {
        db.executionLogDao().clearLogs()
    }
}
