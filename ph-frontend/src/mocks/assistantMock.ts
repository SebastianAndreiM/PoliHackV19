import type { AssistantMessage } from "../types/assistant";
import {
    cardsWalkthrough,
    savingsWalkthrough,
    supportWalkthrough,
    transferWalkthrough,
} from "./walkthroughMock";

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
        normalized.includes("payment")
    ) {
        return {
            message: "Sure. I will guide you step by step through a safe transfer.",
            walkthrough: transferWalkthrough,
        };
    }

    if (
        normalized.includes("card") ||
        normalized.includes("cards") ||
        normalized.includes("limit") ||
        normalized.includes("freeze")
    ) {
        return {
            message: "Sure. I will guide you through the Cards section.",
            walkthrough: cardsWalkthrough,
        };
    }

    if (
        normalized.includes("saving") ||
        normalized.includes("savings") ||
        normalized.includes("goal")
    ) {
        return {
            message: "Sure. I will guide you through the Savings section.",
            walkthrough: savingsWalkthrough,
        };
    }

    if (
        normalized.includes("support") ||
        normalized.includes("help") ||
        normalized.includes("advisor")
    ) {
        return {
            message: "Sure. I will guide you through the Support section.",
            walkthrough: supportWalkthrough,
        };
    }

    return {
        message:
            "I can help with transfers, cards, savings, account details and app navigation.",
        walkthrough: [],
    };
}