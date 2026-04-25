import type { WalkthroughStep } from "../types/assistant";

export const transferWalkthrough: WalkthroughStep[] = [
    {
        id: "step-1",
        targetId: "nav-payments",
        title: "Go to payments",
        description: "This is where you start any transfer or payment.",
        assistantText:
            "Payments is the section used for sending money, paying bills or starting bank transfers.",
        position: "right",
    },
    {
        id: "step-2",
        targetId: "transfer-card",
        title: "Start a transfer",
        description: "Choose this option when you want to send money to someone.",
        assistantText:
            "This form starts a new transfer. You need the receiver IBAN, the amount and optionally a short payment description.",
        position: "left",
    },
    {
        id: "step-3",
        targetId: "iban-input",
        title: "Add the IBAN",
        description: "Paste or type the receiver IBAN here.",
        assistantText:
            "The IBAN identifies the receiver account. It is important to copy it exactly.",
        position: "top",
    },
    {
        id: "step-4",
        targetId: "amount-input",
        title: "Enter the amount",
        description: "Write how much money you want to send.",
        assistantText:
            "Here you enter the amount. Double check the currency and decimals before moving forward.",
        position: "top",
    },
    {
        id: "step-5",
        targetId: "confirm-transfer",
        title: "Confirm safely",
        description: "Review the details before confirming the transfer.",
        assistantText:
            "This is the final confirmation. Check the IBAN, amount and details one more time.",
        position: "top",
    },
];

export const onboardingWalkthrough: WalkthroughStep[] = [
    {
        id: "onboarding-1",
        targetId: "balance-card",
        title: "Your balance",
        description: "Here you always see how much money you currently have.",
        assistantText:
            "This card shows your current balance and the account IBAN. It is the fastest way to check your available money.",
        position: "bottom",
    },
    {
        id: "onboarding-2",
        targetId: "nav-payments",
        title: "Payments",
        description: "Use this section for transfers, bills and recurring payments.",
        assistantText:
            "Payments is where you send money, pay bills or manage recurring payments.",
        position: "right",
    },
    {
        id: "onboarding-3",
        targetId: "nav-cards",
        title: "Cards",
        description: "Here you manage your cards, limits and security settings.",
        assistantText:
            "Cards lets you freeze your card, change limits or check security settings.",
        position: "right",
    },
    {
        id: "onboarding-4",
        targetId: "assistant-button",
        title: "AI assistant",
        description: "Whenever you are stuck, ask the assistant and it will guide you.",
        assistantText:
            "The assistant stays available all the time. You can ask what you want to do and it will guide you step by step.",
        position: "left",
    },
];