package com.example.agent

import com.example.data.local.dao.ExecutionLogDao
import com.example.data.local.dao.UIMemoryDao
import com.example.data.local.dao.WorkflowDao
import com.example.data.local.entities.ExecutionLogEntity
import com.example.data.local.entities.UIMemoryEntity
import com.example.data.local.entities.WorkflowEntity

class MemoryAgent(
    private val workflowDao: WorkflowDao,
    private val logDao: ExecutionLogDao,
    private val uiMemoryDao: UIMemoryDao
) {

    suspend fun recordWorkflowRun(
        command: String,
        workflowName: String,
        status: String,
        startedAt: Long,
        durationMs: Long,
        totalSteps: Int,
        completedSteps: Int,
        aiThoughtsJson: String,
        logsDetailJson: String,
        error: String? = null
    ) {
        val log = ExecutionLogEntity(
            command = command,
            workflowName = workflowName,
            status = status,
            startedAt = startedAt,
            durationMs = durationMs,
            totalSteps = totalSteps,
            completedSteps = completedSteps,
            aiThoughtsJson = aiThoughtsJson,
            logsDetailJson = logsDetailJson,
            errorMessage = error
        )
        logDao.insertLog(log)
    }

    suspend fun recordLearnedElement(
        appPackage: String,
        appName: String,
        elementKey: String,
        leftRatio: Float,
        topRatio: Float,
        textLabel: String
    ) {
        val memory = UIMemoryEntity(
            appPackage = appPackage,
            appName = appName,
            appVersion = "12.4.0",
            elementKey = elementKey,
            boundsJson = "[$leftRatio, $topRatio]",
            className = "android.widget.Button",
            textLabel = textLabel
        )
        uiMemoryDao.insertMemory(memory)
    }

    suspend fun saveCustomWorkflow(
        title: String,
        description: String,
        category: String,
        promptCommand: String,
        stepsJson: String
    ) {
        val workflow = WorkflowEntity(
            title = title,
            description = description,
            category = category,
            promptCommand = promptCommand,
            stepsJson = stepsJson,
            isTemplate = false
        )
        workflowDao.insertWorkflow(workflow)
    }
}
