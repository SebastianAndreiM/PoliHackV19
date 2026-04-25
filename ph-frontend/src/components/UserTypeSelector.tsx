import type { UserProfile, UserType } from "../types/user";

type Props = {
    user: UserProfile | null;
    loading: boolean;
    onChangeType: (type: UserType) => void;
};

const options: { label: string; value: UserType }[] = [
    { label: "Student", value: "STUDENT" },
    { label: "Business", value: "BUSINESS" },
    { label: "Senior", value: "SENIOR" },
    { label: "Default", value: "DEFAULT" },
];

export function UserTypeSelector({ user, loading, onChangeType }: Props) {
    return (
        <div className="user-type-card">
            <div>
                <span className="eyebrow">Live backend integration</span>
                <h3>User profile</h3>
                <p>
                    Current type: <strong>{user?.userType ?? "not registered"}</strong>
                </p>
            </div>

            <div className="type-buttons">
                {options.map((option) => (
                    <button
                        key={option.value}
                        disabled={loading || !user}
                        className={user?.userType === option.value ? "selected" : ""}
                        onClick={() => onChangeType(option.value)}
                    >
                        {option.label}
                    </button>
                ))}
            </div>
        </div>
    );
}