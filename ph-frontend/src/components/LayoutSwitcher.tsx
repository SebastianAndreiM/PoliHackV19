import type { UserType } from "../types/ui";

type Props = {
    selectedType: UserType;
    onChange: (type: UserType) => void;
};

const options: UserType[] = ["DEFAULT", "STUDENT", "BUSINESS", "SENIOR"];

export function LayoutSwitcher({ selectedType, onChange }: Props) {
    return (
        <section className="layout-switcher">
            <div>
                <span className="eyebrow">Adaptive UI demo</span>
                <h3>Choose layout type</h3>
                <p>No register needed. Layout is fetched directly from backend.</p>
            </div>

            <div className="layout-switcher-buttons">
                {options.map((type) => (
                    <button
                        key={type}
                        className={selectedType === type ? "selected" : ""}
                        onClick={() => onChange(type)}
                    >
                        {type}
                    </button>
                ))}
            </div>
        </section>
    );
}