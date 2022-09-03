package pl.intelligent.finance.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;
import pl.intelligent.finance.repository.ExpenditureRecordRepository;
import pl.intelligent.finance.service.IExpenditureRecordService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpenditureRecordServiceDbTest {

    private static String STORED_BANK_STATEMENT_ID = "062022";
    private static String STORED_BANK_STATEMENT_ID2 = "072022";
    private static String NOT_EXISTING_BANK_STATEMENT_ID = "not_existing_bsid";

    private static Integer STORED_EXPENDITURE_CATEGORY_ID = 1;

    private static String NOT_EXISTING_RECORD_NAME = "not_existing_record";
    private static String STORED_RECORD_NAME = "record1";
    private static String STORED_RECORD_NAME2 = "record2";
    private static String STORED_RECORD_NAME3 = "record3";
    private static String STORED_RECORD_NAME4 = "record4";

    @MockBean
    private ExpenditureRecordRepository repository;

    @Autowired
    @Qualifier("expenditureRecordServiceDb")
    private IExpenditureRecordService service;

    @Test
    @DisplayName("Should return all records associated with provided bank statement id")
    public void findByBankStatementIdTest() {
        List<ExpenditureRecord> expectedRecords = createExpectedRecords();

        List<ExpenditureRecord> expectedReturnList = expectedRecords.stream()
                .filter(rec -> rec.getBankStatementId().equals(STORED_BANK_STATEMENT_ID))
                .collect(Collectors.toList());

        when(repository.findByBankStatementId(STORED_BANK_STATEMENT_ID)).thenReturn(expectedReturnList);

        List<IExpenditureRecord> records = service.findByBankStatementId(STORED_BANK_STATEMENT_ID);

        assertEquals(3, records.size());
        assertEquals(expectedReturnList, records);

        verify(repository).findByBankStatementId(STORED_BANK_STATEMENT_ID);
    }

    @Test
    @DisplayName("Should return single record with provided bank statement id and name if exits")
    public void findByNameAndBankStatementIdTest() {
        ExpenditureRecord expectedEntity = createExpectedEntity(1L, STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 10.0);
        when(repository.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID)).thenReturn(expectedEntity);

        IExpenditureRecord entity = service.findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedEntity, entity);

        verify(repository).findByNameAndBankStatementId(STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID);
    }

    @Test
    @DisplayName("Should persist record when there are no integration issues")
    public void createTest() {
        ExpenditureRecord expectedEntity = createExpectedEntity(1L, STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 10.0);
        when(repository.save(any())).thenReturn(expectedEntity);

        IExpenditureRecord persistedEntity = service.create(expectedEntity);
        assertNotNull(persistedEntity);
        assertEquals(expectedEntity, persistedEntity);

        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should delete entity from persistence store if found")
    public void deleteTest() {
        List<ExpenditureRecord> allRecords = createExpectedRecords();
        when(repository.findByBankStatementId(STORED_BANK_STATEMENT_ID)).thenReturn(allRecords);
        doAnswer(invocation -> {
            deleteFromList(allRecords, allRecords.get(0));
            return null;
        }).when(repository).delete(any());

        service.delete(allRecords.get(0));

        List<IExpenditureRecord> result = service.findByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(3, result.size());

        verify(repository).delete(any());
    }

    private boolean deleteFromList(List<ExpenditureRecord> records, ExpenditureRecord entityToDelete) {
        return deleteFromList(records, entityToDelete.getId());
    }

    private boolean deleteFromList(List<ExpenditureRecord> records, Long id) {
        ExpenditureRecord record = records.stream()
                .filter(rec -> rec.getId().equals(id))
                .findFirst().orElse(null);
        if (record == null) {
            throw new EntityNotFoundException("Entity with id: " + id + " not found");
        }

        records.remove(record);

        return true;
    }

    private List<ExpenditureRecord> createExpectedRecords() {
        AtomicLong entityCounter = new AtomicLong(1);
        List<ExpenditureRecord> records = new ArrayList<>();

        records.add(createExpectedEntity(entityCounter.incrementAndGet(), STORED_RECORD_NAME, STORED_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 10.0));
        records.add(createExpectedEntity(entityCounter.incrementAndGet(), STORED_RECORD_NAME2, STORED_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 8.0));
        records.add(createExpectedEntity(entityCounter.incrementAndGet(), STORED_RECORD_NAME3, STORED_BANK_STATEMENT_ID2, STORED_EXPENDITURE_CATEGORY_ID, 1.0));
        records.add(createExpectedEntity(entityCounter.incrementAndGet(), STORED_RECORD_NAME4, STORED_BANK_STATEMENT_ID, STORED_EXPENDITURE_CATEGORY_ID, 1.0));

        return records;
    }

    private ExpenditureRecord createExpectedEntity(Long id, String name, String bankStatementId,
                                                   int categoryId, double amount) {
        ExpenditureRecord entity = (ExpenditureRecord) service.createInstance();
        entity.setId(id);
        entity.setName(name);
        entity.setBankStatementId(bankStatementId);
        entity.setCategoryId(categoryId);
        entity.setAmount(amount);

        return entity;
    }


}
