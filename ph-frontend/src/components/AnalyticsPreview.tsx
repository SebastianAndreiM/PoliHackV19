export function AnalyticsPreview() {
    return (
        <section className="analytics-preview">
            <div>
                <span className="eyebrow">For banks</span>
                <h2>Confusion analytics, not just onboarding</h2>
                <p>
                    The mocked dashboard shows where users get stuck, which flows are
                    abandoned and what the AI assistant is asked most often.
                </p>
            </div>

            <div className="analytics-grid">
                <div className="metric-card">
                    <span>Onboarding completion</span>
                    <strong>84%</strong>
                    <small>+31% estimated uplift</small>
                </div>

                <div className="metric-card">
                    <span>Most confusing flow</span>
                    <strong>Transfers</strong>
                    <small>42% of help requests</small>
                </div>

                <div className="metric-card">
                    <span>Support deflection</span>
                    <strong>63%</strong>
                    <small>handled by AI guide</small>
                </div>
            </div>
        </section>
    );
}