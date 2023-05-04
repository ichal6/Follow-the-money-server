INSERT INTO "user_data"
VALUES (1, TO_DATE('2020-05-09','YYYY-MM-DD'), 'user@user.pl',1 , 'User Userowy', 'user1');

INSERT INTO "user_data"
VALUES (2, TO_DATE('2020-06-09','YYYY-MM-DD'), 'jan@kowalski@gmail.com', 1, 'Jan Kowalski', 'jankowalski1');

INSERT INTO "user_data"
VALUES (3, TO_DATE('2019-05-09','YYYY-MM-DD'), 'mike@shmidt.com', 1, 'Mike Shmidt', 'shmidt1');

INSERT INTO "authorities" ("name", "id")
VALUES ('ROLE_USER', 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 2);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 3);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (1, 'BANK', 'USD', 2000.0, 'Millenium', 0.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (2, 'CASH', 'USD', -1000.0, 'WALLET MY', 200.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (3, 'BANK', 'USD', 4000.0, 'PEKAO SA', 300.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (4, 'CASH', 'USD', 900.0, 'Savings in sock', 0.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (5, 'BANK', 'USD', -50000.0, 'House morgage', 300.0, 1);

INSERT INTO "category" (id, name, user_id)
VALUES (1, 'Credit', '1');

INSERT INTO "category" (id, name, user_id)
VALUES (2, 'Food', '1');

INSERT INTO "category" (id, name, user_id)
VALUES (3, 'Salary', '1');

INSERT INTO "category" (id, name, user_id)
VALUES (4, 'Bonuses', '1');

INSERT INTO "category" (id, name, user_id)
VALUES (5, 'Transport', '1');

INSERT INTO "category" (id, name, user_id, category_id)
VALUES (6, 'studies', 1, 1);

INSERT INTO "category" (id, name, user_id, category_id)
VALUES (7, 'books', 1, 1);

INSERT INTO "category" (id, name, user_id, category_id)
VALUES (8, 'regular salary', 1, 3);

INSERT INTO "category" (id, name, user_id, category_id)
VALUES (9, 'extra hours', 1, 3);

SELECT setval('category_id_seq', 9);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (1, 'INCOME', 'Job', 1);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (2, 'EXPENSE', 'Bank', 3);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (3, 'EXPENSE', 'Biedronka', 1);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (4, 'EXPENSE', 'Mariusz-trans komis', 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (1, TO_DATE('2022-05-02','YYYY-MM-DD'), 'buy car', 'EXPENSE', -2500.00, 5, 4, 4);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (2, TO_DATE('2022-04-21','YYYY-MM-DD'), 'buy another car', 'EXPENSE', -2500.00, 5, 4, 4);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (3, TO_DATE('2022-05-10','YYYY-MM-DD'), 'buy milk', 'EXPENSE', -25.00, 2, 3, 2);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (4, TO_DATE('2022-05-10','YYYY-MM-DD'), 'buy bicycle', 'EXPENSE', -1200.00, 5, 4, 3);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (5, TO_DATE('2022-05-01','YYYY-MM-DD'), 'salary', 'INCOME', 1200.00, 3, 1, 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (6, TO_DATE('2022-04-01','YYYY-MM-DD'), 'salary', 'INCOME', 1200.00, 3, 1, 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (7, TO_DATE('2022-04-07','YYYY-MM-DD'), 'Morgage october', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (8, TO_DATE('2022-09-07','YYYY-MM-DD'), 'Morgage september', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (9, TO_DATE('2023-01-07','YYYY-MM-DD'), 'Morgage january', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (10, TO_DATE('2023-02-07','YYYY-MM-DD'), 'Morgage february', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (1, TO_DATE('2022-09-02','YYYY-MM-DD'), 'wplatomat wrzesien', 280.0, 2, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (2, TO_DATE('2023-02-02','YYYY-MM-DD'), 'wplatomat pazdziernik', 200.0, 2, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (3, TO_DATE('2023-03-12','YYYY-MM-DD'), 'wyplata z bankomatu', 200.0, 1, 2);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (4, TO_DATE('2023-04-12','YYYY-MM-DD'), 'wyplata z bankomatu', 400.0, 1, 2);
