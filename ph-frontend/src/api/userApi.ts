import { http } from "./http";
import type {
    RegisterRequest,
    UpdateUserTypeRequest,
    UserProfile,
} from "../types/user";

const USE_MOCKS = import.meta.env.VITE_USE_MOCKS !== "false";

let mockUser: UserProfile | null = null;

export async function registerUser(
    request: RegisterRequest
): Promise<UserProfile> {
    if (USE_MOCKS) {
        mockUser = {
            id: crypto.randomUUID(),
            externalId: request.externalId,
            locale: request.locale,
            userType: "DEFAULT",
            createdAt: new Date().toISOString(),
        };

        return mockUser;
    }

    return http<UserProfile>("/api/v1/users/register", {
        method: "POST",
        body: JSON.stringify(request),
    });
}

export async function getUserProfile(id: string): Promise<UserProfile> {
    if (USE_MOCKS) {
        if (!mockUser) {
            throw new Error("Mock user not registered");
        }

        return mockUser;
    }

    return http<UserProfile>(`/api/v1/users/${id}/profile`);
}

export async function updateUserType(
    id: string,
    request: UpdateUserTypeRequest
): Promise<UserProfile> {
    if (USE_MOCKS) {
        if (!mockUser) {
            throw new Error("Mock user not registered");
        }

        mockUser = {
            ...mockUser,
            userType: request.userType,
        };

        return mockUser;
    }

    return http<UserProfile>(`/api/v1/users/${id}/type`, {
        method: "PATCH",
        body: JSON.stringify(request),
    });
}