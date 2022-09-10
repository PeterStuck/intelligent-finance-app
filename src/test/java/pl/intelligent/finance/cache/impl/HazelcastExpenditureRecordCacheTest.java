package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.cache.config.HazelcastConfigBuilder;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;
import pl.intelligent.finance.service.provider.ServiceProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
public class HazelcastExpenditureRecordCacheTest {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private ServiceProvider serviceProvider;

    private HazelcastExpenditureRecordCache expenditureRecordCache;

    protected static String STORED_BANK_STATEMENT_ID = "062022";
    protected static String STORED_BANK_STATEMENT_ID2 = "072022";
    protected static String NOT_EXISTING_BANK_STATEMENT_ID = "not_existing_bsid";

    protected static Integer STORED_EXPENDITURE_CATEGORY_ID = 1;

    protected static String NOT_EXISTING_RECORD_NAME = "not_existing_record";
    protected static String STORED_RECORD_NAME = "record1";
    protected static String STORED_RECORD_NAME2 = "record2";
    protected static String STORED_RECORD_NAME3 = "record3";
    protected static String STORED_RECORD_NAME4 = "record4";

    private boolean HAZELCAST_INSTANCE_INITIALIZED = false;

    @BeforeEach
    public void setUp() {
        if (!HAZELCAST_INSTANCE_INITIALIZED) {
            HazelcastConfigBuilder.initialize(hazelcastInstance, serviceProvider);
            HAZELCAST_INSTANCE_INITIALIZED = true;
        }

        expenditureRecordCache = new HazelcastExpenditureRecordCache(hazelcastInstance, serviceProvider);
    }

    @AfterEach
    public void tearDown() {
        IMap<Object, Object> map = hazelcastInstance.getMap(HazelcastExpenditureRecordCache.CACHE_NAME);
        map.loadAll(true);
    }

    @Test
    public void getAllByBankStatementIdTest() {
        List<StorableExpenditureRecord> records = expenditureRecordCache.getAllByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, records.size());

        StorableExpenditureRecord storableExpenditureRecord = getByName(records, STORED_RECORD_NAME);
        assertNotNull(storableExpenditureRecord);
        StorableExpenditureRecord expectedRecord = createExpectedRecord(1L, STORED_RECORD_NAME, 10.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        storableExpenditureRecord = getByName(records, STORED_RECORD_NAME2);
        assertNotNull(storableExpenditureRecord);
        expectedRecord = createExpectedRecord(2L, STORED_RECORD_NAME2, 8.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        storableExpenditureRecord = getByName(records, STORED_RECORD_NAME3);
        assertNotNull(storableExpenditureRecord);
        expectedRecord = createExpectedRecord(3L, STORED_RECORD_NAME3, 5.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        storableExpenditureRecord = getByName(records, STORED_RECORD_NAME4);
        assertNotNull(storableExpenditureRecord);
        expectedRecord = createExpectedRecord(4L, STORED_RECORD_NAME4, 11.0,
                STORED_EXPENDITURE_CATEGORY_ID, STORED_BANK_STATEMENT_ID);
        assertEquals(expectedRecord, storableExpenditureRecord);

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(STORED_BANK_STATEMENT_ID);
        assertEquals(4, recordsDb.size());
    }

    @Test
    public void getAllByBankStatementIdEmptyResultTest() {
        List<StorableExpenditureRecord> records = expenditureRecordCache.getAllByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(0, records.size());

        List<IExpenditureRecord> recordsDb = serviceProvider.getExpenditureRecordService().findByBankStatementId(NOT_EXISTING_BANK_STATEMENT_ID);
        assertEquals(0, recordsDb.size());
    }

    private StorableExpenditureRecord createExpectedRecord(Long expectedId, String expectedName, Double expectedAmount,
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
