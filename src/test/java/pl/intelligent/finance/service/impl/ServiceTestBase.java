package pl.intelligent.finance.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.service.IBankStatementService;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;
import pl.intelligent.finance.service.IExpenditureRecordService;

@Sql(scripts={"/sql/02_create_tables.sql", "/data/test_repository_data.sql"})
@Import(ServiceTestBase.TestConfig.class)
public class ServiceTestBase {

    protected static String STORED_BANK_STATEMENT = "062022";
    protected static String STORED_BANK_STATEMENT2 = "072022";
    protected static Integer STORED_BANK_STATEMENT_ID = 1;
    protected static Integer STORED_BANK_STATEMENT_ID2 = 2;

    protected static String NOT_EXISTING_BANK_STATEMENT = "not_existing_bsid";
    protected static Integer NOT_EXISTING_BANK_STATEMENT_ID = 99;

    protected static Integer STORED_EXPENDITURE_CATEGORY_ID = 1;
    protected static Integer STORED_EXPENDITURE_CATEGORY_ID2 = 2;
    protected static Integer STORED_EXPENDITURE_CATEGORY_ID3 = 3;
    protected static String STORED_EXPENDITURE_CATEGORY = "category1";
    protected static String STORED_EXPENDITURE_CATEGORY2 = "category2";
    protected static String STORED_EXPENDITURE_CATEGORY3 = "category3";
    protected static String NOT_EXISTING_EXPENDITURE_CATEGORY = "not_exist_cat";

    protected static String NOT_EXISTING_RECORD_NAME = "not_existing_record";
    protected static String STORED_RECORD_NAME = "record1";
    protected static String STORED_RECORD_NAME2 = "record2";
    protected static String STORED_RECORD_NAME3 = "record3";
    protected static String STORED_RECORD_NAME4 = "record4";

    public static class TestConfig {
        public TestConfig() {
        }

        @Bean
        public IExpenditureRecordService expenditureRecordServiceDb() {
            return new ExpenditureRecordServiceDb();
        }

        @Bean
        public IExpenditureCategoryService expenditureCategoryServiceDb() {
            return new ExpenditureCategoryServiceDb();
        }

        @Bean
        public IExpenditureCategoryMatcherService expenditureCategoryMatcherServiceDb() {
            return new ExpenditureCategoryMatcherServiceDb();
        }

        @Bean
        public IBankStatementService bankStatementServiceDb() {
            return new BankStatementServiceDb();
        }

    }
    
}
