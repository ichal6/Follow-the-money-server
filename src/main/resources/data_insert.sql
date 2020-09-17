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

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (default, 'EXPENSE', 'Education', '1');

INSERT INTO "subcategory" (id, name, type, category_id)
VALUES (default, 'studies', 'EXPENSE', 1);

INSERT INTO "payee" (id, general_type, name, user_id)
VALUES (default, 'INCOME', 'Job', 1);

INSERT INTO "transaction" (id, date, title, type, value, category_id, payee_id, account_id)
VALUES (default, TO_DATE('2020-09-02','YYYY-MM-DD'), 'buy car', 'EXPENSE', 2500.00, 1, 1, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (default, TO_DATE('2019-09-02','YYYY-MM-DD'), 'from friend', 280.0, 2, 1);
