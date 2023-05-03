INSERT INTO "user_data"
VALUES (1, TO_DATE('2020-05-09','YYYY-MM-DD'), 'user@user.pl', 1, 'User Userowy', 'user1');

INSERT INTO "user_data"
VALUES (2, TO_DATE('2020-06-09','YYYY-MM-DD'), 'jan.kowalski@gmail.com', 1, 'Jan Kowalski', 'jankowalski1');

INSERT INTO "authorities" ("name", "id")
VALUES ('ROLE_USER', 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 1);

INSERT INTO "user_authority"("authority_id", "user_id")
VALUES (1, 2);
