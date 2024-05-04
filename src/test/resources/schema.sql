DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS bank_accounts;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    password VARCHAR(125) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(50) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS bank_accounts (
    id BIGSERIAL PRIMARY KEY,
    users BIGINT NOT NULL,
    users_key BIGINT NOT NULL,
    balance DOUBLE PRECISION NOT NULL DEFAULT 0,

    FOREIGN KEY (users) REFERENCES users(id)
);

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

ALTER TABLE transactions DROP CONSTRAINT transactions_bank_accounts_fkey;