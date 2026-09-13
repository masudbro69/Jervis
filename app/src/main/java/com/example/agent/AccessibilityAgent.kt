package com.example.agent

import com.example.agent.models.ActionType
import com.example.agent.models.AgentAction
import kotlinx.coroutines.delay

class AccessibilityAgent {

    suspend fun executeAction(action: AgentAction, screenWidthPx: Int = 1080, screenHeightPx: Int = 2400): Boolean {
        val service = JarvisAccessibilityService.instance

        if (service != null && JarvisAccessibilityService.isServiceActive.value) {
            val targetX = (action.xRatio ?: 0.5f) * screenWidthPx
            val targetY = (action.yRatio ?: 0.5f) * screenHeightPx

            when (action.type) {
                ActionType.TAP, ActionType.EXTRACT_MEDIA, ActionType.GENERATE_AUDIO, ActionType.ADD_SUBTITLES, ActionType.UPLOAD_MEDIA -> {
                    service.performTapAt(targetX, targetY)
                }
                ActionType.SWIPE, ActionType.SCROLL_DOWN -> {
                    service.performSwipe(targetX, targetY + 400f, targetX, targetY - 400f)
                }
                ActionType.SCROLL_UP -> {
                    service.performSwipe(targetX, targetY - 400f, targetX, targetY + 400f)
                }
                ActionType.BACK -> {
                    service.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK)
                }
                ActionType.HOME -> {
                    service.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME)
                }
                ActionType.RECENTS -> {
                    service.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS)
                }
                else -> {
                    // Fallback gesture
                    service.performTapAt(targetX, targetY)
                }
            }
        }

        // Simulate execution latency for high-fidelity UI visual feedback
        delay(action.delayMs)
        return true
    }
}
