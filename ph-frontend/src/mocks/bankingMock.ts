import type { Account, Transaction } from "../types/banking";

export const mockAccounts: Account[] = [
    {
        id: "acc-main",
        name: "Main account",
        iban: "RO49 BTRL 0000 0000 1234 5678",
        balance: 12450.75,
        currency: "RON",
    },
    {
        id: "acc-savings",
        name: "Savings pocket",
        iban: "RO12 BTRL 0000 0000 9876 5432",
        balance: 3820.0,
        currency: "RON",
    },
];

export const mockTransactions: Transaction[] = [
    {
        id: "tr-1",
        title: "Mega Image",
        category: "Groceries",
        amount: -84.25,
        date: "Today",
    },
    {
        id: "tr-2",
        title: "Salary",
        category: "Income",
        amount: 4300,
        date: "Yesterday",
    },
    {
        id: "tr-3",
        title: "Netflix",
        category: "Subscription",
        amount: -49.99,
        date: "Apr 21",
    },
];