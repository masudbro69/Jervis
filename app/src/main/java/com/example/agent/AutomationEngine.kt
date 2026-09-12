package com.example.agent

import com.example.agent.models.AIThought
import com.example.agent.models.AgentAction
import com.example.agent.models.ExecutionState
import com.example.agent.models.ScreenFrame
import com.example.agent.models.WorkflowStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AutomationEngine(
    val plannerAgent: PlannerAgent = PlannerAgent(),
    val reasoningAgent: ReasoningAgent = ReasoningAgent(),
    val visionAgent: VisionAgent = VisionAgent(),
    val accessibilityAgent: AccessibilityAgent = AccessibilityAgent(),
    var memoryAgent: MemoryAgent? = null
) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private val _executionState = MutableStateFlow(ExecutionState.IDLE)
    val executionState: StateFlow<ExecutionState> = _executionState.asStateFlow()

    private val _currentCommand = MutableStateFlow("")
    val currentCommand: StateFlow<String> = _currentCommand.asStateFlow()

    private val _activeSteps = MutableStateFlow<List<WorkflowStep>>(emptyList())
    val activeSteps: StateFlow<List<WorkflowStep>> = _activeSteps.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _currentScreen = MutableStateFlow<ScreenFrame?>(null)
    val currentScreen: StateFlow<ScreenFrame?> = _currentScreen.asStateFlow()

    private val _aiThoughts = MutableStateFlow<List<AIThought>>(emptyList())
    val aiThoughts: StateFlow<List<AIThought>> = _aiThoughts.asStateFlow()

    private val _executionLogs = MutableStateFlow<List<String>>(emptyList())
    val executionLogs: StateFlow<List<String>> = _executionLogs.asStateFlow()

    private val _isFloatingHudVisible = MutableStateFlow(true)
    val isFloatingHudVisible: StateFlow<Boolean> = _isFloatingHudVisible.asStateFlow()

    private var executionJob: Job? = null
    private var isPaused = false

    var selectedModel: String = "big-pickle"
    var openCodeZenApiKey: String = "zen_free_open_key_demo"
    var openCodeZenBaseUrl: String = "https://api.opencode.zen/v1"

    fun runCommand(command: String) {
        if (command.isBlank()) return
        executionJob?.cancel()
        _currentCommand.value = command
        _executionState.value = ExecutionState.PLANNING
        _aiThoughts.value = emptyList()
        val modelLabel = when(selectedModel) {
            "big-pickle" -> "OpenCode Zen [Big Pickle Free]"
            "deepseek-v4-flash-free" -> "OpenCode Zen [DeepSeek V4 Flash Free]"
            "minimax-m2.5-free" -> "OpenCode Zen [MiniMax M2.5 Free]"
            "minimax-m3-free" -> "OpenCode Zen [MiniMax M3 Free]"
            "omniroute-auto-free" -> "OmniRoute Gateway [Auto Free Smart Router]"
            "openrouter-free-omni" -> "OpenRouter [Free Meta Llama 3.3 / DeepSeek R1]"
            else -> selectedModel
        }
        _executionLogs.value = listOf(
            "🚀 Makima AI Agent OS initialized for command: \"$command\"",
            "🤖 Active Model Engine: $modelLabel ($openCodeZenBaseUrl)"
        )

        executionJob = scope.launch {
            val startTime = System.currentTimeMillis()
            addLog("🧠 Planner Agent decomposing prompt into executable task pipeline...")

            val steps = plannerAgent.planWorkflow(command)
            _activeSteps.value = steps
            _currentStepIndex.value = 0

            addThought(
                AIThought(
                    stage = "Task Decomposition",
                    reasoning = "Generated ${steps.size} atomic automation steps across target plugins: ${steps.map { it.appName }.distinct().joinToString()}",
                    confidence = 0.99f,
                    detectedElements = steps.size
                )
            )

            addLog("✅ Task pipeline ready with ${steps.size} steps.")

            for ((index, step) in steps.withIndex()) {
                while (isPaused) {
                    _executionState.value = ExecutionState.PAUSED
                    delay(500)
                }

                _currentStepIndex.value = index
                _executionState.value = ExecutionState.REASONING

                // Update steps UI status
                _activeSteps.value = _activeSteps.value.mapIndexed { idx, s ->
                    s.copy(isCurrent = (idx == index))
                }

                addLog("📍 Step ${index + 1}/${steps.size}: ${step.title} (${step.appName})")

                // Vision observation
                val screenFrame = visionAgent.generateSimulatedScreen(step)
                _currentScreen.value = screenFrame
                delay(600)

                // Reasoning
                val (thought, action) = reasoningAgent.determineNextAction(step, screenFrame)
                addThought(thought)
                addLog("🔍 Vision AI: Observed UI node '${step.expectedTargetText ?: step.appName}'. Reasoning: ${thought.reasoning.take(90)}...")

                _executionState.value = ExecutionState.EXECUTING
                delay(400)

                // Action Execution
                addLog("⚡ Executing Action: ${action.description}")
                accessibilityAgent.executeAction(action)

                // Mark step completed
                _activeSteps.value = _activeSteps.value.mapIndexed { idx, s ->
                    if (idx == index) s.copy(isCompleted = true, isCurrent = false) else s
                }

                addLog("✅ Completed Step ${index + 1}: ${step.title}")
                delay(800)
            }

            _executionState.value = ExecutionState.COMPLETED
            val duration = System.currentTimeMillis() - startTime
            addLog("🎉 All workflow steps completed successfully in ${(duration / 1000f)}s!")

            addThought(
                AIThought(
                    stage = "Workflow Completion",
                    reasoning = "Goal achieved: Completed ${steps.size} steps. Saved workflow pattern to Memory Agent.",
                    confidence = 1.0f
                )
            )

            // Save to Room memory
            memoryAgent?.recordWorkflowRun(
                command = command,
                workflowName = steps.firstOrNull()?.title ?: "Custom Workflow",
                status = "COMPLETED",
                startedAt = startTime,
                durationMs = duration,
                totalSteps = steps.size,
                completedSteps = steps.size,
                aiThoughtsJson = "[]",
                logsDetailJson = "[]"
            )
        }
    }

    fun pause() {
        isPaused = true
        _executionState.value = ExecutionState.PAUSED
        addLog("⏸️ Execution paused by user.")
    }

    fun resume() {
        isPaused = false
        addLog("▶️ Execution resumed.")
    }

    fun stop() {
        executionJob?.cancel()
        _executionState.value = ExecutionState.IDLE
        addLog("⏹️ Execution cancelled.")
    }

    fun toggleFloatingHud() {
        _isFloatingHudVisible.value = !_isFloatingHudVisible.value
    }

    private fun addLog(message: String) {
        _executionLogs.value = _executionLogs.value + message
    }

    private fun addThought(thought: AIThought) {
        _aiThoughts.value = _aiThoughts.value + thought
    }
}
