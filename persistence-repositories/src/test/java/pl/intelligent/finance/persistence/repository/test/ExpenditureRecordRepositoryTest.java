package pl.intelligent.finance.persistence.repository.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.persistence.impl.BankStatement;
import pl.intelligent.finance.persistence.impl.ExpenditureRecord;
import pl.intelligent.finance.persistence.repository.BankStatementRepository;
import pl.intelligent.finance.persistence.repository.ExpenditureRecordRepository;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ExpenditureRecordRepositoryTest extends RepositoryTestBase {

    @Autowired
    private ExpenditureRecordRepository expenditureRecordRepository;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Test
    @DisplayName("Should return all stored records")
    public void findAllTest() {
        List<ExpenditureRecord> entities = expenditureRecordRepository.findAll();

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
        ExpenditureRecord record = expenditureRecordRepository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);
        assertEquals(STORED_RECORD_NAME, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(10.0, record.getAmount());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());
    }

    @Test
    @DisplayName("Should return null when no record was found with provided name and bank statement id")
    public void findByNameAndBankStatementIdNotFoundTest() {
        ExpenditureRecord record = expenditureRecordRepository.findByNameAndBankStatementId(NOT_EXISTING_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNull(record);
    }

    @Test
    @DisplayName("Should return list of records with provided bank statement id")
    public void findByBankStatementIdTest() {
        List<ExpenditureRecord> entities = expenditureRecordRepository.findByBankStatementId(STORED_BANK_STATEMENT_ID);

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
        List<ExpenditureRecord> entities = expenditureRecordRepository.findByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
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

        ExpenditureRecord persistedRecord = expenditureRecordRepository.save(record);
        assertNotNull(persistedRecord);

        record.setId(persistedRecord.getId());
        assertEquals(record, persistedRecord);

        List<ExpenditureRecord> allRecords = expenditureRecordRepository.findAll();
        assertEquals(5, allRecords.size());
    }

    @Test
    @DisplayName("Should throw exception when there are integration issues during object persistence")
    public void createRecordIntegrationIssuesTest() throws SQLException {
        List<ExpenditureRecord> allRecords = expenditureRecordRepository.findAll();
        assertEquals(4, allRecords.size());

        BankStatement bankStatement = bankStatementRepository.findById(NOT_EXISTING_BANK_STATEMENT_ID).orElse(null);
        assertNull(bankStatement);

        String createRecordName = "Some test";
        ExpenditureRecord record = ExpenditureRecord.builder()
                .name(createRecordName)
                .amount(15.0)
                .categoryId(STORED_EXPENDITURE_CATEGORY_ID)
                .bankStatementId(NOT_EXISTING_BANK_STATEMENT_ID)
                .build();

        assertThrows(DataIntegrityViolationException.class,
                () -> expenditureRecordRepository.saveAndFlush(record));

        // check bank statement wasn't cascade persisted
        bankStatement = bankStatementRepository.findById(NOT_EXISTING_BANK_STATEMENT_ID).orElse(null);
        assertNull(bankStatement);
    }

    @Test
    @DisplayName("Should update record if found")
    public void updateRecordTest() {
        String newName = "New record name";

        ExpenditureRecord recordUpdate = ExpenditureRecord.builder()
                .name(newName)
                .amount(15.0)
                .categoryId(STORED_EXPENDITURE_CATEGORY_ID)
                .bankStatementId(STORED_BANK_STATEMENT_ID2)
                .build();

        ExpenditureRecord recordBeforeUpdate = expenditureRecordRepository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(recordBeforeUpdate);

        recordUpdate.setId(recordBeforeUpdate.getId());
        ExpenditureRecord updatedRecord = expenditureRecordRepository.save(recordUpdate);

        assertEquals(updatedRecord, recordUpdate);
    }

    @Test
    @DisplayName("Should delete record if found")
    public void deleteRecordTest() {
        ExpenditureRecord record = expenditureRecordRepository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);

        expenditureRecordRepository.delete(record);

        record = expenditureRecordRepository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNull(record);
    }

    @Test
    @DisplayName("Should delete record by instance id if found")
    public void deleteRecordByIdTest() {
        ExpenditureRecord record = expenditureRecordRepository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);

        expenditureRecordRepository.deleteById(record.getId());

        record = expenditureRecordRepository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNull(record);
    }

    @Test
    @DisplayName("Should return all stored record ids")
    public void findAllIdsTest() {
        List<Long> ids = expenditureRecordRepository.findAllIds();
        assertThat(ids.size()).isEqualTo(4);

        assertTrue(ids.contains(1L));
        assertTrue(ids.contains(2L));
        assertTrue(ids.contains(3L));
        assertTrue(ids.contains(4L));
    }

    @Test
    @DisplayName("Should return empty list when no records are stored")
    @Sql(scripts = "/sql/02_create_tables.sql") // recreate schema without populating
    public void findAllIdsEmptyListTest() {
        List<Long> ids = expenditureRecordRepository.findAllIds();
        assertThat(ids.size()).isEqualTo(0);
    }

}
