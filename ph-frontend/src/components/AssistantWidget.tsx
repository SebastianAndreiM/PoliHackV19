import { useEffect, useState } from "react";
import { sendChatMessage } from "../api/aiApi";
import { initialAssistantMessages } from "../mocks/assistantMock";
import type { AssistantMessage, WalkthroughStep } from "../types/assistant";
import type { UserType } from "../types/ui";
import {
    cardsWalkthrough,
    savingsWalkthrough,
    supportWalkthrough,
    transferWalkthrough,
} from "../mocks/walkthroughMock";

type Props = {
    userId: string;
    userType: UserType;
    onStartWalkthrough: (steps: WalkthroughStep[]) => void;
    activeStep?: WalkthroughStep | null;
};

function walkthroughForDeepLink(deepLink: string | null): WalkthroughStep[] {
    if (!deepLink) return [];
    if (deepLink.startsWith("/transfer")) return transferWalkthrough;
    if (deepLink.startsWith("/card")) return cardsWalkthrough;
    if (deepLink.startsWith("/savings")) return savingsWalkthrough;
    if (deepLink.startsWith("/support")) return supportWalkthrough;
    return [];
}

export function AssistantWidget({ userId, userType, onStartWalkthrough, activeStep }: Props) {
    const [open, setOpen] = useState(true);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [lastExplainedStepId, setLastExplainedStepId] = useState<string | null>(null);
    const [messages, setMessages] = useState<AssistantMessage[]>(initialAssistantMessages);

    useEffect(() => {
        if (!activeStep) return;
        if (activeStep.id === lastExplainedStepId) return;

        const message: AssistantMessage = {
            id: crypto.randomUUID(),
            role: "assistant",
            text: activeStep.assistantText,
        };

        setMessages((current) => [...current, message]);
        setLastExplainedStepId(activeStep.id);
    }, [activeStep, lastExplainedStepId]);

    async function sendMessage() {
        if (!input.trim() || loading) return;

        const userMessage: AssistantMessage = {
            id: crypto.randomUUID(),
            role: "user",
            text: input,
        };

        setMessages((current) => [...current, userMessage]);
        const sentText = input;
        setInput("");
        setLoading(true);

        try {
            const response = await sendChatMessage({ userId, userType, message: sentText });

            setMessages((current) => [
                ...current,
                { id: response.id, role: "assistant", text: response.reply },
            ]);

            const steps = walkthroughForDeepLink(response.deepLink);
            if (steps.length > 0) {
                onStartWalkthrough(steps);
            }
        } catch (err) {
            const detail = err instanceof Error ? err.message : "eroare necunoscută";
            setMessages((current) => [
                ...current,
                {
                    id: crypto.randomUUID(),
                    role: "assistant",
                    text: `Eroare: ${detail}`,
                },
            ]);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="assistant-shell">
            <button
                id="assistant-button"
                className="assistant-toggle"
                onClick={() => setOpen((value) => !value)}
            >
                AI
            </button>

            {open && (
                <div className="assistant-panel">
                    <div className="assistant-header">
                        <div>
                            <strong>AssetGuard AI</strong>
                            <span>Banking walkthrough agent</span>
                        </div>
                        <button onClick={() => setOpen(false)}>×</button>
                    </div>

                    <div className="assistant-messages">
                        {messages.map((message) => (
                            <div
                                key={message.id}
                                className={`assistant-message ${message.role}`}
                            >
                                {message.text}
                            </div>
                        ))}
                        {loading && (
                            <div className="assistant-message assistant">...</div>
                        )}
                    </div>

                    <div className="assistant-input">
                        <input
                            value={input}
                            placeholder="Try: I want to make a transfer"
                            onChange={(event) => setInput(event.target.value)}
                            onKeyDown={(event) => {
                                if (event.key === "Enter") sendMessage();
                            }}
                            disabled={loading}
                        />
                        <button onClick={sendMessage} disabled={loading}>
                            Send
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}
