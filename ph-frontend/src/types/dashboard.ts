export type DashboardMetric = {
    label: string;
    value: string;
    helper: string;
};

export type FrictionPoint = {
    id: string;
    screen: string;
    component: string;
    issue: string;
    severity: "low" | "medium" | "high";
    dropOffRate: number;
};

export type HeatmapZone = {
    id: string;
    label: string;
    intensity: "low" | "medium" | "high";
    top: string;
    left: string;
    width: string;
    height: string;
};