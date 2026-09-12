# Contributing to Makima Agent OS

Thank you for your interest in contributing to Makima Agent OS! This document provides guidelines and instructions for contributing.

## Code of Conduct

Please be respectful and constructive in all interactions. We are committed to providing a welcoming and inclusive experience for everyone.

## How to Contribute

### Reporting Bugs

Before creating a bug report, please check existing [issues](https://github.com/masudbro69/Jervis/issues) to avoid duplicates.

When filing a bug report, include:

- **A clear and descriptive title**
- **Steps to reproduce** the issue
- **Expected behavior** vs. **actual behavior**
- **Device information** (model, Android version, screen size)
- **Screenshots or screen recordings** if applicable
- **Logcat output** or error messages

### Suggesting Features

Feature suggestions are welcome. Please include:

- A clear description of the feature
- The motivation / use case
- Any mockups or examples if applicable

### Pull Requests

1. Fork the repository
2. Create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make your changes following the coding standards below
4. Write or update tests as needed
5. Ensure all tests pass
6. Commit with a clear message (see [Commit Messages](#commit-messages))
7. Push to your fork and submit a pull request

## Development Setup

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Ladybug or newer)
- JDK 11+
- A Gemini API key from [Google AI Studio](https://ai.google.dev/)

### Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/masudbro69/Jervis.git
   cd Jervis
   ```

2. Create your `.env` file:
   ```bash
   cp .env.example .env
   ```
   Then set your `GEMINI_API_KEY` in `.env`.

3. Open the project in Android Studio and let Gradle sync complete.

4. Run on an emulator or physical device (API 24+).

## Coding Standards

### Kotlin Style

- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions focused and concise
- Add KDoc comments for public APIs

### Architecture

- Follow the existing MVVM architecture with Jetpack Compose
- Place new agent logic under `com.example.agent`
- Place new UI screens under `com.example.ui.screens`
- Place new data models under `com.example.agent.models` or appropriate data layer packages
- Use Kotlin Coroutines and Flow for asynchronous operations

### Testing

- Write unit tests for new agent logic
- Write UI tests for new screens and components
- Run the full test suite before submitting:
  ```bash
  ./gradlew test
  ```

## Commit Messages

Use clear, descriptive commit messages following this format:

```
<type>: <short summary>

<optional body>
```

**Types:**
- `feat` — New feature
- `fix` — Bug fix
- `docs` — Documentation changes
- `style` — Code style changes (formatting, no logic change)
- `refactor` — Code refactoring
- `test` — Adding or updating tests
- `chore` — Build, CI, or tooling changes

**Examples:**
```
feat: add TikTok automation plugin
fix: resolve accessibility service crash on Android 14
docs: update setup instructions in README
refactor: extract vision logic into dedicated agent class
```

## Project Structure

```
app/src/main/java/com/example/
├── agent/              # Core AI agent logic
│   ├── models/         # Data models (UINode, WorkflowStep, etc.)
│   ├── AutomationEngine.kt
│   ├── PlannerAgent.kt
│   ├── VisionAgent.kt
│   ├── ReasoningAgent.kt
│   ├── AccessibilityAgent.kt
│   ├── MemoryAgent.kt
│   └── PluginManager.kt
├── data/
│   ├── local/          # Room database (entities, DAOs)
│   ├── network/        # Gemini API service
│   └── repository/     # Data repository
├── ui/
│   ├── components/     # Reusable Compose components
│   ├── screens/        # App screens
│   └── theme/          # Material 3 theme
└── MainActivity.kt
```

## License

By contributing, you agree that your contributions will be licensed under the [Apache License 2.0](LICENSE).

## Questions?

If you have questions about contributing, feel free to open a [discussion](https://github.com/masudbro69/Jervis/discussions) on GitHub.
