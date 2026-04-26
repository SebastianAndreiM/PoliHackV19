import type { WalkthroughStep } from "../types/assistant";

export const transferWalkthrough: WalkthroughStep[] = [
    {
        id: "transfer-1",
        page: "payments",
        targetId: "nav-payments",
        title: "Go to payments",
        description: "This is where you start any transfer or payment.",
        assistantText:
            "Payments is the section used for sending money, paying bills or starting bank transfers.",
        position: "right",
    },
    {
        id: "transfer-2",
        page: "payments",
        targetId: "transfer-card",
        title: "Start a transfer",
        description: "This form is used when you want to send money.",
        assistantText:
            "This form starts a new transfer. You need the receiver IBAN, the amount and optionally a short payment description.",
        position: "left",
    },
    {
        id: "transfer-3",
        page: "payments",
        targetId: "iban-input",
        title: "Add the IBAN",
        description: "Paste or type the receiver IBAN here.",
        assistantText:
            "The IBAN identifies the receiver account. It is important to copy it exactly.",
        position: "top",
    },
    {
        id: "transfer-4",
        page: "payments",
        targetId: "amount-input",
        title: "Enter the amount",
        description: "Write how much money you want to send.",
        assistantText:
            "Here you enter the amount. Double check the currency and decimals before moving forward.",
        position: "top",
    },
    {
        id: "transfer-5",
        page: "payments",
        targetId: "confirm-transfer",
        title: "Confirm safely",
        description: "Review the details before confirming the transfer.",
        assistantText:
            "This is the final confirmation. Check the IBAN, amount and details one more time.",
        position: "top",
    },
];

export const cardsWalkthrough: WalkthroughStep[] = [
    {
        id: "cards-1",
        page: "cards",
        targetId: "nav-cards",
        title: "Go to cards",
        description: "This is where you manage your bank cards.",
        assistantText:
            "The Cards section helps you control your card, limits and security settings from one place.",
        position: "right",
    },
    {
        id: "cards-2",
        page: "cards",
        targetId: "card-management-card",
        title: "Card management",
        description: "Here you can freeze your card or manage card settings.",
        assistantText:
            "Card management is useful when you lose your card, want to block it temporarily, or change important security options.",
        position: "left",
    },
    {
        id: "cards-3",
        page: "cards",
        targetId: "daily-limit-card",
        title: "Daily limit",
        description: "This shows the current daily spending limit.",
        assistantText:
            "The daily limit controls how much can be spent in one day. It is useful for safety and budget control.",
        position: "left",
    },
    {
        id: "cards-4",
        page: "cards",
        targetId: "security-tips-card",
        title: "Security tips",
        description: "This area explains safe card usage.",
        assistantText:
            "Security tips explain what each option means before the user changes something sensitive.",
        position: "top",
    },
];

export const savingsWalkthrough: WalkthroughStep[] = [
    {
        id: "savings-1",
        page: "savings",
        targetId: "nav-savings",
        title: "Go to savings",
        description: "This is where you manage goals and savings.",
        assistantText:
            "Savings helps users organize money for goals like vacations, emergency funds or planned purchases.",
        position: "right",
    },
    {
        id: "savings-2",
        page: "savings",
        targetId: "saving-goals-card",
        title: "Saving goals",
        description: "Here you can create a new goal.",
        assistantText:
            "A saving goal turns a vague objective into a clear target with progress tracking.",
        position: "left",
    },
    {
        id: "savings-3",
        page: "savings",
        targetId: "vacation-fund-card",
        title: "Progress tracking",
        description: "This shows how close you are to your goal.",
        assistantText:
            "Progress tracking makes saving easier because the user sees how close they are to finishing the goal.",
        position: "left",
    },
    {
        id: "savings-4",
        page: "savings",
        targetId: "saving-ai-suggestion-card",
        title: "AI suggestion",
        description: "The assistant suggests a realistic saving action.",
        assistantText:
            "AI suggestions make the experience more personal by recommending small actions based on spending behavior.",
        position: "top",
    },
];

export const supportWalkthrough: WalkthroughStep[] = [
    {
        id: "support-1",
        page: "support",
        targetId: "nav-support",
        title: "Go to support",
        description: "This is where users ask for help.",
        assistantText:
            "Support is the fallback area when the user needs help, clarification or human assistance.",
        position: "right",
    },
    {
        id: "support-2",
        page: "support",
        targetId: "help-center-card",
        title: "Help center",
        description: "Ask the assistant or contact support.",
        assistantText:
            "The help center gives users a clear place to ask questions instead of searching through the whole app.",
        position: "left",
    },
    {
        id: "support-3",
        page: "support",
        targetId: "estimated-wait-card",
        title: "Estimated wait",
        description: "This shows how long human support might take.",
        assistantText:
            "Showing the estimated wait reduces uncertainty and helps users decide if they want AI help first.",
        position: "left",
    },
    {
        id: "support-4",
        page: "support",
        targetId: "handoff-card",
        title: "Human handoff",
        description: "The AI summarizes the issue before escalation.",
        assistantText:
            "A good handoff means the user does not need to repeat the same problem again to a human advisor.",
        position: "top",
    },
];

export const onboardingWalkthrough: WalkthroughStep[] = [
    {
        id: "onboarding-1",
        page: "overview",
        targetId: "balance-card",
        title: "Your balance",
        description: "Here you always see how much money you currently have.",
        assistantText:
            "This card shows your current balance and the account IBAN. It is the fastest way to check your available money.",
        position: "bottom",
    },
    {
        id: "onboarding-2",
        page: "overview",
        targetId: "nav-payments",
        title: "Payments",
        description: "Use this section for transfers, bills and recurring payments.",
        assistantText:
            "Payments is where you send money, pay bills or manage recurring payments.",
        position: "right",
    },
    {
        id: "onboarding-3",
        page: "overview",
        targetId: "nav-cards",
        title: "Cards",
        description: "Here you manage your cards, limits and security settings.",
        assistantText:
            "Cards lets you freeze your card, change limits or check security settings.",
        position: "right",
    },
    {
        id: "onboarding-4",
        page: "overview",
        targetId: "assistant-button",
        title: "AI assistant",
        description: "Whenever you are stuck, ask the assistant and it will guide you.",
        assistantText:
            "The assistant stays available all the time. You can ask what you want to do and it will guide you step by step.",
        position: "left",
    },
];