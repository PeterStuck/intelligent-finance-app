package pl.intelligent.finance.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;
import pl.intelligent.finance.service.IExpenditureRecordService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ExpenditureRecordServiceDbTest extends ServiceTestBase {

    @Autowired
    @Qualifier("expenditureRecordServiceDb")
    private IExpenditureRecordService service;

    @Test
    @DisplayName("Should return all records associated with provided bank statement id")
    public void findByBankStatementIdTest() {
        List<IExpenditureRecord> entities = service.findByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, entities.size());

        IExpenditureRecord record = entities.get(0);
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
    @DisplayName("Should return single record with provided bank statement id and name if exits")
    public void findByNameAndBankStatementIdTest() {
        IExpenditureRecord record = service.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);
        assertEquals(STORED_RECORD_NAME, record.getName());
        assertEquals(STORED_BANK_STATEMENT_ID, record.getBankStatementId());
        assertEquals(10.0, record.getAmount());
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID, record.getCategoryId());
    }

    @Test
    @DisplayName("Should persist record when there are no integration issues")
    public void createTest() throws Exception {
        String newRecordName = "newRecord";
        IExpenditureRecord recordToCreate = createEntity(newRecordName, STORED_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 10.0);

        IExpenditureRecord persistedRecord = service.create(recordToCreate);
        assertNotNull(persistedRecord);
        recordToCreate.setId(persistedRecord.getId());
        assertEquals(recordToCreate, persistedRecord);

        IExpenditureRecord storedRecord = service.findByNameAndBankStatementId(newRecordName, STORED_BANK_STATEMENT_ID);
        assertEquals(storedRecord, persistedRecord);
    }

    @Test
    @DisplayName("Should throw exception if there are some integration issues")
    public void createIntegrationIssueTest() {
        IExpenditureRecord recordToCreate = createEntity(STORED_RECORD_NAME, NOT_EXISTING_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 10.0);

        assertThrows(DataIntegrityViolationException.class,  () -> service.create(recordToCreate));
    }

    @Test
    @DisplayName("Should delete entity from persistence store if found")
    public void deleteTest() {
        IExpenditureRecord record = service.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);

        service.delete(record);

        record = service.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNull(record);
    }

    @Test
    @DisplayName("Should delete entity by instance id from persistence store if found")
    public void deleteByIdTest() {
        IExpenditureRecord record = service.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNotNull(record);

        service.deleteById(record.getId());

        record = service.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertNull(record);
    }

    @Test
    @DisplayName("Should throw exception when during deletion entity with provided ID was not found")
    public void deleteByIdNotFoundTest() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.deleteById(99L));
    }

    private IExpenditureRecord createEntity(String name, String bankStatementId,
                                           int categoryId, double amount) {
        ExpenditureRecord entity = (ExpenditureRecord) service.createInstance();
        entity.setName(name);
        entity.setBankStatementId(bankStatementId);
        entity.setCategoryId(categoryId);
        entity.setAmount(amount);

        return entity;
    }


}
