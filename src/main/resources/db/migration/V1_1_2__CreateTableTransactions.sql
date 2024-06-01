CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    bank_accounts BIGINT NOT NULL,
    bank_accounts_key BIGINT NOT NULL,
    msg VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    money_amount DOUBLE PRECISION NOT NULL,
    transaction_date timestamp NOT NULL,

    FOREIGN KEY (bank_accounts) REFERENCES bank_accounts(id)
);