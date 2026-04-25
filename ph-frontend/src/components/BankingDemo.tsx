import { useState } from "react";
import { mockAccounts, mockTransactions } from "../mocks/bankingMock";
import type { AdaptiveLayout } from "../types/ui";

type Page = "overview" | "payments" | "cards" | "savings" | "support";

type Props = {
    layout: AdaptiveLayout | null;
};

export function BankingDemo({ layout }: Props) {
    const [page, setPage] = useState<Page>("overview");
    const mainAccount = mockAccounts[0];

    const components = [...(layout?.components ?? [])].sort(
        (a, b) => a.order - b.order
    );

    function isVisible(key: string) {
        return components.find((component) => component.key === key)?.visible ?? true;
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
        { label: "Overview", page: "overview" as Page, visible: true },
        {
            label: "Payments",
            page: "payments" as Page,
            visible: isVisible("quick_actions"),
        },
        { label: "Cards", page: "cards" as Page, visible: true },
        {
            label: "Savings",
            page: "savings" as Page,
            visible: isVisible("fx_rates"),
        },
        {
            label: "Support",
            page: "support" as Page,
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
                            id={item.page === "payments" ? "nav-payments" : undefined}
                            className={page === item.page ? "active" : ""}
                            onClick={() => setPage(item.page)}
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

                {page === "cards" && (
                    <SimplePage
                        title="Cards"
                        text="Manage card limits, freeze cards and view security settings."
                    />
                )}

                {page === "savings" && (
                    <SimplePage
                        title="Savings"
                        text="Create saving goals and track progress."
                    />
                )}

                {page === "support" && (
                    <SimplePage
                        title="Support"
                        text="Get help from the AI guide or contact a human advisor."
                    />
                )}
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
                    <p className="field-hint">
                        Double check the IBAN carefully. One wrong character can block the
                        transfer or send money to the wrong account.
                    </p>
                )}
            </label>

            <label>
                Amount
                <input id="amount-input" placeholder="250.00" />

                {explanationLevel === "detailed" && (
                    <p className="field-hint">
                        Check the amount and currency before continuing.
                    </p>
                )}
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
            <div className="card-title-row">
                <h3>Recent activity</h3>
                <span>Mock data</span>
            </div>

            {mockTransactions.slice(0, limit).map((transaction) => (
                <div className="transaction-row" key={transaction.id}>
                    <div>
                        <strong>{transaction.title}</strong>
                        <span>
              {transaction.category} · {transaction.date}
            </span>
                    </div>

                    <b className={transaction.amount > 0 ? "positive" : ""}>
                        {transaction.amount > 0 ? "+" : ""}
                        {transaction.amount.toFixed(2)} RON
                    </b>
                </div>
            ))}
        </div>
    );
}

function SimplePage({ title, text }: { title: string; text: string }) {
    return (
        <div className="empty-bank-page">
            <span className="eyebrow">Mock section</span>
            <h2>{title}</h2>
            <p>{text}</p>
        </div>
    );
}