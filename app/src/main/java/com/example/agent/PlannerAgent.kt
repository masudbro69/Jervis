package com.example.agent

import com.example.agent.models.ActionType
import com.example.agent.models.WorkflowStep
import com.example.data.network.GeminiNetwork

class PlannerAgent {

    suspend fun planWorkflow(userPrompt: String): List<WorkflowStep> {
        val lower = userPrompt.lowercase()

        // Match predefined complex pipelines or generate intelligent dynamic steps
        return when {
            lower.contains("rednote") || (lower.contains("video") && lower.contains("youtube")) -> {
                buildRednoteToYouTubePipeline(userPrompt)
            }
            lower.contains("gallery") || lower.contains("photo") -> {
                buildGalleryOrganizerPipeline()
            }
            lower.contains("capcut") || lower.contains("subtitle") || lower.contains("voice") -> {
                buildCapCutVoiceSubtitlePipeline()
            }
            lower.contains("scrape") || lower.contains("chrome") || lower.contains("web") -> {
                buildWebScraperPipeline(userPrompt)
            }
            else -> {
                generateDynamicPlan(userPrompt)
            }
        }
    }

    private fun buildRednoteToYouTubePipeline(prompt: String): List<WorkflowStep> {
        val isHindi = prompt.contains("hindi", ignoreCase = true)
        val voiceLang = if (isHindi) "Hindi" else "English"

        return listOf(
            WorkflowStep(
                stepIndex = 1,
                title = "Launch Rednote App",
                appName = "Rednote",
                description = "Observe home feed and navigate to trending video section.",
                actionType = ActionType.OPEN_APP,
                expectedTargetText = "Rednote"
            ),
            WorkflowStep(
                stepIndex = 2,
                title = "Locate & Extract Video",
                appName = "Rednote",
                description = "Detect share/download button via Vision AI and extract HD video stream.",
                actionType = ActionType.EXTRACT_MEDIA,
                expectedTargetText = "Download Video"
            ),
            WorkflowStep(
                stepIndex = 3,
                title = "Extract Audio & Speech-to-Text",
                appName = "CapCut / Media3",
                description = "Separate audio track, perform speech recognition and translation.",
                actionType = ActionType.TRANSLATE_TEXT,
                inputText = "Translate speech to $voiceLang"
            ),
            WorkflowStep(
                stepIndex = 4,
                title = "Generate $voiceLang AI Voiceover",
                appName = "Google AI Studio / Gemini TTS",
                description = "Synthesize neural studio $voiceLang TTS track matching video timeline.",
                actionType = ActionType.GENERATE_AUDIO,
                inputText = "Voice: $voiceLang Expressive Studio"
            ),
            WorkflowStep(
                stepIndex = 5,
                title = "Replace Audio & Auto-Burn Subtitles",
                appName = "CapCut Engine",
                description = "Merge new voiceover track and render styled captions onto video.",
                actionType = ActionType.ADD_SUBTITLES,
                inputText = "$voiceLang Subtitles (Font: Impact Bold)"
            ),
            WorkflowStep(
                stepIndex = 6,
                title = "Open YouTube & Create Short",
                appName = "YouTube",
                description = "Launch YouTube, tap + button, select Upload Short.",
                actionType = ActionType.OPEN_APP,
                expectedTargetText = "YouTube Short"
            ),
            WorkflowStep(
                stepIndex = 7,
                title = "Set AI Title & Upload",
                appName = "YouTube",
                description = "Auto-fill viral title, description, tags and publish short.",
                actionType = ActionType.UPLOAD_MEDIA,
                inputText = "Viral AI Short #Shorts #Trending"
            )
        )
    }

    private fun buildGalleryOrganizerPipeline(): List<WorkflowStep> {
        return listOf(
            WorkflowStep(
                stepIndex = 1,
                title = "Open Gallery App",
                appName = "Gallery",
                description = "Launch system gallery and scan recent media albums.",
                actionType = ActionType.OPEN_APP
            ),
            WorkflowStep(
                stepIndex = 2,
                title = "Vision AI Quality Scan",
                appName = "Gallery",
                description = "Analyze screenshots, duplicates, and blurred images.",
                actionType = ActionType.TAP,
                expectedTargetText = "Albums"
            ),
            WorkflowStep(
                stepIndex = 3,
                title = "Group & Auto-Tag Photos",
                appName = "Gallery",
                description = "Create smart folders for Receipts, Documents, and Travel.",
                actionType = ActionType.TYPE_TEXT,
                inputText = "Smart Organize"
            )
        )
    }

    private fun buildCapCutVoiceSubtitlePipeline(): List<WorkflowStep> {
        return listOf(
            WorkflowStep(
                stepIndex = 1,
                title = "Launch CapCut Studio",
                appName = "CapCut",
                description = "Open CapCut video editor and load active project.",
                actionType = ActionType.OPEN_APP
            ),
            WorkflowStep(
                stepIndex = 2,
                title = "Extract Subtitle Tracks",
                appName = "CapCut",
                description = "Run automatic caption generator.",
                actionType = ActionType.ADD_SUBTITLES
            ),
            WorkflowStep(
                stepIndex = 3,
                title = "Export Rendered Video",
                appName = "CapCut",
                description = "Export 1080p 60fps MP4 video file.",
                actionType = ActionType.TAP,
                expectedTargetText = "Export"
            )
        )
    }

    private fun buildWebScraperPipeline(prompt: String): List<WorkflowStep> {
        return listOf(
            WorkflowStep(
                stepIndex = 1,
                title = "Launch Chrome Browser",
                appName = "Chrome",
                description = "Open browser and navigate to target URL.",
                actionType = ActionType.OPEN_APP
            ),
            WorkflowStep(
                stepIndex = 2,
                title = "Search & Scrape Content",
                appName = "Chrome",
                description = "Extract headline text, tables, and media links.",
                actionType = ActionType.TYPE_TEXT,
                inputText = prompt
            ),
            WorkflowStep(
                stepIndex = 3,
                title = "AI Summarization",
                appName = "Makima Core",
                description = "Summarize scraped content into actionable intelligence.",
                actionType = ActionType.TRANSLATE_TEXT
            )
        )
    }

    private suspend fun generateDynamicPlan(userPrompt: String): List<WorkflowStep> {
        val systemPrompt = "You are Makima AI Agent OS Planner. Break down the user prompt into 3 to 5 discrete executable Android automation steps. Return brief clear descriptions."
        val aiAdvice = GeminiNetwork.askGemini(userPrompt, systemPrompt)

        return listOf(
            WorkflowStep(
                stepIndex = 1,
                title = "Analyze System Context",
                appName = "Makima Core",
                description = "Identify target apps, permissions, and initial UI state.",
                actionType = ActionType.OPEN_APP
            ),
            WorkflowStep(
                stepIndex = 2,
                title = "Observe Screen & Reason",
                appName = "Target App",
                description = aiAdvice.take(120),
                actionType = ActionType.TAP
            ),
            WorkflowStep(
                stepIndex = 3,
                title = "Execute Workflow Action",
                appName = "Makima Core",
                description = "Perform required gesture, text input, or media processing.",
                actionType = ActionType.TYPE_TEXT,
                inputText = userPrompt
            )
        )
    }
}
