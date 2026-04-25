import type { WalkthroughStep } from "../types/assistant";

export const transferWalkthrough: WalkthroughStep[] = [
    {
        id: "step-1",
        targetId: "nav-payments",
        title: "Go to payments",
        description: "This is where you start any transfer or payment.",
        position: "right",
        assistantText: ""
    },
    {
        id: "step-2",
        targetId: "transfer-card",
        title: "Start a transfer",
        description: "Choose this option when you want to send money to someone.",
        position: "left",
        assistantText: ""
    },
    {
        id: "step-3",
        targetId: "iban-input",
        title: "Add the IBAN",
        description: "Paste or type the receiver IBAN here. We will check the format.",
        position: "top",
        assistantText: ""
    },
    {
        id: "step-4",
        targetId: "amount-input",
        title: "Enter the amount",
        description: "Write how much money you want to send.",
        position: "top",
        assistantText: ""
    },
    {
        id: "step-5",
        targetId: "confirm-transfer",
        title: "Confirm safely",
        description: "Review the details before confirming the transfer.",
        position: "top",
        assistantText: ""
    },
];

export const onboardingWalkthrough: WalkthroughStep[] = [
    {
        id: "onboarding-1",
        targetId: "balance-card",
        title: "Your balance",
        description: "Here you always see how much money you currently have.",
        position: "bottom",
        assistantText: ""
    },
    {
        id: "onboarding-2",
        targetId: "nav-payments",
        title: "Payments",
        description: "Use this section for transfers, bills and recurring payments.",
        position: "right",
        assistantText: ""
    },
    {
        id: "onboarding-3",
        targetId: "nav-cards",
        title: "Cards",
        description: "Here you manage your cards, limits and security settings.",
        position: "right",
        assistantText: ""
    },
    {
        id: "onboarding-4",
        targetId: "assistant-button",
        title: "AI assistant",
        description: "Whenever you are stuck, ask the assistant and it will guide you.",
        position: "left",
        assistantText: ""
    },
];