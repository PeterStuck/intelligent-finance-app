package pl.intelligent.finance.cache.impl;

import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.ExpenditureCategoryMatcherType;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HazelcastExpenditureCategoryCacheTest extends HazelcastCacheBaseTest {

    private HazelcastExpenditureCategoryCache hzExpCategoryCache;

    @BeforeEach
    public void setUp() {
        super.setUp();

        hzExpCategoryCache = new HazelcastExpenditureCategoryCache(hazelcastInstance, serviceProvider);

        IMap<Object, Object> map = hazelcastInstance.getMap(HazelcastExpenditureCategoryCache.CACHE_NAME);
        map.evictAll();
        map.loadAll(true);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void getAllTest() {
        List<? extends StorableExpenditureCategory> allCategories = hzExpCategoryCache.getAll();
        assertEquals(3, allCategories.size());

        StorableExpenditureCategory cat = getByName(allCategories, STORED_EXPENDITURE_CATEGORY);
        assertNotNull(cat);

        var expectedMatcher = createMatcher(1, "^test$", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher2 = createMatcher(2, "^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher3 = createMatcher(3, "^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
        var expectedCat = createCategory(STORED_EXPENDITURE_CATEGORY_ID, STORED_EXPENDITURE_CATEGORY,
                null, Arrays.asList(expectedMatcher, expectedMatcher2, expectedMatcher3));
        assertEquals(expectedCat, cat);

        List<Integer> allIds = serviceProvider.getExpenditureCategoryService().findAllIds();
        assertEquals(allCategories.size(), allIds.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql"})
    public void getAllEmptyListTest() {
        List<? extends StorableExpenditureCategory> allCategories = hzExpCategoryCache.getAll();
        assertEquals(0, allCategories.size());

        List<Integer> allIds = serviceProvider.getExpenditureCategoryService().findAllIds();
        assertEquals(allCategories.size(), allIds.size());
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void getByNameTest() {
        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);

        var expectedMatcher = createMatcher(1, "^test$", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher2 = createMatcher(2, "^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher3 = createMatcher(3, "^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
        var expectedCat = createCategory(STORED_EXPENDITURE_CATEGORY_ID, STORED_EXPENDITURE_CATEGORY,
                null, Arrays.asList(expectedMatcher, expectedMatcher2, expectedMatcher3));
        assertEquals(expectedCat, category);

        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void getByNameEmptyMatcherListTest() {
        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(category);
        var expectedCat = createCategory(STORED_EXPENDITURE_CATEGORY_ID3, STORED_EXPENDITURE_CATEGORY3,
                null, Collections.emptyList());
        assertEquals(expectedCat, category);

        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(categoryDb);
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addTest() throws InvalidDataException {
        String name = "testCategory";

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(name);
        assertNull(category);

        var categoryToCreate = (HazelcastExpenditureCategory) createCategory(name,null, Collections.emptyList());
        StorableExpenditureCategory persistedCategory = hzExpCategoryCache.add(categoryToCreate);
        assertNotNull(persistedCategory);
        categoryToCreate.setId(persistedCategory.getId());
        assertEquals(categoryToCreate, persistedCategory);

        StorableExpenditureCategory storedCategory = hzExpCategoryCache.getByName(name);
        assertNotNull(storedCategory);
        assertEquals(persistedCategory, storedCategory);

        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(name);
        assertNotNull(categoryDb);
        assertCacheEntityWithDbEntity(storedCategory, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addWithMatchersTest() throws InvalidDataException {
        String name = "testCategory";

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(name);
        assertNull(category);

        var matcher = createMatcher("^test$", ExpenditureCategoryMatcherType.REGEX);
        var matcher2 = createMatcher("^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var matcher3 = createMatcher("^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
        var categoryToCreate = (HazelcastExpenditureCategory) createCategory(name,null,
                Arrays.asList(matcher, matcher2, matcher3));
        StorableExpenditureCategory persistedCategory = hzExpCategoryCache.add(categoryToCreate);
        assertNotNull(persistedCategory);
        categoryToCreate.setId(persistedCategory.getId());
        ((HazelcastExpenditureCategoryMatcher) categoryToCreate.getMatchers().get(0)).setId(persistedCategory.getMatchers().get(0).getId());
        ((HazelcastExpenditureCategoryMatcher) categoryToCreate.getMatchers().get(1)).setId(persistedCategory.getMatchers().get(1).getId());
        ((HazelcastExpenditureCategoryMatcher) categoryToCreate.getMatchers().get(2)).setId(persistedCategory.getMatchers().get(2).getId());
        assertEquals(categoryToCreate, persistedCategory);

        StorableExpenditureCategory storedCategory = hzExpCategoryCache.getByName(name);
        assertNotNull(storedCategory);
        assertEquals(persistedCategory, storedCategory);

        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(name);
        assertNotNull(categoryDb);
        assertCacheEntityWithDbEntity(storedCategory, categoryDb);
    }

    private void assertCacheEntityWithDbEntity(StorableExpenditureCategory storedCategory, IExpenditureCategory categoryDb) {
        assertEquals(storedCategory.getId(), categoryDb.getId());
        assertEquals(storedCategory.getName(), categoryDb.getName());
        assertEquals(storedCategory.getParentCategoryId(), categoryDb.getParentCategoryId());
        if (categoryDb.getMatchers() != null) {
            assertEquals(storedCategory.getMatchers().size(), categoryDb.getMatchers().size());
            categoryDb.getMatchers().forEach(matDb -> {
                var matcher = getMatcherByPattern(storedCategory.getMatchers(), matDb.getPattern());
                assertNotNull(matcher);
                assertEquals(matDb.getId(), matcher.getId());
                assertEquals(matDb.getPattern(), matcher.getPattern());
                assertEquals(matDb.getMatcherType().getId(), matcher.getMatcherType().getId());
            });
        } else {
            assertEquals(0, storedCategory.getMatchers().size());
        }
    }

    private StorableExpenditureCategoryMatcher getMatcherByPattern(List<? extends StorableExpenditureCategoryMatcher> matchers, String pattern) {
        return matchers.stream()
                .filter(mat -> mat.getPattern().equals(pattern))
                .findFirst().orElse(null);
    }

    private StorableExpenditureCategory getByName(List<? extends StorableExpenditureCategory> categories, String name) {
        return categories.stream()
                .filter(cat -> cat.getName().equals(name))
                .findFirst().orElse(null);
    }

    private StorableExpenditureCategory createCategory(String name,
                                                       Integer parentCategoryId,
                                                       List<StorableExpenditureCategoryMatcher> matchers) {
        return createCategory(null, name, parentCategoryId, matchers);
    }

    private StorableExpenditureCategory createCategory(Integer id, String name,
                                                       Integer parentCategoryId,
                                                       List<? extends StorableExpenditureCategoryMatcher> matchers) {
        return new HazelcastExpenditureCategory(id, name, parentCategoryId, matchers);
    }

    private StorableExpenditureCategoryMatcher createMatcher(String pattern, ExpenditureCategoryMatcherType type) {
        return createMatcher(null, pattern, type);
    }

    private StorableExpenditureCategoryMatcher createMatcher(Integer id, String pattern, ExpenditureCategoryMatcherType type) {
        return new HazelcastExpenditureCategoryMatcher(id, pattern, type);
    }

}
