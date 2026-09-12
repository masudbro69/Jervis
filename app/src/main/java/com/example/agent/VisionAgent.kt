package com.example.agent

import androidx.compose.ui.geometry.Rect
import com.example.agent.models.ScreenFrame
import com.example.agent.models.UINode
import com.example.agent.models.WorkflowStep

class VisionAgent {

    fun generateSimulatedScreen(step: WorkflowStep?): ScreenFrame {
        val appName = step?.appName ?: "Rednote"
        val packageName = when (appName) {
            "Rednote" -> "com.xingin.xiaohongshu"
            "YouTube" -> "com.google.android.youtube"
            "CapCut" -> "com.lemon.lvoverseas"
            "Chrome" -> "com.android.chrome"
            "Gallery" -> "com.android.gallery3d"
            "Telegram" -> "org.telegram.messenger"
            else -> "com.example.app"
        }

        val nodes = mutableListOf<UINode>()

        // Generate synthetic UI nodes matching the active step's target app context
        when (appName) {
            "Rednote" -> {
                nodes.add(UINode("top_bar", "Rednote Trending Feed", bounds = Rect(20f, 40f, 380f, 90f), isClickable = false))
                nodes.add(UINode("video_container", "Video Player [HD 1080p Stream]", bounds = Rect(20f, 100f, 380f, 480f), isClickable = true))
                nodes.add(UINode("download_btn", "Download HD Video", bounds = Rect(300f, 420f, 370f, 470f), isClickable = true))
                nodes.add(UINode("share_btn", "Share Video", bounds = Rect(220f, 420f, 290f, 470f), isClickable = true))
                nodes.add(UINode("like_btn", "❤️ 142.8k Likes", bounds = Rect(30f, 420f, 150f, 470f), isClickable = true))
            }
            "YouTube" -> {
                nodes.add(UINode("yt_logo", "YouTube Shorts Studio", bounds = Rect(20f, 40f, 220f, 90f), isClickable = false))
                nodes.add(UINode("create_btn", "+ Create Short", bounds = Rect(150f, 650f, 250f, 700f), isClickable = true))
                nodes.add(UINode("title_input", "Add Title (viral hashtags...)", bounds = Rect(20f, 120f, 380f, 180f), isEditable = true))
                nodes.add(UINode("upload_btn", "Upload Short", bounds = Rect(260f, 200f, 380f, 250f), isClickable = true))
            }
            "CapCut" -> {
                nodes.add(UINode("capcut_title", "CapCut Video Editor Pro", bounds = Rect(20f, 40f, 300f, 90f), isClickable = false))
                nodes.add(UINode("timeline", "Audio Track: Hindi Studio Voiceover.mp3", bounds = Rect(20f, 380f, 380f, 520f), isClickable = true))
                nodes.add(UINode("subtitles_btn", "Auto-Captions (Hindi)", bounds = Rect(20f, 540f, 190f, 590f), isClickable = true))
                nodes.add(UINode("export_btn", "Export 1080p MP4", bounds = Rect(280f, 40f, 380f, 90f), isClickable = true))
            }
            else -> {
                nodes.add(UINode("app_header", "$appName Dashboard", bounds = Rect(20f, 40f, 380f, 90f), isClickable = false))
                nodes.add(UINode("search_bar", "Search or enter URL", bounds = Rect(20f, 100f, 380f, 150f), isEditable = true))
                nodes.add(UINode("action_btn", "Execute Command", bounds = Rect(20f, 170f, 200f, 220f), isClickable = true))
            }
        }

        return ScreenFrame(
            appName = appName,
            packageName = packageName,
            screenTitle = step?.title ?: "Screen Observation",
            nodes = nodes,
            progressPercent = if (step?.isCompleted == true) 1.0f else 0.65f,
            statusText = "Vision OCR active: Detected ${nodes.size} interactive nodes"
        )
    }
}
