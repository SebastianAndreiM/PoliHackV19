import type { AdaptiveLayout } from "../types/ui";

type Props = {
    layout: AdaptiveLayout | null;
    loading: boolean;
};

export function LayoutPreview({ layout, loading }: Props) {
    return (
        <section className="layout-preview">
            <div>
                <span className="eyebrow">Backend layout response</span>
                <h3>{loading ? "Loading layout..." : "Current adaptive layout"}</h3>
            </div>

            <pre>{layout ? JSON.stringify(layout, null, 2) : "No layout loaded"}</pre>
        </section>
    );
}