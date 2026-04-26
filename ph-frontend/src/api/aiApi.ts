import { http } from "./http";

export type ChatRequest = {
    userId: string;
    userType: string;
    message: string;
};

export type ChatResponse = {
    id: string;
    reply: string;
    deepLink: string | null;
    uiHint: string | null;
    role: string;
    createdAt: string;
};

export async function sendChatMessage(request: ChatRequest): Promise<ChatResponse> {
    return http<ChatResponse>("/api/v1/ai/chat", {
        method: "POST",
        body: JSON.stringify(request),
    });
}
