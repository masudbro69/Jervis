package com.example.agent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Makima Voice Engine — Handles continuous speech recognition,
 * text-to-speech feedback, and wake word detection.
 */
class VoiceEngine(private val context: Context) {

    // ── State Flows ──────────────────────────────────────────────
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isWakeWordActive = MutableStateFlow(false)
    val isWakeWordActive: StateFlow<Boolean> = _isWakeWordActive.asStateFlow()

    private val _partialText = MutableStateFlow("")
    val partialText: StateFlow<String> = _partialText.asStateFlow()

    private val _finalText = MutableStateFlow("")
    val finalText: StateFlow<String> = _finalText.asStateFlow()

    private val _voiceAmplitude = MutableStateFlow(0f)
    val voiceAmplitude: StateFlow<Float> = _voiceAmplitude.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ── Internal ─────────────────────────────────────────────────
    private var speechRecognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var onResultCallback: ((String) -> Unit)? = null
    private var onWakeWordCallback: (() -> Unit)? = null
    private val wakeWords = listOf("makima", "hey makima", "ok makima")

    // TTS voice settings
    private var ttsLanguage = Locale.US
    private var ttsSpeechRate = 1.1f
    private var ttsPitch = 0.95f

    // ── Initialize ───────────────────────────────────────────────
    fun initialize() {
        initSpeechRecognizer()
        initTTS()
    }

    private fun initSpeechRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _error.value = "Speech recognition not available on this device"
            return
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _error.value = null
                }

                override fun onBeginningOfSpeech() {
                    _isListening.value = true
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Normalize RMS to 0..1 range for waveform visualization
                    _voiceAmplitude.value = ((rmsdB + 2f) / 10f).coerceIn(0f, 1f)
                }

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    _isListening.value = false
                    _voiceAmplitude.value = 0f
                }

                override fun onError(error: Int) {
                    _isListening.value = false
                    _voiceAmplitude.value = 0f
                    _error.value = mapSpeechError(error)
                    // Auto-restart if wake word mode is active
                    if (_isWakeWordActive.value && error != SpeechRecognizer.ERROR_NO_MATCH) {
                        restartListening()
                    }
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull()?.lowercase()?.trim() ?: ""
                    _finalText.value = text
                    _isListening.value = false

                    if (text.isNotEmpty()) {
                        if (_isWakeWordActive.value && containsWakeWord(text)) {
                            onWakeWordCallback?.invoke()
                            // Extract command after wake word
                            val command = extractCommandAfterWakeWord(text)
                            if (command.isNotEmpty()) {
                                onResultCallback?.invoke(command)
                            } else {
                                speak("Yes? I'm listening.")
                                startListening()
                                return
                            }
                        } else if (!_isWakeWordActive.value) {
                            onResultCallback?.invoke(text)
                        }
                    }

                    if (_isWakeWordActive.value) {
                        restartListening()
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    _partialText.value = matches?.firstOrNull() ?: ""
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    private fun initTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = ttsLanguage
                tts?.setSpeechRate(ttsSpeechRate)
                tts?.setPitch(ttsPitch)
                ttsReady = true

                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    @Deprecated("Deprecated in API")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
            }
        }
    }

    // ── Listening Controls ───────────────────────────────────────
    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000L)
        }
        try {
            speechRecognizer?.startListening(intent)
            _isListening.value = true
            _error.value = null
        } catch (e: Exception) {
            _error.value = "Failed to start listening: ${e.message}"
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
        _voiceAmplitude.value = 0f
    }

    fun toggleListening() {
        if (_isListening.value) stopListening() else startListening()
    }

    // ── Wake Word Mode ───────────────────────────────────────────
    fun startWakeWordMode(onWake: () -> Unit, onCommand: (String) -> Unit) {
        _isWakeWordActive.value = true
        onWakeWordCallback = onWake
        onResultCallback = onCommand
        startListening()
    }

    fun stopWakeWordMode() {
        _isWakeWordActive.value = false
        onWakeWordCallback = null
        stopListening()
    }

    // ── Text-to-Speech ───────────────────────────────────────────
    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (!ttsReady) return
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "makima_tts_${System.currentTimeMillis()}")
        }
        tts?.speak(text, queueMode, params, "makima_tts_${System.currentTimeMillis()}")
    }

    fun speakAndThen(text: String, onComplete: () -> Unit) {
        if (!ttsReady) {
            onComplete()
            return
        }
        val utteranceId = "makima_callback_${System.currentTimeMillis()}"
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) { _isSpeaking.value = true }
            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                onComplete()
            }
            @Deprecated("Deprecated in API")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                onComplete()
            }
        })
        val params = Bundle().apply { putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId) }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    // ── Voice Settings ───────────────────────────────────────────
    fun setVoiceLanguage(locale: Locale) {
        ttsLanguage = locale
        tts?.language = locale
    }

    fun setSpeechRate(rate: Float) {
        ttsSpeechRate = rate.coerceIn(0.5f, 2.0f)
        tts?.setSpeechRate(ttsSpeechRate)
    }

    fun setPitch(pitch: Float) {
        ttsPitch = pitch.coerceIn(0.5f, 2.0f)
        tts?.setPitch(ttsPitch)
    }

    // ── Result Callback ──────────────────────────────────────────
    fun setOnResultListener(callback: (String) -> Unit) {
        onResultCallback = callback
    }

    // ── Helpers ──────────────────────────────────────────────────
    private fun restartListening() {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            if (_isWakeWordActive.value) startListening()
        }, 300)
    }

    private fun containsWakeWord(text: String): Boolean {
        return wakeWords.any { text.contains(it, ignoreCase = true) }
    }

    private fun extractCommandAfterWakeWord(text: String): String {
        var result = text
        for (wake in wakeWords) {
            val idx = text.lowercase().indexOf(wake)
            if (idx >= 0) {
                result = text.substring(idx + wake.length).trim()
                break
            }
        }
        return result
    }

    private fun mapSpeechError(error: Int): String = when (error) {
        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required"
        SpeechRecognizer.ERROR_NETWORK -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
        SpeechRecognizer.ERROR_SERVER -> "Server error"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
        else -> "Unknown error ($error)"
    }

    // ── Cleanup ──────────────────────────────────────────────────
    fun shutdown() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        tts?.stop()
        tts?.shutdown()
        tts = null
        _isWakeWordActive.value = false
    }
}
