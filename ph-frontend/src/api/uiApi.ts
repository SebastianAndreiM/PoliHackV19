import { http } from "./http";
import type { AdaptiveLayout, UserType } from "../types/ui";

export async function getLayoutByUserType(
    userType: UserType
): Promise<AdaptiveLayout> {
    return http<AdaptiveLayout>(`/api/v1/ui/layout/${userType}`);
}