# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-08-07

### Added

- **Multi-Agent Architecture** — Planner, Vision, Reasoning, Accessibility, and Memory agents working in concert
- **AutomationEngine** — Coroutine-based orchestration with pause/resume/stop controls
- **Gemini AI Integration** — Dynamic workflow planning via Google Gemini 2.5 Flash and 1.5 Pro models
- **Accessibility Service** — Full Android Accessibility API integration for tap, swipe, scroll, text input, and gesture dispatch
- **Plugin System** — 10 built-in plugins: Rednote, YouTube, CapCut, Chrome, Gallery, Telegram, Instagram, TikTok, WhatsApp, and Gemini TTS
- **Workflow Engine** — Predefined pipelines (Rednote→YouTube, Gallery Organizer, CapCut Subtitles, Web Scraper) with dynamic AI-generated plans as fallback
- **Room Database** — Persistent storage for workflows, execution logs, UI memory, and plugin configuration
- **Command Hub** — Primary screen for entering natural-language automation commands
- **Floating Assistant Overlay** — Persistent HUD with run/pause/resume/stop controls
- **Workflows Screen** — Browse, create, and manage saved automation workflows
- **Plugins Screen** — View, enable/disable, and configure app automation plugins
- **Memory Screen** — Inspect learned UI element positions and execution history
- **Logs Screen** — Real-time execution log viewer with AI thought reasoning
- **Settings Screen** — Model selection (OpenCode Zen, OmniRoute, OpenRouter), API configuration, and app preferences
- **Jetpack Compose UI** — Material 3 dark theme with cyan accent, edge-to-edge design
- **Unit & UI Tests** — Robolectric tests, screenshot tests with Roborazzi
