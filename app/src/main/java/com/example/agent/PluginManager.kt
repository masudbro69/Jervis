package com.example.agent

import com.example.agent.models.ActionType
import com.example.agent.models.PluginInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PluginManager {

    private val defaultPlugins = listOf(
        PluginInfo(
            id = "plugin_rednote",
            name = "Rednote (小红书)",
            packageName = "com.xingin.xiaohongshu",
            iconName = "ic_rednote",
            description = "Download HD trending videos, extract watermark-free media streams, copy title & hashtag metadata.",
            category = "Social Media & Download",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.TAP, ActionType.EXTRACT_MEDIA, ActionType.COPY)
        ),
        PluginInfo(
            id = "plugin_youtube",
            name = "YouTube Studio & Shorts",
            packageName = "com.google.android.youtube",
            iconName = "ic_youtube",
            description = "Auto-upload Shorts/Videos, fill AI metadata, publish videos, track views & analytics.",
            category = "Content Creation",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.UPLOAD_MEDIA, ActionType.TYPE_TEXT, ActionType.TAP)
        ),
        PluginInfo(
            id = "plugin_gemini_tts",
            name = "Google AI Studio & Gemini",
            packageName = "com.google.android.apps.bard",
            iconName = "ic_gemini",
            description = "Multi-language neural voice generator (Hindi, English, Spanish), text translator, auto scriptwriter.",
            category = "AI Engine & Voice",
            supportedActions = listOf(ActionType.GENERATE_AUDIO, ActionType.TRANSLATE_TEXT)
        ),
        PluginInfo(
            id = "plugin_capcut",
            name = "CapCut Video Engine",
            packageName = "com.lemon.lvoverseas",
            iconName = "ic_capcut",
            description = "Automate audio replacement, generate burned-in styled subtitles, apply video speed effects, render MP4.",
            category = "Video Editing",
            supportedActions = listOf(ActionType.ADD_SUBTITLES, ActionType.OPEN_APP, ActionType.TAP)
        ),
        PluginInfo(
            id = "plugin_chrome",
            name = "Chrome Web Browser",
            packageName = "com.android.chrome",
            iconName = "ic_chrome",
            description = "Web page automation, deep web scraping, form filling, article content extraction.",
            category = "Web Automation",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.TYPE_TEXT, ActionType.SCROLL_DOWN, ActionType.COPY)
        ),
        PluginInfo(
            id = "plugin_gallery",
            name = "Android Media Gallery",
            packageName = "com.android.gallery3d",
            iconName = "ic_gallery",
            description = "Manage media files, Vision AI photo cleanup, album categorization, duplicate image removal.",
            category = "Storage & Media",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.TAP)
        ),
        PluginInfo(
            id = "plugin_telegram",
            name = "Telegram Automation",
            packageName = "org.telegram.messenger",
            iconName = "ic_telegram",
            description = "Auto-send notifications, forward channel media, manage group messages.",
            category = "Messaging",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.TYPE_TEXT, ActionType.TAP)
        ),
        PluginInfo(
            id = "plugin_instagram",
            name = "Instagram Reels",
            packageName = "com.instagram.android",
            iconName = "ic_instagram",
            description = "Automate Instagram Reels posting, auto-reply to DMs, hashtag research.",
            category = "Social Media",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.UPLOAD_MEDIA)
        ),
        PluginInfo(
            id = "plugin_tiktok",
            name = "TikTok Studio",
            packageName = "com.zhiliaoapp.musically",
            iconName = "ic_tiktok",
            description = "Auto-publish TikTok videos, trending music attachment, video analytics.",
            category = "Social Media",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.UPLOAD_MEDIA)
        ),
        PluginInfo(
            id = "plugin_whatsapp",
            name = "WhatsApp Business",
            packageName = "com.whatsapp",
            iconName = "ic_whatsapp",
            description = "Auto-send task updates, broadcast status reports to users.",
            category = "Messaging",
            supportedActions = listOf(ActionType.OPEN_APP, ActionType.TYPE_TEXT)
        )
    )

    private val _plugins = MutableStateFlow(defaultPlugins)
    val plugins: StateFlow<List<PluginInfo>> = _plugins

    fun togglePlugin(id: String) {
        _plugins.value = _plugins.value.map {
            if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it
        }
    }
}
