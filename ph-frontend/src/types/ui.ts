export type UserType = "STUDENT" | "BUSINESS" | "SENIOR" | "DEFAULT";

export type AdaptiveComponent = {
    key: string;
    order: number;
    visible: boolean;
    config: Record<string, unknown>;
};

export type AdaptiveLayout = {
    userType: UserType;
    theme: string;
    components: AdaptiveComponent[];
    deepLinks: {
        primaryAction?: string;
        secondaryAction?: string;
    };
};