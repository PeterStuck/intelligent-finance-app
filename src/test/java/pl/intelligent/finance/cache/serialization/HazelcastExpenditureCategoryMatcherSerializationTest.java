package pl.intelligent.finance.cache.serialization;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategoryMatcher;
import pl.intelligent.finance.resource.entity.ExpenditureCategoryMatcherType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HazelcastExpenditureCategoryMatcherSerializationTest {

    private static HazelcastInstance hazelcastInstance;

    private static IMap<Integer, HazelcastExpenditureCategoryMatcher> map;

    @BeforeAll
    public static void setUpClass() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        map = hazelcastInstance.getMap("if-test-ex-cat-match");
    }

    @BeforeEach
    public void setUp() {
        map.evict(1);
    }

    @AfterAll
    public static void tearDownClass() {
        hazelcastInstance.shutdown();
    }

    @Test
    public void serializeTest() {
        HazelcastExpenditureCategoryMatcher written = new HazelcastExpenditureCategoryMatcher(1, "test", ExpenditureCategoryMatcherType.NORMAL);

        map.set(1, written);

        HazelcastExpenditureCategoryMatcher read = map.get(1);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithNullsTest() {
        HazelcastExpenditureCategoryMatcher written = new HazelcastExpenditureCategoryMatcher(1, null, ExpenditureCategoryMatcherType.NORMAL);

        map.set(1, written);

        HazelcastExpenditureCategoryMatcher read = map.get(1);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithAllNullsTest() {
        HazelcastExpenditureCategoryMatcher written = new HazelcastExpenditureCategoryMatcher(null, null, null);

        map.set(1, written);

        HazelcastExpenditureCategoryMatcher read = map.get(1);

        assertEquals(written, read);
    }



}
