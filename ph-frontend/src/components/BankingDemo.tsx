import { mockAccounts, mockTransactions } from "../mocks/bankingMock";
import type { BankPage } from "../types/assistant";
import type { AdaptiveLayout } from "../types/ui";

type Props = {
    layout: AdaptiveLayout | null;
    page: BankPage;
    onChangePage: (page: BankPage) => void;
};

export function BankingDemo({ layout, page, onChangePage }: Props) {
    const mainAccount = mockAccounts[0];

    const components = [...(layout?.components ?? [])].sort(
        (a, b) => a.order - b.order
    );

    function isVisible(key: string) {
        return (
            components.find((component) => component.key === key)?.visible ?? true
        );
    }

    function getConfig(key: string) {
        return components.find((component) => component.key === key)?.config ?? {};
    }

    const isAccessible = layout?.theme === "accessible";
    const fontScale = isAccessible ? 1.18 : 1;
    const explanationLevel = isAccessible ? "detailed" : "balanced";

    const recentTransactionsConfig = getConfig("recent_transactions");
    const transactionLimit =
        typeof recentTransactionsConfig.limit === "number"
            ? recentTransactionsConfig.limit
            : 3;

    const quickActionsConfig = getConfig("quick_actions");
    const quickActions = Array.isArray(quickActionsConfig.actions)
        ? quickActionsConfig.actions
        : [];

    const navItems = [
        { label: "Overview", page: "overview" as BankPage, visible: true },
        {
            label: "Payments",
            page: "payments" as BankPage,
            visible: isVisible("quick_actions"),
        },
        { label: "Cards", page: "cards" as BankPage, visible: true },
        {
            label: "Savings",
            page: "savings" as BankPage,
            visible: isVisible("fx_rates"),
        },
        {
            label: "Support",
            page: "support" as BankPage,
            visible: isVisible("support_banner"),
        },
    ].filter((item) => item.visible);

    return (
        <section
            id="demo"
            className={`banking-demo ${isAccessible ? "accessible-layout" : ""}`}
        >
            <aside className="bank-sidebar">
                <div className="brand-mark">AG</div>

                <nav>
                    {navItems.map((item) => (
                        <button
                            key={item.page}
                            id={`nav-${item.page}`}
                            className={page === item.page ? "active" : ""}
                            onClick={() => onChangePage(item.page)}
                        >
                            {item.label}
                        </button>
                    ))}
                </nav>
            </aside>

            <main className="bank-main" style={{ fontSize: `${fontScale}em` }}>
                {page === "overview" && (
                    <>
                        <div className="bank-header">
                            <div>
                                <span className="eyebrow">NestBank demo app</span>
                                <h2>Good afternoon, Sebastian</h2>
                            </div>
                            <button className="ghost-button">Secure session</button>
                        </div>

                        <div className="bank-grid">
                            {isVisible("balance_card") && (
                                <div id="balance-card" className="balance-card">
                                    <span>Total balance</span>
                                    <strong>
                                        {mainAccount.balance.toLocaleString("en-US", {
                                            minimumFractionDigits: 2,
                                        })}{" "}
                                        {mainAccount.currency}
                                    </strong>
                                    <small>{mainAccount.iban}</small>
                                </div>
                            )}

                            {isVisible("quick_actions") && (
                                <TransferCard
                                    explanationLevel={explanationLevel}
                                    quickActions={quickActions}
                                />
                            )}

                            {isVisible("support_banner") && (
                                <div className="support-banner">
                                    <strong>Need help?</strong>
                                    <span>
                    AssetGuard AI can guide you step by step through any banking
                    flow.
                  </span>
                                </div>
                            )}

                            {isVisible("recent_transactions") && (
                                <Transactions limit={transactionLimit} />
                            )}
                        </div>
                    </>
                )}

                {page === "payments" && (
                    <>
                        <div className="bank-header">
                            <div>
                                <span className="eyebrow">Payments</span>
                                <h2>Make a safe transfer</h2>
                            </div>
                            <button className="ghost-button">Verified flow</button>
                        </div>

                        <div className="bank-grid">
                            <TransferCard
                                explanationLevel={explanationLevel}
                                quickActions={quickActions}
                            />

                            {isVisible("balance_card") && (
                                <div className="balance-card">
                                    <span>From account</span>
                                    <strong>Main account</strong>
                                    <small>{mainAccount.iban}</small>
                                </div>
                            )}

                            {isVisible("support_banner") && (
                                <div className="support-banner">
                                    <strong>Not sure what to do?</strong>
                                    <span>
                    Ask AssetGuard AI and it will explain each field before you
                    confirm.
                  </span>
                                </div>
                            )}
                        </div>
                    </>
                )}

                {page === "cards" && <CardsPage />}

                {page === "savings" && <SavingsPage />}

                {page === "support" && <SupportPage />}
            </main>
        </section>
    );
}

function TransferCard({
                          explanationLevel,
                          quickActions,
                      }: {
    explanationLevel: string;
    quickActions: unknown[];
}) {
    const showRequestAction = quickActions.includes("request");

    return (
        <div id="transfer-card" className="transfer-card">
            <span className="eyebrow">Quick transfer</span>

            <label>
                Receiver IBAN
                <input id="iban-input" placeholder="RO49 BTRL..." />
                {explanationLevel !== "short" && (
                    <p className="field-hint">
                        The IBAN identifies the receiver account.
                    </p>
                )}
                {explanationLevel === "detailed" && (
                    <p className="field-hint">Double check the IBAN carefully.</p>
                )}
            </label>

            <label>
                Amount
                <input id="amount-input" placeholder="250.00" />
            </label>

            <button id="confirm-transfer" className="primary">
                Confirm transfer
            </button>

            {showRequestAction && (
                <button type="button" className="ghost-button">
                    Request money
                </button>
            )}
        </div>
    );
}

function Transactions({ limit }: { limit: number }) {
    return (
        <div className="transactions-card">
            <h3>Recent activity</h3>

            {mockTransactions.slice(0, limit).map((transaction) => (
                <div key={transaction.id} className="transaction-row">
                    <span>{transaction.title}</span>
                    <b>{transaction.amount} RON</b>
                </div>
            ))}
        </div>
    );
}

function CardsPage() {
    return (
        <div className="mock-page-grid">
            <div id="card-management-card" className="mock-feature-card">
                <span className="eyebrow">Cards</span>
                <h2>Card management</h2>
                <p>Freeze cards, change limits, view PIN help and security settings.</p>
                <button className="primary">Freeze card</button>
            </div>

            <div id="daily-limit-card" className="mock-feature-card">
                <h3>Daily limit</h3>
                <strong>2,500 RON</strong>
                <p>Recommended for your current spending pattern.</p>
            </div>

            <div id="security-tips-card" className="mock-feature-card wide">
                <h3>Security tips</h3>
                <p>
                    AssetGuard can explain what every card security option means before
                    the user changes it.
                </p>
            </div>
        </div>
    );
}

function SavingsPage() {
    return (
        <div className="mock-page-grid">
            <div id="saving-goals-card" className="mock-feature-card">
                <span className="eyebrow">Savings</span>
                <h2>Saving goals</h2>
                <p>Create goals, track progress and receive simple AI suggestions.</p>
                <button className="primary">Create goal</button>
            </div>

            <div id="vacation-fund-card" className="mock-feature-card">
                <h3>Vacation fund</h3>
                <strong>68%</strong>
                <p>2,040 RON saved from 3,000 RON target.</p>
            </div>

            <div id="saving-ai-suggestion-card" className="mock-feature-card wide">
                <h3>AI suggestion</h3>
                <p>
                    Move 150 RON this week to stay on track without affecting daily
                    spending.
                </p>
            </div>
        </div>
    );
}

function SupportPage() {
    return (
        <div className="mock-page-grid">
            <div id="help-center-card" className="mock-feature-card">
                <span className="eyebrow">Support</span>
                <h2>Help center</h2>
                <p>Ask the assistant or escalate to a human advisor when needed.</p>
                <button className="primary">Ask AssetGuard</button>
            </div>

            <div id="estimated-wait-card" className="mock-feature-card">
                <h3>Estimated wait</h3>
                <strong>2 min</strong>
                <p>AI resolves most common questions instantly.</p>
            </div>

            <div id="handoff-card" className="mock-feature-card wide">
                <h3>Human handoff</h3>
                <p>
                    When AI cannot solve the problem, it summarizes the context before
                    sending the user to human support.
                </p>
            </div>
        </div>
    );
}