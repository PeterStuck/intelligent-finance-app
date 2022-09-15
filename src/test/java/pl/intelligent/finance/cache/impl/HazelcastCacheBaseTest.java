package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import pl.intelligent.finance.cache.config.HazelcastConfigBuilder;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.service.provider.ServiceProvider;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class HazelcastCacheBaseTest {

    @Autowired
    protected HazelcastInstance hazelcastInstance;

    @Autowired
    protected ServiceProvider serviceProvider;

    protected static String STORED_BANK_STATEMENT_ID = "062022";
    protected static String STORED_BANK_STATEMENT_ID2 = "072022";
    protected static String NOT_EXISTING_BANK_STATEMENT_ID = "not_existing_bsid";

    protected static Integer STORED_EXPENDITURE_CATEGORY_ID = 1;
    protected static Integer NOT_EXISTING_EXPENDITURE_CATEGORY_ID = 99;

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

        IMap<Object, Object> map = hazelcastInstance.getMap(HazelcastExpenditureRecordCache.CACHE_NAME);
        map.evictAll();
        map.loadAll(true);
    }

    public void assertExceptionOccurred(Callable callable, InvalidDataException expectedException) {
        InvalidDataException exception = null;
        try {
            callable.call();
        } catch (Exception e) {
            exception = (InvalidDataException) e;
        }

        assertNotNull(exception);
        assertEquals(expectedException.getMessage(), exception.getMessage());
        assertEquals(expectedException.getResultCode(), exception.getResultCode());
    }
}
