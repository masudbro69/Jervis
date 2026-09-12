package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.AutomationEngine
import com.example.agent.MemoryAgent
import com.example.agent.PluginManager
import com.example.agent.models.ExecutionState
import com.example.agent.models.PluginInfo
import com.example.agent.models.WorkflowStep
import com.example.data.local.AppDatabase
import com.example.data.local.entities.ExecutionLogEntity
import com.example.data.local.entities.UIMemoryEntity
import com.example.data.local.entities.WorkflowEntity
import com.example.data.repository.AgentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = AgentRepository(db)
    val pluginManager = PluginManager()
    val automationEngine = AutomationEngine()

    init {
        val memoryAgent = MemoryAgent(db.workflowDao(), db.executionLogDao(), db.uiMemoryDao())
        automationEngine.memoryAgent = memoryAgent
    }

    val workflows: StateFlow<List<WorkflowEntity>> = repository.allWorkflows.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val logs: StateFlow<List<ExecutionLogEntity>> = repository.allLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val uiMemories: StateFlow<List<UIMemoryEntity>> = repository.allMemories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val plugins: StateFlow<List<PluginInfo>> = pluginManager.plugins

    // OpenCode Zen & Model Configuration
    private val _selectedModel = MutableStateFlow("big-pickle")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private val _openCodeZenApiKey = MutableStateFlow("zen_free_open_key_demo")
    val openCodeZenApiKey: StateFlow<String> = _openCodeZenApiKey.asStateFlow()

    private val _openCodeZenBaseUrl = MutableStateFlow("https://api.opencode.zen/v1")
    val openCodeZenBaseUrl: StateFlow<String> = _openCodeZenBaseUrl.asStateFlow()

    fun updateSelectedModel(model: String) {
        _selectedModel.value = model
        automationEngine.selectedModel = model
    }

    fun updateOpenCodeZenApiKey(key: String) {
        _openCodeZenApiKey.value = key
        automationEngine.openCodeZenApiKey = key
    }

    fun updateOpenCodeZenBaseUrl(url: String) {
        _openCodeZenBaseUrl.value = url
        automationEngine.openCodeZenBaseUrl = url
    }

    val executionState: StateFlow<ExecutionState> = automationEngine.executionState
    val activeSteps: StateFlow<List<WorkflowStep>> = automationEngine.activeSteps
    val currentStepIndex: StateFlow<Int> = automationEngine.currentStepIndex
    val executionLogs: StateFlow<List<String>> = automationEngine.executionLogs
    val isFloatingHudVisible: StateFlow<Boolean> = automationEngine.isFloatingHudVisible

    fun runPromptCommand(prompt: String) {
        automationEngine.runCommand(prompt)
    }

    fun pauseExecution() {
        automationEngine.pause()
    }

    fun resumeExecution() {
        automationEngine.resume()
    }

    fun stopExecution() {
        automationEngine.stop()
    }

    fun toggleFloatingHud() {
        automationEngine.toggleFloatingHud()
    }

    fun saveCustomWorkflow(title: String, description: String, prompt: String) {
        viewModelScope.launch {
            val steps = automationEngine.plannerAgent.planWorkflow(prompt)
            val stepsJson = steps.joinToString(";") { "${it.title}|${it.appName}" }
            repository.saveWorkflow(
                WorkflowEntity(
                    title = title,
                    description = description,
                    category = "Custom Automation",
                    promptCommand = prompt,
                    stepsJson = stepsJson,
                    isTemplate = false
                )
            )
        }
    }

    fun deleteWorkflow(id: Int) {
        viewModelScope.launch {
            repository.deleteWorkflow(id)
        }
    }

    fun clearLogs() {
        viewModelScope.launch {
            repository.clearLogs()
        }
    }
}
