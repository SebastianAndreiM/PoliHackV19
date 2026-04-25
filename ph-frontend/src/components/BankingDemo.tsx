import { useState } from "react";
import { mockAccounts, mockTransactions } from "../mocks/bankingMock";

type Page = "overview" | "payments" | "cards" | "savings" | "support";

export function BankingDemo() {
    const [page, setPage] = useState<Page>("overview");
    const mainAccount = mockAccounts[0];

    return (
        <section id="demo" className="banking-demo">
            <aside className="bank-sidebar">
                <div className="brand-mark">AG</div>

                <nav>
                    <button className={page === "overview" ? "active" : ""} onClick={() => setPage("overview")}>
                        Overview
                    </button>
                    <button id="nav-payments" className={page === "payments" ? "active" : ""} onClick={() => setPage("payments")}>
                        Payments
                    </button>
                    <button id="nav-cards" className={page === "cards" ? "active" : ""} onClick={() => setPage("cards")}>
                        Cards
                    </button>
                    <button id="nav-savings" className={page === "savings" ? "active" : ""} onClick={() => setPage("savings")}>
                        Savings
                    </button>
                    <button id="nav-support" className={page === "support" ? "active" : ""} onClick={() => setPage("support")}>
                        Support
                    </button>
                </nav>
            </aside>

            <main className="bank-main">
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
                            <div id="balance-card" className="balance-card">
                                <span>Total balance</span>
                                <strong>{mainAccount.balance.toLocaleString("en-US", { minimumFractionDigits: 2 })} {mainAccount.currency}</strong>
                                <small>{mainAccount.iban}</small>
                            </div>

                            <div id="transfer-card" className="transfer-card">
                                <span className="eyebrow">Quick transfer</span>
                                <label>Receiver IBAN<input id="iban-input" placeholder="RO49 BTRL..." /></label>
                                <label>Amount<input id="amount-input" placeholder="250.00" /></label>
                                <button id="confirm-transfer" className="primary">Confirm transfer</button>
                            </div>

                            <Transactions />
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
                            <div id="transfer-card" className="transfer-card">
                                <span className="eyebrow">Bank transfer</span>
                                <label>Receiver IBAN<input id="iban-input" placeholder="RO49 BTRL..." /></label>
                                <label>Amount<input id="amount-input" placeholder="250.00" /></label>
                                <label>Details<input placeholder="Optional message" /></label>
                                <button id="confirm-transfer" className="primary">Confirm transfer</button>
                            </div>

                            <div className="balance-card">
                                <span>From account</span>
                                <strong>Main account</strong>
                                <small>{mainAccount.iban}</small>
                            </div>
                        </div>
                    </>
                )}

                {page === "cards" && <SimplePage title="Cards" text="Manage card limits, freeze cards and view security settings." />}
                {page === "savings" && <SimplePage title="Savings" text="Create saving goals and track progress." />}
                {page === "support" && <SimplePage title="Support" text="Get help from the AI guide or contact a human advisor." />}
            </main>
        </section>
    );
}

function Transactions() {
    return (
        <div className="transactions-card">
            <div className="card-title-row">
                <h3>Recent activity</h3>
                <span>Mock data</span>
            </div>

            {mockTransactions.map((transaction) => (
                <div className="transaction-row" key={transaction.id}>
                    <div>
                        <strong>{transaction.title}</strong>
                        <span>{transaction.category} · {transaction.date}</span>
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