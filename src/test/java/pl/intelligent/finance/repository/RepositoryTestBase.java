package pl.intelligent.finance.repository;

import org.springframework.test.context.jdbc.Sql;

@Sql(scripts={"/sql/02_create_tables.sql", "/data/test_repository_data.sql"})
public abstract class RepositoryTestBase {

    protected static String STORED_BANK_STATEMENT_ID = "062022";
    protected static String STORED_BANK_STATEMENT_ID2 = "072022";
    protected static String NOT_EXISTING_BANK_STATEMENT_ID = "not_existing_bsid";

    protected static Integer STORED_EXPENDITURE_CATEGORY_ID = 1;
    protected static Integer STORED_EXPENDITURE_CATEGORY_ID2 = 2;
    protected static Integer STORED_EXPENDITURE_CATEGORY_ID3 = 3;
    protected static String STORED_EXPENDITURE_CATEGORY = "category1";
    protected static String STORED_EXPENDITURE_CATEGORY2 = "category2";
    protected static String STORED_EXPENDITURE_CATEGORY3 = "category3";

    protected static String NOT_EXISTING_RECORD_NAME = "not_existing_record";
    protected static String STORED_RECORD_NAME = "record1";
    protected static String STORED_RECORD_NAME2 = "record2";
    protected static String STORED_RECORD_NAME3 = "record3";
    protected static String STORED_RECORD_NAME4 = "record4";
    

}
