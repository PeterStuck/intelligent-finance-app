package pl.intelligent.finance.cache.serialization;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HazelcastExpenditureRecordSerializationTest {

    private static HazelcastInstance hazelcastInstance;

    private static IMap<Long, HazelcastExpenditureRecord> map;

    @BeforeAll
    public static void setUpClass() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        map = hazelcastInstance.getMap("if-test-ex-rec");
    }

    @BeforeEach
    public void setUp() {
        map.evict(1L);
    }

    @Test
    public void serializeTest() {
        HazelcastExpenditureRecord written = new HazelcastExpenditureRecord(1L, "test", 10.0, 1, "test");

        map.set(1L, written);

        HazelcastExpenditureRecord read = map.get(1L);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithNullsTest() {
        HazelcastExpenditureRecord written = new HazelcastExpenditureRecord(1L, null, 10.0, 1, null);

        map.set(1L, written);

        HazelcastExpenditureRecord read = map.get(1L);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithAllNullsTest() {
        HazelcastExpenditureRecord written = new HazelcastExpenditureRecord(null, null, null, null, null);

        map.set(1L, written);

        HazelcastExpenditureRecord read = map.get(1L);

        assertEquals(written, read);
    }



}
