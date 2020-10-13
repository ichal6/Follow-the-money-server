INSERT INTO "user_data"
VALUES (default, TO_DATE('2020-05-09','YYYY-MM-DD'), 'user@user.pl',1 , 'User Userowy', 'user1');

INSERT INTO "user_data"
VALUES (default, TO_DATE('2020-06-09','YYYY-MM-DD'), 'jan@kowalski@gmail.com', 1, 'Jan Kowalski', 'jankowalski1');

INSERT INTO "user_data"
VALUES (default, TO_DATE('2019-05-09','YYYY-MM-DD'), 'mike@shmidt.com', 1, 'Mike Shmidt', 'shmidt1');

INSERT INTO "authorities" ("name", "id")
VALUES ('ROLE_USER', 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 2);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 3);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (default, 'BANK', 'USD', 2000.0, 'Millenium', 0.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (default, 'CASH', 'USD', -1000.0, 'WALLET MY', 200.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (default, 'BANK', 'USD', 4000.0, 'PEKAO SA', 300.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (default, 'CASH', 'USD', 900.0, 'Savings in sock', 0.0, 1);

INSERT INTO "account" (id, account_type, currency, current_balance, name, starting_balance, user_id)
VALUES (default, 'BANK', 'USD', -50000.0, 'House morgage', 300.0, 1);

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (default, 'EXPENSE', 'Education', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (default, 'EXPENSE', 'Food', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (default, 'INCOME', 'Salary', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (default, 'INCOME', 'Bonuses', '1');

INSERT INTO "subcategory" (id, name, type, category_id)
VALUES (default, 'studies', 'EXPENSE', 1);

INSERT INTO "subcategory" (id, name, type, category_id)
VALUES (default, 'books', 'EXPENSE', 1);

INSERT INTO "subcategory" (id, name, type, category_id)
VALUES (default, 'regular salary', 'INCOME', 3);

INSERT INTO "subcategory" (id, name, type, category_id)
VALUES (default, 'extra hours', 'INCOME', 3);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (default, 'INCOME', 'Job', 1);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (default, 'EXPENSE', 'Bank', 1);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (default, 'EXPENSE', 'Biedronka', 1);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (default, 'EXPENSE', 'Mariusz-trans komis', 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-09-02','YYYY-MM-DD'), 'buy car', 'EXPENSE', -2500.00, 2, 1, 4);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-09-21','YYYY-MM-DD'), 'buy another car', 'EXPENSE', -2500.00, 2, 1, 4);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-09-10','YYYY-MM-DD'), 'buy milk', 'EXPENSE', -25.00, 2, 3, 2);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-08-10','YYYY-MM-DD'), 'buy bicycle', 'EXPENSE', -1200.00, 1, 4, 3);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-09-01','YYYY-MM-DD'), 'salary', 'INCOME', 1200.00, 3, 1, 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-02-01','YYYY-MM-DD'), 'salary', 'INCOME', 1200.00, 3, 1, 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-10-07','YYYY-MM-DD'), 'Morgage october', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-09-07','YYYY-MM-DD'), 'Morgage september', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-01-07','YYYY-MM-DD'), 'Morgage january', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-02-07','YYYY-MM-DD'), 'Morgage february', 'EXPENSE', -1000.00, 1, 2, 5);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (default, TO_DATE('2020-09-02','YYYY-MM-DD'), 'wplatomat wrzesien', 280.0, 2, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (default, TO_DATE('2020-10-02','YYYY-MM-DD'), 'wplatomat pazdziernik', 200.0, 2, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (default, TO_DATE('2020-09-12','YYYY-MM-DD'), 'wyplata z bankomatu', 200.0, 1, 2);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (default, TO_DATE('2020-07-12','YYYY-MM-DD'), 'wyplata z bankomatu', 400.0, 1, 2);
