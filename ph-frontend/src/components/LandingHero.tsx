type Props = {
    onStartOnboarding: () => void;
};

export function LandingHero({ onStartOnboarding }: Props) {
    return (
        <section className="landing-hero">
            <div className="hero-copy">
                <span className="eyebrow">AssetGuard SDK</span>
                <h1>An AI ghost layer for banking apps</h1>
                <p>
                    A plug-in walkthrough agent that helps users understand banking flows,
                    complete tasks faster and avoid confusion.
                </p>

                <div className="hero-actions">
                    <button className="primary" onClick={onStartOnboarding}>
                        Start onboarding demo
                    </button>
                    <a href="#demo">View banking demo</a>
                </div>
            </div>

            <div className="hero-card">
                <div className="mini-phone">
                    <div className="phone-top"></div>
                    <div className="phone-balance">12,450.75 RON</div>
                    <div className="phone-row"></div>
                    <div className="phone-row short"></div>
                    <div className="phone-grid">
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                </div>
            </div>
        </section>
    );
}