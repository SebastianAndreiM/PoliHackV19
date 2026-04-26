import type {
    DashboardMetric,
    FrictionPoint,
    HeatmapZone,
} from "../types/dashboard";

export const dashboardMetrics: DashboardMetric[] = [
    {
        label: "Onboarding completion",
        value: "84%",
        helper: "+31% estimated uplift",
    },
    {
        label: "Most confusing flow",
        value: "Transfers",
        helper: "42% of help requests",
    },
    {
        label: "Support deflection",
        value: "63%",
        helper: "handled by AI guide",
    },
    {
        label: "Avg. time saved",
        value: "2.4m",
        helper: "per completed task",
    },
];

export const frictionPoints: FrictionPoint[] = [
    {
        id: "fp-1",
        screen: "Payments",
        component: "IBAN field",
        issue: "Users hesitate before entering receiver details.",
        severity: "high",
        dropOffRate: 42,
    },
    {
        id: "fp-2",
        screen: "Cards",
        component: "Daily limit",
        issue: "Users do not understand the impact of changing limits.",
        severity: "medium",
        dropOffRate: 27,
    },
    {
        id: "fp-3",
        screen: "Savings",
        component: "Create goal",
        issue: "Users abandon when they need to choose a target amount.",
        severity: "medium",
        dropOffRate: 22,
    },
    {
        id: "fp-4",
        screen: "Support",
        component: "Human advisor",
        issue: "Users contact support for flows that AI can guide.",
        severity: "low",
        dropOffRate: 14,
    },
];

export const heatmapZones: HeatmapZone[] = [
    {
        id: "hm-1",
        label: "IBAN",
        intensity: "high",
        top: "34%",
        left: "28%",
        width: "34%",
        height: "10%",
    },
    {
        id: "hm-2",
        label: "Amount",
        intensity: "medium",
        top: "48%",
        left: "28%",
        width: "28%",
        height: "10%",
    },
    {
        id: "hm-3",
        label: "Confirm",
        intensity: "high",
        top: "63%",
        left: "28%",
        width: "38%",
        height: "10%",
    },
    {
        id: "hm-4",
        label: "Support",
        intensity: "low",
        top: "78%",
        left: "61%",
        width: "22%",
        height: "8%",
    },
];