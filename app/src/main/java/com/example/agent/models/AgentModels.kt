package com.example.agent.models

import androidx.compose.ui.geometry.Rect

enum class ActionType {
    TAP,
    DOUBLE_TAP,
    LONG_PRESS,
    SWIPE,
    SCROLL_DOWN,
    SCROLL_UP,
    TYPE_TEXT,
    COPY,
    PASTE,
    OPEN_APP,
    CLOSE_APP,
    BACK,
    HOME,
    RECENTS,
    EXTRACT_MEDIA,
    GENERATE_AUDIO,
    TRANSLATE_TEXT,
    ADD_SUBTITLES,
    UPLOAD_MEDIA,
    DELAY
}

data class UINode(
    val id: String,
    val text: String,
    val contentDescription: String? = null,
    val className: String = "android.widget.Button",
    val bounds: Rect,
    val isClickable: Boolean = true,
    val isEditable: Boolean = false,
    val isScrollable: Boolean = false,
    val packageName: String = "",
    val confidence: Float = 0.95f
)

data class ScreenFrame(
    val appName: String,
    val packageName: String,
    val screenTitle: String,
    val nodes: List<UINode>,
    val activeAppIconRes: Int? = null,
    val progressPercent: Float? = null,
    val statusText: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class AgentAction(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: ActionType,
    val targetNodeId: String? = null,
    val xRatio: Float? = null,
    val yRatio: Float? = null,
    val inputText: String? = null,
    val appPackage: String? = null,
    val description: String,
    val delayMs: Long = 1000L
)

enum class ExecutionState {
    IDLE,
    PLANNING,
    REASONING,
    EXECUTING,
    PAUSED,
    COMPLETED,
    FAILED
}

data class WorkflowStep(
    val stepIndex: Int,
    val title: String,
    val appName: String,
    val description: String,
    val actionType: ActionType,
    val expectedTargetText: String? = null,
    val inputText: String? = null,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class AIThought(
    val timestamp: Long = System.currentTimeMillis(),
    val stage: String,
    val reasoning: String,
    val confidence: Float = 0.98f,
    val detectedElements: Int = 0
)

data class PluginInfo(
    val id: String,
    val name: String,
    val packageName: String,
    val iconName: String,
    val description: String,
    val category: String,
    val isEnabled: Boolean = true,
    val supportedActions: List<ActionType>,
    val sampleWorkflowsCount: Int = 3
)

data class AppPermissionStatus(
    val name: String,
    val isGranted: Boolean,
    val description: String
)
