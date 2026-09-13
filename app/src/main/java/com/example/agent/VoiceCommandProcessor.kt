package com.example.agent

import com.example.agent.models.ActionType
import com.example.agent.models.VoiceCommand
import com.example.agent.models.VoiceCommandType

/**
 * Processes natural language voice commands and maps them
 * to executable agent actions or system controls.
 */
class VoiceCommandProcessor {

    /**
     * Parse a voice command string into a typed VoiceCommand.
     * Returns null if the command doesn't match any known pattern
     * (in which case it should be sent to the AI planner).
     */
    fun parse(rawText: String): VoiceCommand {
        val lower = rawText.lowercase().trim()

        return when {
            // ── System Navigation ─────────────────────────
            lower.matches(Regex(".*(go|navigate|press)\\s+back.*")) ->
                VoiceCommand(VoiceCommandType.SYSTEM_ACTION, ActionType.BACK, rawText)

            lower.matches(Regex(".*(go|press|navigate)\\s+home.*")) ->
                VoiceCommand(VoiceCommandType.SYSTEM_ACTION, ActionType.HOME, rawText)

            lower.matches(Regex(".*(recent|recents|switch)\\s*(apps|app)??.*")) ->
                VoiceCommand(VoiceCommandType.SYSTEM_ACTION, ActionType.RECENTS, rawText)

            // ── App Launch ───────────────────────────────
            lower.matches(Regex(".*(open|launch|start|run)\\s+(.+?)\\s*(app)?.*")) -> {
                val appName = extractAppName(lower)
                VoiceCommand(VoiceCommandType.LAUNCH_APP, ActionType.OPEN_APP, rawText, targetApp = appName)
            }

            // ── Scrolling ────────────────────────────────
            lower.matches(Regex(".*(scroll|swipe)\\s*(down|up|left|right).*")) -> {
                val action = if (lower.contains("up")) ActionType.SCROLL_UP else ActionType.SCROLL_DOWN
                VoiceCommand(VoiceCommandType.SYSTEM_ACTION, action, rawText)
            }

            // ── Text Input ───────────────────────────────
            lower.matches(Regex("(type|write|input|enter|search)\\s+(.+)")) -> {
                val text = extractTypeText(lower)
                VoiceCommand(VoiceCommandType.TYPE_TEXT, ActionType.TYPE_TEXT, rawText, inputText = text)
            }

            // ── Tap / Click ──────────────────────────────
            lower.matches(Regex("(tap|click|press|touch)\\s+(on\\s+)?(.+)")) -> {
                val target = extractTapTarget(lower)
                VoiceCommand(VoiceCommandType.TAP_ELEMENT, ActionType.TAP, rawText, targetElement = target)
            }

            // ── Screenshot ───────────────────────────────
            lower.matches(Regex(".*(take|capture|screenshot|screen\\s*shot).*")) ->
                VoiceCommand(VoiceCommandType.TAKE_SCREENSHOT, ActionType.TAP, rawText)

            // ── Read Screen ──────────────────────────────
            lower.matches(Regex(".*(read|what.{0,3}(on|is).{0,3}screen|describe).*screen.*")) ->
                VoiceCommand(VoiceCommandType.READ_SCREEN, ActionType.TAP, rawText)

            // ── Media Controls ───────────────────────────
            lower.matches(Regex(".*(play|pause|stop|next|previous)\\s*(music|song|video|track)?.*")) ->
                VoiceCommand(VoiceCommandType.MEDIA_CONTROL, ActionType.TAP, rawText)

            // ── Volume ───────────────────────────────────
            lower.matches(Regex(".*(volume|mute|unmute)\\s*(up|down)?.*")) ->
                VoiceCommand(VoiceCommandType.VOLUME_CONTROL, ActionType.TAP, rawText)

            // ── Brightness ───────────────────────────────
            lower.matches(Regex(".*(brightness|dim|brighten).*")) ->
                VoiceCommand(VoiceCommandType.BRIGHTNESS_CONTROL, ActionType.TAP, rawText)

            // ── Notification ─────────────────────────────
            lower.matches(Regex(".*(notification|notify|alert).*")) ->
                VoiceCommand(VoiceCommandType.READ_NOTIFICATIONS, ActionType.TAP, rawText)

            // ── Stop / Cancel ────────────────────────────
            lower.matches(Regex("(stop|cancel|abort|halt|enough|quiet|shut).*")) ->
                VoiceCommand(VoiceCommandType.STOP_EXECUTION, ActionType.TAP, rawText)

            // ── Pause / Resume ───────────────────────────
            lower.matches(Regex("(pause|hold|wait).*")) ->
                VoiceCommand(VoiceCommandType.PAUSE_EXECUTION, ActionType.TAP, rawText)

            lower.matches(Regex("(resume|continue|proceed|go\\s+on).*")) ->
                VoiceCommand(VoiceCommandType.RESUME_EXECUTION, ActionType.TAP, rawText)

            // ── Workflow Commands ────────────────────────
            lower.matches(Regex("(save|remember)\\s+(this|workflow|task).*")) ->
                VoiceCommand(VoiceCommandType.SAVE_WORKFLOW, ActionType.TAP, rawText)

            // ── Default: AI Planner Command ──────────────
            else -> VoiceCommand(VoiceCommandType.AI_COMMAND, ActionType.TAP, rawText)
        }
    }

    // ── Extraction Helpers ────────────────────────────────────────
    private fun extractAppName(text: String): String {
        val match = Regex("(?:open|launch|start|run)\\s+(.+?)(?:\\s+app)?(?:\\s*$)").find(text)
        return match?.groupValues?.get(1)?.trim() ?: ""
    }

    private fun extractTypeText(text: String): String {
        val match = Regex("(?:type|write|input|enter|search)\\s+(.+)").find(text)
        return match?.groupValues?.get(1)?.trim() ?: ""
    }

    private fun extractTapTarget(text: String): String {
        val match = Regex("(?:tap|click|press|touch)\\s+(?:on\\s+)?(.+)").find(text)
        return match?.groupValues?.get(1)?.trim() ?: ""
    }
}
