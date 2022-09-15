INSERT INTO EXPENDITURE_CATEGORY(ID, NAME, PARENT_CATEGORY_ID) VALUES
(1, 'category1', NULL),
(2, 'category2', NULL),
(3, 'category3', NULL);

INSERT INTO BANK_STATEMENT(ID) VALUES
('062022'),
-- 072022 is used dynamically in tests, do not associate here
('072022');

INSERT INTO EXPENDITURE_RECORD (ID, NAME, AMOUNT, EXPENDITURE_CATEGORY_ID, BANK_STATEMENT_ID) VALUES
(1, 'record1', 10.0, 1, '062022'),
(2, 'record2', 8.0, 1, '062022'),
(3, 'record3', 5.0, 1, '062022'),
(4, 'record4', 11.0, 1, '062022');