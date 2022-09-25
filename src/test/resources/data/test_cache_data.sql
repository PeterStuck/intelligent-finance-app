INSERT INTO EXPENDITURE_CATEGORY(ID, NAME, PARENT_CATEGORY_ID) VALUES
(1, 'category1', NULL),
(2, 'category2', NULL),
-- category3 should have no matcher associations in this file
(3, 'category3', NULL);

INSERT INTO EXPENDITURE_CATEGORY_MATCHER(ID, PATTERN, MATCHER_TYPE_ID, EXPENDITURE_CATEGORY_ID) VALUES
(1, '^test$', 'REGEX', 1),
(2, '^test[0-9]*', 'REGEX', 1),
(3, '^[A-Za-z]{1,}', 'REGEX', 1);

INSERT INTO BANK_STATEMENT(ID, NAME) VALUES
(1, '062022'),
-- 072022 is used dynamically in tests, do not associate here
(2, '072022');

INSERT INTO EXPENDITURE_RECORD (ID, NAME, AMOUNT, EXPENDITURE_CATEGORY_ID, BANK_STATEMENT_ID) VALUES
(1, 'record1', 10.0, 1, 1),
(2, 'record2', 8.0, 1, 1),
(3, 'record3', 5.0, 1, 1),
(4, 'record4', 11.0, 1, 1);