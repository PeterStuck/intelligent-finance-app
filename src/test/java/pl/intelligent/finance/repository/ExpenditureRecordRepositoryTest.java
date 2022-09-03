package pl.intelligent.finance.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts={"/sql/02_create_tables.sql", "/data/test_repository_data.sql"})
public class ExpenditureRecordRepositoryTest {

    private static String STORED_BANK_STATEMENT_ID = "062022";
    private static String NOT_EXISTING_BANK_STATEMENT_ID = "not_existing_bsid";

    private static Integer STORED_EXPENDITURE_CATEGORY_ID = 1;

    private static String NOT_EXISTING_RECORD_NAME = "not_existing_record";
    private static String STORED_RECORD_NAME = "record1";
    private static String STORED_RECORD_NAME2 = "record2";
    private static String STORED_RECORD_NAME3 = "record3";
    private static String STORED_RECORD_NAME4 = "record4";

    @Autowired
    private ExpenditureRecordRepository repository;

    @Test
    @DisplayName("Should return all stored records")
    public void testFindAll() {
        List<ExpenditureRecord> entities = repository.findAll();

        assertThat(entities.size()).isEqualTo(4);

        ExpenditureRecord record = entities.get(0);
        assertEquals(10.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());

        record = entities.get(1);
        assertEquals(8.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME2, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());

        record = entities.get(2);
        assertEquals(5.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME3, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());

        record = entities.get(3);
        assertEquals(11.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME4, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());
    }

    @Test
    @DisplayName("Should return an record when name and bank statement id matches record in database")
    public void findByNameAndBankStatementIdTest() {
        ExpenditureRecord record = repository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);
        assertEquals(STORED_RECORD_NAME, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(10.0, record.getAmount());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());
    }

    @Test
    @DisplayName("Should return null when no record was found with provided name and bank statement id")
    public void findByNameAndBankStatementIdNotFoundTest() {
        ExpenditureRecord record = repository.findByNameAndBankStatementId(NOT_EXISTING_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNull(record);
    }

    @Test
    @DisplayName("Should return list of records with provided bank statement id")
    public void findByBankStatementIdTest() {
        List<ExpenditureRecord> entities = repository.findByBankStatementId(STORED_BANK_STATEMENT_ID);

        assertThat(entities.size()).isEqualTo(4);

        ExpenditureRecord record = entities.get(0);
        assertEquals(10.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());

        record = entities.get(1);
        assertEquals(8.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME2, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());

        record = entities.get(2);
        assertEquals(5.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME3, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());

        record = entities.get(3);
        assertEquals(11.0, record.getAmount());
        assertEquals(STORED_RECORD_NAME4, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());
    }

    @Test
    @DisplayName("Should return empty list when no records was found with provided bank statement")
    public void findByBankStatementIdNotFoundTest() {
        List<ExpenditureRecord> entities = repository.findByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertThat(entities.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should persist object when there is no integration issues")
    public void createRecordTest() {
        ExpenditureRecord record = ExpenditureRecord.builder()
                .name("Some test")
                .amount(15.0)
                .categoryId(STORED_EXPENDITURE_CATEGORY_ID)
                .bankStatementId(STORED_BANK_STATEMENT_ID)
                .build();

        ExpenditureRecord persistedRecord = repository.save(record);
        assertNotNull(persistedRecord);

        record.setId(persistedRecord.getId());
        assertEquals(record, persistedRecord);

        List<ExpenditureRecord> allRecords = repository.findAll();
        assertEquals(5, allRecords.size());
    }

    // TODO create/update/delete tests

}
