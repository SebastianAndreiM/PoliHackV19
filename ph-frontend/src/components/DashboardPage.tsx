import {
    dashboardMetrics,
    frictionPoints,
    heatmapZones,
} from "../mocks/dashboardMock";

export function DashboardPage() {
    return (
        <section className="dashboard-page">
            <DashboardHeader />

            <div className="dashboard-metrics">
                {dashboardMetrics.map((metric) => (
                    <div className="dashboard-metric-card" key={metric.label}>
                        <span>{metric.label}</span>
                        <strong>{metric.value}</strong>
                        <small>{metric.helper}</small>
                    </div>
                ))}
            </div>

            <div className="dashboard-main-grid">
                <HeatmapPanel />
                <FrictionPanel />
            </div>

            <div className="dashboard-bottom-grid">
                <AiInsightPanel />
                <FlowPerformancePanel />
            </div>
        </section>
    );
}

function DashboardHeader() {
    return (
        <div className="dashboard-header">
            <div>
                <span className="eyebrow">Bank dashboard</span>
                <h2>Behavioral heatmap</h2>
                <p>
                    See where users get confused, hesitate or abandon flows inside the
                    banking app.
                </p>
            </div>

            <div className="dashboard-header-actions">
                <button>Last 7 days</button>
                <button className="primary">Export report</button>
            </div>
        </div>
    );
}

function HeatmapPanel() {
    return (
        <div className="dashboard-panel heatmap-panel">
            <div className="panel-title-row">
                <div>
                    <span className="eyebrow">Live confusion map</span>
                    <h3>Transfer flow heatmap</h3>
                </div>
                <span className="status-pill">Mock telemetry</span>
            </div>

            <div className="phone-heatmap">
                <div className="phone-notch" />
                <div className="phone-screen-title">Send money</div>

                <div className="phone-field">Receiver IBAN</div>
                <div className="phone-field">Amount</div>
                <div className="phone-field short">Details</div>
                <div className="phone-button">Confirm transfer</div>

                {heatmapZones.map((zone) => (
                    <div
                        key={zone.id}
                        className={`heatmap-zone ${zone.intensity}`}
                        style={{
                            top: zone.top,
                            left: zone.left,
                            width: zone.width,
                            height: zone.height,
                        }}
                    >
                        {zone.label}
                    </div>
                ))}
            </div>
        </div>
    );
}

function FrictionPanel() {
    return (
        <div className="dashboard-panel">
            <div className="panel-title-row">
                <div>
                    <span className="eyebrow">Top friction points</span>
                    <h3>What blocks users</h3>
                </div>
            </div>

            <div className="friction-list">
                {frictionPoints.map((point) => (
                    <div className="friction-item" key={point.id}>
                        <div>
                            <strong>
                                {point.screen} · {point.component}
                            </strong>
                            <p>{point.issue}</p>
                        </div>

                        <div className="friction-meta">
              <span className={`severity ${point.severity}`}>
                {point.severity}
              </span>
                            <b>{point.dropOffRate}%</b>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

function AiInsightPanel() {
    return (
        <div className="dashboard-panel dark-panel">
            <span className="eyebrow">AI recommendation</span>
            <h3>Reduce transfer abandonment</h3>
            <p>
                Users hesitate most at the IBAN field. Add a proactive explanation before
                the field and trigger a short walkthrough when the user pauses for more
                than 4 seconds.
            </p>

            <div className="recommendation-list">
                <span>Expected support reduction: 18%</span>
                <span>Estimated completion uplift: +12%</span>
                <span>Best target segment: Senior users</span>
            </div>
        </div>
    );
}

function FlowPerformancePanel() {
    return (
        <div className="dashboard-panel">
            <span className="eyebrow">Flow performance</span>
            <h3>Guided vs unguided users</h3>

            <div className="bar-chart">
                <div>
                    <span>Unguided</span>
                    <div className="bar-track">
                        <div className="bar-fill unguided" />
                    </div>
                    <b>56%</b>
                </div>

                <div>
                    <span>With AssetGuard</span>
                    <div className="bar-track">
                        <div className="bar-fill guided" />
                    </div>
                    <b>84%</b>
                </div>
            </div>
        </div>
    );
}