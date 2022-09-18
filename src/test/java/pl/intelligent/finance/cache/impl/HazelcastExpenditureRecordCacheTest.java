package pl.intelligent.finance.cache.impl;

import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class HazelcastExpenditureRecordCacheTest extends HazelcastCacheBaseTest {

    private HazelcastExpenditureRecordCache hzExpenditureRecordCache;

    @BeforeEach
    public void setUp() {
        super.setUp();

        hzExpenditureRecordCache = new HazelcastExpenditureRecordCache(hazelcastInstance, serviceProvider);

        IMap<Object, Object> map = hazelcastInstance.getMap(HazelcastExpenditureRecordCache.CACHE_NAME);
        map.evictAll();
        map.loadAll(true);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void getAllByBankStatementIdTest() {
        List<StorableExpenditureRecord> records = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, records.size());

        StorableExpenditureRecord storableExpenditureRecord = getByName(records, STORED_RECORD_NAME);
        assertNotNull(storableExpenditureRecord);
        StorableExpenditureRecord expectedRecord = createRecord(1L, STORED_RECORD_NAME, 10.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        storableExpenditureRecord = getByName(records, STORED_RECORD_NAME2);
        assertNotNull(storableExpenditureRecord);
        expectedRecord = createRecord(2L, STORED_RECORD_NAME2, 8.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        storableExpenditureRecord = getByName(records, STORED_RECORD_NAME3);
        assertNotNull(storableExpenditureRecord);
        expectedRecord = createRecord(3L, STORED_RECORD_NAME3, 5.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        storableExpenditureRecord = getByName(records, STORED_RECORD_NAME4);
        assertNotNull(storableExpenditureRecord);
        expectedRecord = createRecord(4L, STORED_RECORD_NAME4, 11.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void getAllByBankStatementIdEmptyResultTest() {
        List<StorableExpenditureRecord> records = hzExpenditureRecordCache.getAllByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(0, records.size());

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(0, recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addTest() throws InvalidDataException {
        final String recordName = "newRecord";

        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);

        StorableExpenditureRecord createdRecord = hzExpenditureRecordCache.add(recordToCreate);
        assertNotNull(createdRecord);
        recordToCreate.setId(createdRecord.getId());
        assertEquals(recordToCreate, createdRecord);

        StorableExpenditureRecord storedRecord = hzExpenditureRecordCache.get(createdRecord.getId());
        assertNotNull(storedRecord);
        assertEquals(createdRecord, storedRecord);

        IExpenditureRecord recordDb = serviceProvider.getExpenditureRecordService().findById(storedRecord.getId());
        assertNotNull(recordDb);
        compareCacheAndDbEntity(storedRecord, recordDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addRecordWithExistingNameAndBankStatementIdTest() throws InvalidDataException {
        List<StorableExpenditureRecord> recordsBefore = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, recordsBefore.size());


        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(STORED_RECORD_NAME, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);

        assertExceptionOccurred(
                () -> hzExpenditureRecordCache.add(recordToCreate),
                ExceptionUtil.dataIntegrityError()
        );

        List<StorableExpenditureRecord> recordAfter = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(recordAfter.size(), recordsBefore.size());

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(recordsBefore.size(), recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addRecordBankStatementIdNotExistsTest() throws InvalidDataException {
        List<StorableExpenditureRecord> recordsBefore = hzExpenditureRecordCache.getAllByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(0, recordsBefore.size());


        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(STORED_RECORD_NAME, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, NOT_EXISTING_BANK_STATEMENT_ID);

        assertExceptionOccurred(
                () -> hzExpenditureRecordCache.add(recordToCreate),
                ExceptionUtil.dataIntegrityError()
        );

        List<StorableExpenditureRecord> recordAfter = hzExpenditureRecordCache.getAllByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(recordAfter.size(), recordsBefore.size());

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(recordsBefore.size(), recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addRecordExpenditureCategoryNotExistsTest() throws InvalidDataException {
        final String recordName = "newRecord";

        List<StorableExpenditureRecord> recordsBefore = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, recordsBefore.size());

        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                NOT_EXISTING_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);

        assertExceptionOccurred(
                () -> hzExpenditureRecordCache.add(recordToCreate),
                ExceptionUtil.dataIntegrityError()
        );

        List<StorableExpenditureRecord> recordAfter = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(recordAfter.size(), recordsBefore.size());

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(recordsBefore.size(), recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void batchAddTest() throws InvalidDataException {
        final String recordName = "newRecord";
        final String recordName2 = "newRecord2";
        final String recordName3 = "newRecord3";

        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate2 = (HazelcastExpenditureRecord) createRecord(recordName2, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate3 = (HazelcastExpenditureRecord) createRecord(recordName3, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);

        List<StorableExpenditureRecord> recordsToCreate = Arrays.asList(recordToCreate, recordToCreate2, recordToCreate3);

        // add records via hazelcast
        List<StorableExpenditureRecord> createdRecords = hzExpenditureRecordCache.batchAdd(recordsToCreate);
        assertNotNull(createdRecords);
        assertEquals(recordsToCreate.size(), createdRecords.size());

        HazelcastExpenditureRecord createdRecord = (HazelcastExpenditureRecord) getByName(createdRecords, recordName);
        recordToCreate.setId(createdRecord.getId());
        assertEquals(recordToCreate, createdRecord);

        HazelcastExpenditureRecord createdRecord2 = (HazelcastExpenditureRecord) getByName(createdRecords, recordName2);
        recordToCreate2.setId(createdRecord2.getId());
        assertEquals(recordToCreate2, createdRecord2);

        HazelcastExpenditureRecord createdRecord3 = (HazelcastExpenditureRecord) getByName(createdRecords, recordName3);
        recordToCreate3.setId(createdRecord3.getId());
        assertEquals(recordToCreate3, createdRecord3);

        // check hazelcast cache
        StorableExpenditureRecord storedRecord = hzExpenditureRecordCache.get(createdRecord.getId());
        assertNotNull(storedRecord);
        assertEquals(createdRecord, storedRecord);

        StorableExpenditureRecord storedRecord2 = hzExpenditureRecordCache.get(createdRecord2.getId());
        assertNotNull(storedRecord2);
        assertEquals(createdRecord2, storedRecord2);

        StorableExpenditureRecord storedRecord3 = hzExpenditureRecordCache.get(createdRecord3.getId());
        assertNotNull(storedRecord3);
        assertEquals(createdRecord3, storedRecord3);

        // check db state
        IExpenditureRecord recordDb = serviceProvider.getExpenditureRecordService().findById(storedRecord.getId());
        assertNotNull(recordDb);
        compareCacheAndDbEntity(storedRecord, recordDb);

        recordDb = serviceProvider.getExpenditureRecordService().findById(storedRecord2.getId());
        assertNotNull(recordDb);
        compareCacheAndDbEntity(storedRecord2, recordDb);

        recordDb = serviceProvider.getExpenditureRecordService().findById(storedRecord3.getId());
        assertNotNull(recordDb);
        compareCacheAndDbEntity(storedRecord3, recordDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void batchAddOneOfRecordsWithExistingNameTest() throws InvalidDataException {
        final String recordName = "newRecord";
        final String recordName2 = "newRecord2";
        final String recordName3 = "newRecord3";

        List<StorableExpenditureRecord> recordsBefore = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(0, recordsBefore.size());

        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate2 = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate3 = (HazelcastExpenditureRecord) createRecord(recordName3, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);

        List<StorableExpenditureRecord> recordsToCreate = Arrays.asList(recordToCreate, recordToCreate2, recordToCreate3);

        // add records via hazelcast
        assertExceptionOccurred(
                () -> hzExpenditureRecordCache.batchAdd(recordsToCreate),
                ExceptionUtil.dataIntegrityError()
        );

        // check hazelcast cache
        List<StorableExpenditureRecord> recordsAfter = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(recordsAfter.size(), recordsBefore.size());

        // check db state
        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(recordsBefore.size(), recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void batchAddOneOfRecordsWithNotExistingBankStatementIdTest() throws InvalidDataException {
        final String recordName = "newRecord";
        final String recordName2 = "newRecord2";
        final String recordName3 = "newRecord3";

        List<StorableExpenditureRecord> recordsBefore = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(0, recordsBefore.size());

        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate2 = (HazelcastExpenditureRecord) createRecord(recordName2, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, NOT_EXISTING_BANK_STATEMENT_ID);
        HazelcastExpenditureRecord recordToCreate3 = (HazelcastExpenditureRecord) createRecord(recordName3, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);

        List<StorableExpenditureRecord> recordsToCreate = Arrays.asList(recordToCreate, recordToCreate2, recordToCreate3);

        // add records via hazelcast
        assertExceptionOccurred(
                () -> hzExpenditureRecordCache.batchAdd(recordsToCreate),
                ExceptionUtil.dataIntegrityError()
        );

        // check hazelcast cache
        List<StorableExpenditureRecord> recordsAfter = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(recordsAfter.size(), recordsBefore.size());

        // check db state
        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(recordsBefore.size(), recordsDb.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void batchAddOneOfRecordsWithNotExistingExpenditureCategoryIdTest() throws InvalidDataException {
        final String recordName = "newRecord";
        final String recordName2 = "newRecord2";
        final String recordName3 = "newRecord3";

        List<StorableExpenditureRecord> recordsBefore = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(0, recordsBefore.size());

        HazelcastExpenditureRecord recordToCreate = (HazelcastExpenditureRecord) createRecord(recordName, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate2 = (HazelcastExpenditureRecord) createRecord(recordName2, 15.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);
        HazelcastExpenditureRecord recordToCreate3 = (HazelcastExpenditureRecord) createRecord(recordName3, 15.0,
                NOT_EXISTING_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID2);

        List<StorableExpenditureRecord> recordsToCreate = Arrays.asList(recordToCreate, recordToCreate2, recordToCreate3);

        // add records via hazelcast
        assertExceptionOccurred(
                () -> hzExpenditureRecordCache.batchAdd(recordsToCreate),
                ExceptionUtil.dataIntegrityError()
        );

        // check hazelcast cache
        List<StorableExpenditureRecord> recordsAfter = hzExpenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(recordsAfter.size(), recordsBefore.size());

        // check db state
        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID2);
        assertEquals(recordsBefore.size(), recordsDb.size());
    }

    private void compareCacheAndDbEntity(StorableExpenditureRecord storedRecord, IExpenditureRecord recordDb) {
        assertEquals(storedRecord.getId(), recordDb.getId());
        assertEquals(storedRecord.getName(), recordDb.getName());
        assertEquals(storedRecord.getAmount(), recordDb.getAmount());
        assertEquals(storedRecord.getCategoryId(), recordDb.getCategoryId());
        assertEquals(storedRecord.getBankStatementId(), recordDb.getBankStatementId());
    }

    private StorableExpenditureRecord createRecord(String expectedName, Double expectedAmount,
                                                   Integer expectedCategoryId, String expectedBankStatementId) {
        return new HazelcastExpenditureRecord(null, expectedName,
                expectedAmount, expectedCategoryId, expectedBankStatementId);
    }

    private StorableExpenditureRecord createRecord(Long expectedId, String expectedName, Double expectedAmount,
                                                   Integer expectedCategoryId, String expectedBankStatementId) {
        return new HazelcastExpenditureRecord(expectedId, expectedName,
                expectedAmount, expectedCategoryId, expectedBankStatementId);
    }

    private StorableExpenditureRecord getByName(List<StorableExpenditureRecord> records, String name) {
        return records.stream()
                .filter(er -> er.getName().equals(name))
                .findFirst().orElse(null);
    }

}
