export type UserType = "STUDENT" | "BUSINESS" | "SENIOR" | "DEFAULT";

export type UserProfile = {
    id: string;
    externalId: string;
    userType: UserType;
    locale: string;
    createdAt: string;
};

export type RegisterRequest = {
    externalId: string;
    locale: string;
};

export type UpdateUserTypeRequest = {
    userType: UserType;
};