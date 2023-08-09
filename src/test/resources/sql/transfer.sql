INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (1, TO_DATE('2022-09-02','YYYY-MM-DD'), 'wplatomat wrzesien', 280.0, 2, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (2, TO_DATE('2023-02-02','YYYY-MM-DD'), 'wplatomat pazdziernik', 200.0, 2, 1);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (3, TO_DATE('2023-03-12','YYYY-MM-DD'), 'wyplata z bankomatu', 200.0, 1, 2);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (4, TO_DATE('2023-04-12','YYYY-MM-DD'), 'wyplata z bankomatu', 400.0, 1, 2);

INSERT INTO "transfer" (id, date, title, value, account_from_id, account_to_id)
VALUES (5, TO_DATE('2023-08-07','YYYY-MM-DD'), 'Przesył środków', 1000.0, 7, 6);
