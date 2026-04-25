import type { AssistantMessage } from "../types/assistant";
import { transferWalkthrough } from "./walkthroughMock";

export const initialAssistantMessages: AssistantMessage[] = [
    {
        id: "m-1",
        role: "assistant",
        text: "Hi, I am your banking guide. Ask me what you want to do and I will walk you through it.",
    },
];

export function getMockAssistantReply(input: string) {
    const normalized = input.toLowerCase();

    if (
        normalized.includes("transfer") ||
        normalized.includes("send") ||
        normalized.includes("plata") ||
        normalized.includes("transfer")
    ) {
        return {
            message: "Sure. I will guide you step by step through a safe transfer.",
            walkthrough: transferWalkthrough,
        };
    }

    if (
        normalized.includes("card") ||
        normalized.includes("limit") ||
        normalized.includes("freeze")
    ) {
        return {
            message:
                "Card management is in the Cards area. I can guide you there and explain each option.",
            walkthrough: [],
        };
    }

    return {
        message:
            "I can help with transfers, cards, savings, account details and app navigation.",
        walkthrough: [],
    };
}