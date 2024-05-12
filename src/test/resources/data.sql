INSERT INTO users(firstname, lastname, password, email, phone_number, role)
VALUES ('Dmytro', 'Melnyk', 'asAS12!@', 'dimamel28@gmail.com', '380984035791', 'ROLE_USER');

INSERT INTO bank_accounts(balance, users, users_key)
VALUES (1000, 1, 0);

INSERT INTO transactions(bank_accounts, msg, type, money_amount, transaction_date, bank_accounts_key)
VALUES (1, 'msg', 'transaction_type', 1000, '2023-01-25 23:10:10', 0);

INSERT INTO transactions(bank_accounts, msg, type, money_amount, transaction_date, bank_accounts_key)
VALUES (1, 'msg', 'transaction_type', 2000, '2023-02-25 20:10:10', 1);
