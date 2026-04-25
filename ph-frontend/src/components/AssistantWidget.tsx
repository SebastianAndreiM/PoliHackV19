import { useEffect, useState } from "react";
import type { AssistantMessage, WalkthroughStep } from "../types/assistant";
import {
    getMockAssistantReply,
    initialAssistantMessages,
} from "../mocks/assistantMock";

type Props = {
    onStartWalkthrough: (steps: WalkthroughStep[]) => void;
    activeStep?: WalkthroughStep | null;
};

export function AssistantWidget({
                                    onStartWalkthrough,
                                    activeStep,
                                }: Props) {
    const [open, setOpen] = useState(true);
    const [input, setInput] = useState("");
    const [lastExplainedStepId, setLastExplainedStepId] = useState<string | null>(
        null
    );
    const [messages, setMessages] = useState<AssistantMessage[]>(
        initialAssistantMessages
    );

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

    function sendMessage() {
        if (!input.trim()) return;

        const userMessage: AssistantMessage = {
            id: crypto.randomUUID(),
            role: "user",
            text: input,
        };

        const reply = getMockAssistantReply(input);

        const assistantMessage: AssistantMessage = {
            id: crypto.randomUUID(),
            role: "assistant",
            text: reply.message,
        };

        setMessages((current) => [...current, userMessage, assistantMessage]);
        setInput("");

        if (reply.walkthrough.length > 0) {
            onStartWalkthrough(reply.walkthrough);
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
                    </div>

                    <div className="assistant-input">
                        <input
                            value={input}
                            placeholder="Try: I want to make a transfer"
                            onChange={(event) => setInput(event.target.value)}
                            onKeyDown={(event) => {
                                if (event.key === "Enter") sendMessage();
                            }}
                        />
                        <button onClick={sendMessage}>Send</button>
                    </div>
                </div>
            )}
        </div>
    );
}