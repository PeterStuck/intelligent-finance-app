package pl.intelligent.finance.cache.serialization;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategoryMatcher;
import pl.intelligent.finance.resource.entity.ExpenditureCategoryMatcherType;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HazelcastExpenditureCategorySerializationTest {

    private static HazelcastInstance hazelcastInstance;

    private static IMap<Integer, HazelcastExpenditureCategory> map;

    @BeforeAll
    public static void setUpClass() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        map = hazelcastInstance.getMap("if-test-ex-cat");
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
        HazelcastExpenditureCategory written = new HazelcastExpenditureCategory(1, "test", 15, Collections.emptyList());

        map.set(1, written);

        HazelcastExpenditureCategory read = map.get(1);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithMatchersTest() {
        var matcher = new HazelcastExpenditureCategoryMatcher(1, "test", ExpenditureCategoryMatcherType.NORMAL);
        var matcher2 = new HazelcastExpenditureCategoryMatcher(2, "test2", ExpenditureCategoryMatcherType.REGEX);

        HazelcastExpenditureCategory written = new HazelcastExpenditureCategory(1, "test", 15, Arrays.asList(matcher, matcher2));

        map.set(1, written);

        HazelcastExpenditureCategory read = map.get(1);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithNullsTest() {
        var matcher = new HazelcastExpenditureCategoryMatcher(1, "test", ExpenditureCategoryMatcherType.NORMAL);
        var matcher2 = new HazelcastExpenditureCategoryMatcher(2, "test2", ExpenditureCategoryMatcherType.REGEX);

        HazelcastExpenditureCategory written = new HazelcastExpenditureCategory(1, null, null, Arrays.asList(matcher, matcher2));

        map.set(1, written);

        HazelcastExpenditureCategory read = map.get(1);

        assertEquals(written, read);
    }

    @Test
    public void serializeWithAllNullsTest() {
        HazelcastExpenditureCategory written = new HazelcastExpenditureCategory(null, null, null, null);

        map.set(1, written);

        HazelcastExpenditureCategory read = map.get(1);

        assertEquals(written, read);
    }



}
