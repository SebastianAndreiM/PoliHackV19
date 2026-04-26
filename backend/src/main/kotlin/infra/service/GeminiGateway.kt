package infra.service

import core.model.ai.IntentResponse
import core.model.user.UserType
import core.repository.AIGateway
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.swing.UIManager.put

class GeminiGateway(private val apiKey: String) : AIGateway {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) { json() }
    }

    private val systemPrompt = """
        You are NeoNav, an AI assistant embedded in a banking app.
        ALWAYS respond with ONLY valid JSON in this exact format, no markdown, no explanation:
        {
          "intent": "string (e.g. send_money, check_balance, view_transactions, open_support)",
          "deepLink": "string or null (e.g. /transfer, /balance, /transactions, /support)",
          "uiHint": "string or null (e.g. highlight_send_button, show_balance_card)",
          "reply": "string (friendly reply in the user's language)",
          "confidence": number between 0.0 and 1.0
        }
        Adapt tone: STUDENT=casual+emoji, BUSINESS=professional, SENIOR=simple, DEFAULT=neutral.
    """.trimIndent()

    override suspend fun parseIntent(
        message: String,
        userType: UserType,
        history: List<Pair<String, String>>
    ): IntentResponse {
        return try {
            val contents = buildJsonArray {
                history.forEach { (role, content) ->
                    add(buildJsonObject {
                        put("role", if (role == "assistant") "model" else "user")
                        put("parts", buildJsonArray {
                            add(buildJsonObject { put("text", content) })
                        })
                    })
                }
                // current message
                add(buildJsonObject {
                    put("role", "user")
                    put("parts", buildJsonArray {
                        add(buildJsonObject { put("text", "UserType: ${userType.name}\n$message") })
                    })
                })
            }

            val response = client.post(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"
            ) {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    put("system_instruction", buildJsonObject {
                        put("parts", buildJsonArray {
                            add(buildJsonObject { put("text", systemPrompt) })
                        })
                    })
                    put("contents", contents)
                    put("generationConfig", buildJsonObject {
                        put("responseMimeType", "application/json")
                        put("maxOutputTokens", 512)
                        put("temperature", 0.3)
                    })
                })
            }

            val body = response.body<JsonObject>()
            println("Gemini response: $body")

            val text = body["candidates"]
                ?.jsonArray?.firstOrNull()
                ?.jsonObject?.get("content")
                ?.jsonObject?.get("parts")
                ?.jsonArray?.firstOrNull()
                ?.jsonObject?.get("text")
                ?.jsonPrimitive?.content
                ?: throw IllegalStateException("Empty response from Gemini")

            parseResponse(text)

        } catch (e: Exception) {
            println("GeminiGateway error: ${e::class.simpleName}: ${e.message}")
            e.printStackTrace()
            IntentResponse(
                intent = "unknown",
                deepLink = null,
                uiHint = null,
                reply = "Încearcă din nou, te rog.",
                confidence = 0.0
            )
        }
    }

    private fun parseResponse(raw: String): IntentResponse {
        return try {
            val clean = raw.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val json = Json.parseToJsonElement(clean).jsonObject
            IntentResponse(
                intent = json["intent"]?.jsonPrimitive?.content ?: "unknown",
                deepLink = json["deepLink"]?.jsonPrimitive?.contentOrNull,
                uiHint = json["uiHint"]?.jsonPrimitive?.contentOrNull,
                reply = json["reply"]?.jsonPrimitive?.content ?: raw,
                confidence = json["confidence"]?.jsonPrimitive?.double ?: 0.5
            )
        } catch (e: Exception) {
            IntentResponse(
                intent = "unknown",
                deepLink = null,
                uiHint = null,
                reply = raw,
                confidence = 0.0
            )
        }
    }
}