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

INSERT INTO "category" (id, name, user_id)
VALUES (10, 'Credit', '2');

INSERT INTO "category" (id, name, user_id)
VALUES (11, 'Taxi', '1');

SELECT setval('category_id_seq', 11);
