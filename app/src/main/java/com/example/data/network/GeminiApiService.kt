package com.example.data.network

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// ── Gemini API Models ────────────────────────────────────────────
data class GeminiPart(val text: String? = null)
data class GeminiContent(val role: String = "user", val parts: List<GeminiPart>)
data class GeminiRequest(val contents: List<GeminiContent>, val systemInstruction: GeminiContent? = null)
data class GeminiCandidate(val content: GeminiContent?)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)

// ── OpenAI-Compatible API Models (for Xkiro & others) ───────────
data class OpenAIMessage(val role: String = "user", val content: String)
data class OpenAIRequest(val model: String, val messages: List<OpenAIMessage>, val max_tokens: Int = 2048)
data class OpenAIChoice(val message: OpenAIMessage?)
data class OpenAIResponse(val choices: List<OpenAIChoice>?)

// ── Retrofit Interfaces ─────────────────────────────────────────
interface GeminiApiService {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse

    @POST("v1beta/models/gemini-1.5-pro:generateContent")
    suspend fun generateProContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

interface OpenAICompatibleService {
    @POST("chat/completions")
    suspend fun chatCompletions(
        @Header("Authorization") auth: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
}

/**
 * Centralized network manager for all AI API calls.
 * Supports: Gemini API, Xkiro, OpenAI-compatible endpoints.
 * API keys are stored in SharedPreferences and configurable from the app.
 */
object GeminiNetwork {

    private const val PREFS_NAME = "makima_api_prefs"
    private const val KEY_GEMINI_API_KEY = "gemini_api_key"
    private const val KEY_XKIRO_API_KEY = "xkiro_api_key"
    private const val KEY_XKIRO_BASE_URL = "xkiro_base_url"
    private const val KEY_XKIRO_MODEL = "xkiro_model"
    private const val KEY_ACTIVE_PROVIDER = "active_ai_provider"

    private var prefs: SharedPreferences? = null

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val geminiApi: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    private var xkiroApi: OpenAICompatibleService? = null

    // ── Initialize ───────────────────────────────────────────────
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        rebuildXkiroClient()
    }

    // ── API Key Getters/Setters ──────────────────────────────────
    fun getGeminiApiKey(): String {
        return prefs?.getString(KEY_GEMINI_API_KEY, "") ?: ""
    }

    fun setGeminiApiKey(key: String) {
        prefs?.edit()?.putString(KEY_GEMINI_API_KEY, key)?.apply()
    }

    fun getXkiroApiKey(): String {
        return prefs?.getString(KEY_XKIRO_API_KEY, "") ?: ""
    }

    fun setXkiroApiKey(key: String) {
        prefs?.edit()?.putString(KEY_XKIRO_API_KEY, key)?.apply()
    }

    fun getXkiroBaseUrl(): String {
        return prefs?.getString(KEY_XKIRO_BASE_URL, "https://api.xkiro.com/v1") ?: "https://api.xkiro.com/v1"
    }

    fun setXkiroBaseUrl(url: String) {
        prefs?.edit()?.putString(KEY_XKIRO_BASE_URL, url)?.apply()
        rebuildXkiroClient()
    }

    fun getXkiroModel(): String {
        return prefs?.getString(KEY_XKIRO_MODEL, "xkiro-auto") ?: "xkiro-auto"
    }

    fun setXkiroModel(model: String) {
        prefs?.edit()?.putString(KEY_XKIRO_MODEL, model)?.apply()
    }

    fun getActiveProvider(): String {
        return prefs?.getString(KEY_ACTIVE_PROVIDER, "gemini") ?: "gemini"
    }

    fun setActiveProvider(provider: String) {
        prefs?.edit()?.putString(KEY_ACTIVE_PROVIDER, provider)?.apply()
    }

    fun hasGeminiKey(): Boolean {
        val key = getGeminiApiKey()
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    fun hasXkiroKey(): Boolean {
        return getXkiroApiKey().isNotBlank()
    }

    fun hasAnyApiKey(): Boolean = hasGeminiKey() || hasXkiroKey()

    // ── Rebuild Xkiro Retrofit Client ────────────────────────────
    private fun rebuildXkiroClient() {
        val baseUrl = getXkiroBaseUrl().let {
            if (it.endsWith("/")) it else "$it/"
        }
        xkiroApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OpenAICompatibleService::class.java)
    }

    // ── Main AI Call ─────────────────────────────────────────────
    /**
     * Routes the prompt to the active AI provider (Gemini or Xkiro).
     * Falls back to the other provider if one fails.
     */
    suspend fun askAI(prompt: String, systemInstruction: String? = null, usePro: Boolean = false): String {
        val provider = getActiveProvider()

        // Try primary provider
        val result = tryProvider(provider, prompt, systemInstruction, usePro)
        if (result != null) return result

        // Fallback to the other provider
        val fallback = if (provider == "gemini") "xkiro" else "gemini"
        val fallbackResult = tryProvider(fallback, prompt, systemInstruction, usePro)
        if (fallbackResult != null) return fallbackResult

        return "⚠️ No AI provider available. Please configure your API key in Settings → API Configuration."
    }

    private suspend fun tryProvider(provider: String, prompt: String, systemInstruction: String?, usePro: Boolean): String? {
        return try {
            when (provider) {
                "gemini" -> askGemini(prompt, systemInstruction, usePro)
                "xkiro" -> askXkiro(prompt, systemInstruction)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    // ── Gemini API Call ──────────────────────────────────────────
    suspend fun askGemini(prompt: String, systemInstruction: String? = null, usePro: Boolean = false): String {
        val apiKey = getGeminiApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return "Local AI reasoning fallback active (Gemini API Key not configured)."
        }

        return try {
            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                systemInstruction = systemInstruction?.let { GeminiContent(parts = listOf(GeminiPart(text = it))) }
            )
            val response = if (usePro) {
                geminiApi.generateProContent(apiKey, request)
            } else {
                geminiApi.generateContent(apiKey, request)
            }
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Empty AI response."
        } catch (e: Exception) {
            "Gemini API error: ${e.localizedMessage ?: "Network error"}"
        }
    }

    // ── Xkiro / OpenAI-Compatible API Call ───────────────────────
    suspend fun askXkiro(prompt: String, systemInstruction: String? = null): String {
        val apiKey = getXkiroApiKey()
        if (apiKey.isBlank()) {
            return "Xkiro API key not configured."
        }

        val client = xkiroApi ?: run {
            rebuildXkiroClient()
            xkiroApi
        } ?: return "Xkiro client not initialized."

        return try {
            val messages = mutableListOf<OpenAIMessage>()
            if (systemInstruction != null) {
                messages.add(OpenAIMessage(role = "system", content = systemInstruction))
            }
            messages.add(OpenAIMessage(role = "user", content = prompt))

            val request = OpenAIRequest(
                model = getXkiroModel(),
                messages = messages
            )
            val response = client.chatCompletions("Bearer $apiKey", request)
            response.choices?.firstOrNull()?.message?.content
                ?: "Empty Xkiro response."
        } catch (e: Exception) {
            "Xkiro API error: ${e.localizedMessage ?: "Network error"}"
        }
    }

    // ── Legacy compatibility ─────────────────────────────────────
    val apiService: GeminiApiService get() = geminiApi
}
