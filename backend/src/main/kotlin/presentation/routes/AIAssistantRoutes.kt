package presentation.routes

import core.model.user.UserType
import core.usecase.ai.GetChatHistoryUseCase
import core.usecase.ai.SendMessageUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import presentation.dto.ai.ChatHistoryResponse
import presentation.dto.ai.ChatRequest
import presentation.dto.ai.ChatResponse
import java.util.UUID

fun Route.aiAssistantRoutes(
    sendMessage: SendMessageUseCase,
    getChatHistory: GetChatHistoryUseCase
) {
    route("/ai") {

        // ── POST /chat ───────────────────────────────────────────
        post("/chat") {
            val req = call.receive<ChatRequest>()
            val userType = runCatching {
                UserType.valueOf(req.userType.uppercase())
            }.getOrDefault(UserType.DEFAULT)

            val response = sendMessage.execute(
                userId = UUID.fromString(req.userId),
                userType = userType,
                message = req.message
            )

            call.respond(
                HttpStatusCode.OK, ChatResponse(
                    id = response.id.toString(),
                    reply = response.content,
                    deepLink = response.deepLink,
                    uiHint = response.uiHint,
                    role = response.role.name,
                    createdAt = response.createdAt.toString()
                )
            )
        }

        // ── GET /history/{userId} ────────────────────────────────
        get("/history/{userId}") {
            val userId = UUID.fromString(call.parameters["userId"])
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val history = getChatHistory.execute(userId, limit)

            call.respond(
                ChatHistoryResponse(
                    userId = userId.toString(),
                    messages = history.map {
                        ChatResponse(
                            id = it.id.toString(),
                            reply = it.content,
                            deepLink = it.deepLink,
                            uiHint = it.uiHint,
                            role = it.role.name,
                            createdAt = it.createdAt.toString()
                        )
                    }
                ))
        }
    }
}