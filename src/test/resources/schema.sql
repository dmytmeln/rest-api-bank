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
    balance DOUBLE PRECISION NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS user_bank_account (
    users BIGINT NOT NULL,
    bank_accounts BIGINT NOT NULL,

    FOREIGN KEY (users) REFERENCES users(id),
    FOREIGN KEY (bank_accounts) REFERENCES bank_accounts(id),

    PRIMARY KEY (users, bank_accounts)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    bank_account_id BIGINT NOT NULL,
    msg VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    money_amount DOUBLE PRECISION NOT NULL,
    transaction_date timestamp NOT NULL,

    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);
