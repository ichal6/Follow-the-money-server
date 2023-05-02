INSERT INTO "user_data"
VALUES (1, TO_DATE('2020-05-09','YYYY-MM-DD'), 'user@user.pl',1 , 'User Userowy', 'user1');

INSERT INTO "authorities" ("name", "id")
VALUES ('ROLE_USER', 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 1);

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (1, 'EXPENSE', 'Credit', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (2, 'EXPENSE', 'Food', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (3, 'INCOME', 'Salary', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (4, 'INCOME', 'Bonuses', '1');

INSERT INTO "category" (id, general_type, name, user_id)
VALUES (5, 'EXPENSE', 'Transport', '1');

INSERT INTO "category" (id, name, general_type, user_id, category_id)
VALUES (6, 'studies', 'EXPENSE', 1, 1);

INSERT INTO "category" (id, name, general_type, user_id, category_id)
VALUES (7, 'books', 'EXPENSE', 1, 1);

INSERT INTO "category" (id, name, general_type, user_id, category_id)
VALUES (8, 'regular salary', 'INCOME', 1, 3);

INSERT INTO "category" (id, name, general_type, user_id, category_id)
VALUES (9, 'extra hours', 'INCOME',1, 3);

SELECT setval('category_id_seq', 9);
