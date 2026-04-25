export type Account = {
    id: string;
    name: string;
    iban: string;
    balance: number;
    currency: string;
};

export type Transaction = {
    id: string;
    title: string;
    category: string;
    amount: number;
    date: string;
};