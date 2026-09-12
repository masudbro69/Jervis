<p align="center">
  <img src="app/src/main/res/drawable/jarvis_app_icon_1786092709989.jpg" alt="JARVIS Agent OS" width="128" height="128" style="border-radius: 24px;" />
</p>

<h1 align="center">JARVIS Agent OS</h1>

<p align="center">
  <strong>Autonomous Android AI Agent — Multi-Agent Architecture with Vision, Reasoning, and Accessibility Automation</strong>
</p>

<p align="center">
  <a href="#features">Features</a> &bull;
  <a href="#architecture">Architecture</a> &bull;
  <a href="#getting-started">Getting Started</a> &bull;
  <a href="#plugins">Plugins</a> &bull;
  <a href="#contributing">Contributing</a> &bull;
  <a href="#license">License</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-24%2B-brightgreen?logo=android" alt="Min SDK" />
  <img src="https://img.shields.io/badge/Kotlin-2.2-blue?logo=kotlin" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-Material%203-purple" alt="Compose" />
  <img src="https://img.shields.io/badge/Gemini-AI-red?logo=google" alt="Gemini AI" />
  <img src="https://img.shields.io/github/license/masudbro69/Jervis" alt="License" />
</p>

---

## Overview

JARVIS Agent OS is an autonomous Android agent that uses natural language commands to automate complex multi-app workflows. Built on a **multi-agent architecture**, it combines AI-powered planning, computer vision, reasoning, and Android accessibility services to execute tasks across apps — from content creation pipelines to web scraping and media management.

Simply tell JARVIS what you want to do in plain language, and it will decompose your request into executable steps, observe the screen, reason about the next action, and carry it out automatically.

## Features

- **Natural Language Command Interface** — Describe tasks in plain English; JARVIS plans and executes them autonomously
- **Multi-Agent Pipeline** — Specialized agents for planning, vision, reasoning, accessibility, and memory working in concert
- **Gemini AI Integration** — Powered by Google Gemini for intelligent task decomposition and dynamic workflow generation
- **Accessibility Automation** — Full Android Accessibility API support: taps, swipes, scrolls, text input, and gesture dispatch
- **10 Built-in Plugins** — Pre-configured automation for Rednote, YouTube, CapCut, Chrome, Gallery, Telegram, Instagram, TikTok, WhatsApp, and Gemini TTS
- **Workflow Engine** — Save, manage, and re-run automation workflows with predefined and AI-generated pipelines
- **Memory System** — Learns and remembers UI element positions across app sessions via Room database
- **Execution Control** — Pause, resume, and stop workflows mid-execution with a floating overlay HUD
- **Real-Time Logging** — Live execution logs with AI reasoning traces and confidence scores
- **Material 3 Dark UI** — Polished dark theme with cyan accents, edge-to-edge Compose design

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    JARVIS Agent OS                       │
├─────────────────────────────────────────────────────────┤
│  UI Layer (Jetpack Compose + Material 3)                │
│  ┌──────────┬──────────┬──────────┬─────────┐           │
│  │ Command  │Workflow  │ Plugins  │ Memory  │  ...      │
│  │   Hub    │  Screen  │  Screen  │ Screen  │           │
│  └────┬─────┴──────────┴──────────┴─────────┘           │
│       │                                                  │
│  ViewModel Layer (MVVM)                                  │
│  ┌─────────────────────────────────────────────┐        │
│  │            MainViewModel                     │        │
│  └─────────────────┬───────────────────────────┘        │
│                     │                                    │
│  Agent Layer                                            │
│  ┌──────────┐ ┌──────────┐ ┌──────────────┐            │
│  │ Planner  │ │ Vision   │ │  Reasoning   │            │
│  │  Agent   │ │  Agent   │ │    Agent     │            │
│  └────┬─────┘ └────┬─────┘ └──────┬───────┘            │
│       │             │              │                     │
│  ┌────┴─────────────┴──────────────┴───────┐            │
│  │        AutomationEngine (Orchestrator)   │            │
│  └────────────────────┬────────────────────┘            │
│                       │                                  │
│  ┌───────────┐  ┌─────┴──────┐  ┌────────────┐         │
│  │  Memory   │  │Accessiblty │  │  Plugin    │         │
│  │  Agent    │  │  Agent     │  │  Manager   │         │
│  └───────────┘  └────────────┘  └────────────┘         │
│                                                          │
│  Data Layer                                              │
│  ┌──────────────┐  ┌──────────────────────┐             │
│  │  Room DB     │  │  Gemini API Service  │             │
│  │ (SQLite)     │  │  (Retrofit + OkHttp) │             │
│  └──────────────┘  └──────────────────────┘             │
└─────────────────────────────────────────────────────────┘
```

### Agent Responsibilities

| Agent | Role |
|-------|------|
| **Planner Agent** | Decomposes natural language prompts into ordered workflow steps using pattern matching and Gemini AI |
| **Vision Agent** | Generates screen frame observations with UI node detection and element identification |
| **Reasoning Agent** | Determines the next action by matching workflow targets to observed UI elements |
| **Accessibility Agent** | Executes actions via the Android Accessibility Service (taps, swipes, text input, gestures) |
| **Memory Agent** | Persists workflow runs, execution logs, and learned UI element positions to Room database |
| **Automation Engine** | Orchestrates the full agent pipeline with coroutine-based state management |

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Ladybug or newer)
- JDK 11+
- Android device or emulator running API 24+ (Android 7.0+)
- A [Gemini API key](https://ai.google.dev/) from Google AI Studio

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/masudbro69/Jervis.git
   cd Jervis
   ```

2. **Configure your API key:**

   ```bash
   cp .env.example .env
   ```

   Edit `.env` and set your Gemini API key:
   ```
   GEMINI_API_KEY=your_api_key_here
   ```

3. **Open in Android Studio:**

   - Select **Open** and navigate to the project directory
   - Allow Android Studio to resolve any import issues
   - Wait for Gradle sync to complete

4. **Build and run:**

   - Select your target device or emulator
   - Click **Run** (or `Shift+F10`)

### First Run

1. Grant the **Accessibility Service** permission when prompted (required for automation)
2. Navigate to the **Command** tab
3. Type a natural language command, e.g.:
   - *"Download a trending Rednote video, translate it to English, and upload to YouTube Shorts"*
   - *"Organize my gallery photos by category"*
   - *"Scrape the latest news from Chrome and summarize it"*
4. Watch JARVIS plan, reason, and execute each step in real time

## Plugins

JARVIS ships with 10 pre-configured automation plugins:

| Plugin | Category | Capabilities |
|--------|----------|-------------|
| Rednote (小红书) | Social Media | HD video download, media extraction, metadata copy |
| YouTube Studio & Shorts | Content Creation | Auto-upload, AI metadata, analytics |
| Google AI Studio & Gemini | AI Engine | Neural TTS, translation, scriptwriting |
| CapCut Video Engine | Video Editing | Audio replacement, auto-subtitles, MP4 render |
| Chrome Web Browser | Web Automation | Page scraping, form filling, content extraction |
| Android Media Gallery | Storage & Media | Photo cleanup, album categorization, deduplication |
| Telegram Automation | Messaging | Auto-forward, channel media, group management |
| Instagram Reels | Social Media | Reels posting, DM auto-reply, hashtag research |
| TikTok Studio | Social Media | Video publishing, trending music, analytics |
| WhatsApp Business | Messaging | Status broadcast, task updates, notifications |

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin 2.2 |
| UI Framework | Jetpack Compose with Material 3 |
| Architecture | MVVM with multi-agent pattern |
| AI Engine | Google Gemini 2.5 Flash / 1.5 Pro (via Gemini API) |
| Database | Room (SQLite) |
| Networking | Retrofit + OkHttp + Moshi |
| Async | Kotlin Coroutines + StateFlow |
| Image Loading | Coil |
| Build System | Gradle (Kotlin DSL) with Version Catalog |
| Testing | JUnit, Robolectric, Roborazzi, Espresso |

## Project Structure

```
app/src/main/java/com/example/
├── agent/                      # Core AI agent layer
│   ├── models/                 # Data models (UINode, WorkflowStep, etc.)
│   ├── AutomationEngine.kt     # Workflow orchestrator
│   ├── PlannerAgent.kt         # Task decomposition agent
│   ├── VisionAgent.kt          # Screen observation agent
│   ├── ReasoningAgent.kt       # Decision-making agent
│   ├── AccessibilityAgent.kt   # Action execution agent
│   ├── MemoryAgent.kt          # Persistence & learning agent
│   ├── PluginManager.kt        # Plugin registry & management
│   └── JarvisAccessibilityService.kt  # Android Accessibility Service
├── data/
│   ├── local/                  # Room database layer
│   │   ├── entities/           # Database entities
│   │   ├── dao/                # Data access objects
│   │   └── AppDatabase.kt     # Room database configuration
│   ├── network/
│   │   └── GeminiApiService.kt # Gemini API client
│   └── repository/
│       └── AgentRepository.kt  # Data repository
├── ui/
│   ├── components/             # Reusable Compose components
│   │   ├── JarvisHeader.kt
│   │   ├── FloatingAssistantOverlay.kt
│   │   ├── ScreenPreviewCard.kt
│   │   └── LogItemView.kt
│   ├── screens/                # App screens
│   │   ├── CommandHubScreen.kt
│   │   ├── WorkflowsScreen.kt
│   │   ├── PluginsScreen.kt
│   │   ├── MemoryScreen.kt
│   │   ├── LogsScreen.kt
│   │   └── SettingsScreen.kt
│   ├── theme/                  # Material 3 theming
│   ├── JarvisMainApp.kt       # Root composable & navigation
│   └── MainViewModel.kt       # Main ViewModel
└── MainActivity.kt             # Entry point
```

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on reporting bugs, suggesting features, and submitting pull requests.

## License

This project is licensed under the **Apache License 2.0** — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Built with Kotlin, Jetpack Compose, and Google Gemini AI
</p>
