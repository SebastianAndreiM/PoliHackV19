export type BankPage = "overview" | "payments" | "cards" | "savings" | "support";

export type AssistantMessage = {
    id: string;
    role: "user" | "assistant";
    text: string;
};

export type WalkthroughStep = {
    id: string;
    targetId: string;
    page?: BankPage;
    title: string;
    description: string;
    assistantText: string;
    position: "top" | "right" | "bottom" | "left";
};