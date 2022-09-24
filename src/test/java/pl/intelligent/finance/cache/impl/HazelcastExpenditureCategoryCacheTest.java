package pl.intelligent.finance.cache.impl;

import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategoryMatcher;
import pl.intelligent.finance.persistence.entity.IExpenditureCategory;
import pl.intelligent.finance.exception.ExceptionUtil;
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

        var expectedMatcher = (HazelcastExpenditureCategoryMatcher) createMatcher(1, "^test$", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher2 = (HazelcastExpenditureCategoryMatcher) createMatcher(2, "^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher3 = (HazelcastExpenditureCategoryMatcher) createMatcher(3, "^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
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

        var expectedMatcher = (HazelcastExpenditureCategoryMatcher) createMatcher(1, "^test$", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher2 = (HazelcastExpenditureCategoryMatcher) createMatcher(2, "^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var expectedMatcher3 = (HazelcastExpenditureCategoryMatcher) createMatcher(3, "^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
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

        var matcher = (HazelcastExpenditureCategoryMatcher) createMatcher("^test$", ExpenditureCategoryMatcherType.REGEX);
        var matcher2 = (HazelcastExpenditureCategoryMatcher) createMatcher("^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var matcher3 = (HazelcastExpenditureCategoryMatcher) createMatcher("^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
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

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void addWithExistingCategoryNameTest() throws InvalidDataException {
        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY2);
        assertNotNull(category);

        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY2);
        assertNotNull(categoryDb);

        var matcher = (HazelcastExpenditureCategoryMatcher) createMatcher("^test$", ExpenditureCategoryMatcherType.REGEX);
        var matcher2 = (HazelcastExpenditureCategoryMatcher) createMatcher("^test[0-9]*", ExpenditureCategoryMatcherType.REGEX);
        var matcher3 = (HazelcastExpenditureCategoryMatcher) createMatcher("^[A-Za-z]{1,}", ExpenditureCategoryMatcherType.REGEX);
        var categoryToCreate = (HazelcastExpenditureCategory) createCategory(STORED_EXPENDITURE_CATEGORY2,null,
                Arrays.asList(matcher, matcher2, matcher3));

        assertExceptionOccurred(
                () -> hzExpCategoryCache.add(categoryToCreate),
                ExceptionUtil.dataIntegrityError()
        );
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void attachMatcherTest() throws InvalidDataException {
        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(categoryDb);
        assertEquals(0, categoryDb.getMatchers().size());

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(category);
        assertEquals(0, category.getMatchers().size());

        var newMatcher = (HazelcastExpenditureCategoryMatcher) createMatcher("test123", ExpenditureCategoryMatcherType.NORMAL);
        hzExpCategoryCache.attachMatcher(STORED_EXPENDITURE_CATEGORY3, newMatcher);

        category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(category);
        assertEquals(1, category.getMatchers().size());

        StorableExpenditureCategoryMatcher storedMatcher = category.getMatchers().get(0);
        newMatcher.setId(storedMatcher.getId());
        assertEquals(newMatcher, storedMatcher);

        categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(categoryDb);
        assertEquals(1, categoryDb.getMatchers().size());
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void detachMatcherTest() throws InvalidDataException {
        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        hzExpCategoryCache.detachMatcher(STORED_EXPENDITURE_CATEGORY, 1);

        category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(2, category.getMatchers().size());
        assertFalse(category.getMatchers().stream().anyMatch(m -> m.getId().equals(1)));

        categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(2, categoryDb.getMatchers().size());
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void detachMatcherNotFoundTest() throws InvalidDataException {
        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        assertExceptionOccurred(
                () -> hzExpCategoryCache.detachMatcher(STORED_EXPENDITURE_CATEGORY, 99),
                ExceptionUtil.expenditureCategoryMatcherNotFound(STORED_EXPENDITURE_CATEGORY, 99)
        );

        category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());
        assertTrue(category.getMatchers().stream().anyMatch(m -> m.getId().equals(1)));

        categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void detachMatcherByPatternTest() throws InvalidDataException {
        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        var pattern = "^test$";
        hzExpCategoryCache.detachMatcherByPattern(STORED_EXPENDITURE_CATEGORY, pattern);

        category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(2, category.getMatchers().size());
        assertFalse(category.getMatchers().stream().anyMatch(m -> m.getPattern().equals(pattern)));

        categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(2, categoryDb.getMatchers().size());
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void detachMatcherByPatternNotFoundTest() throws InvalidDataException {
        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());

        var pattern = "123.{1,3}--";
        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());
        assertFalse(category.getMatchers().stream().anyMatch(m -> m.getPattern().equals(pattern)));

        assertExceptionOccurred(
                () -> hzExpCategoryCache.detachMatcherByPattern(STORED_EXPENDITURE_CATEGORY, pattern),
                ExceptionUtil.expenditureCategoryMatcherNotFound(STORED_EXPENDITURE_CATEGORY, pattern)
        );

        category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());
        assertCacheEntityWithDbEntity(category, categoryDb);
    }

    @Test
    @Sql(scripts={"/sql/02_create_tables.sql", "/data/test_cache_data.sql"})
    public void detachAllMatchersTest() throws InvalidDataException {
        IExpenditureCategory categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(3, categoryDb.getMatchers().size());

        StorableExpenditureCategory category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        hzExpCategoryCache.detachAllMatchers(STORED_EXPENDITURE_CATEGORY);

        category = hzExpCategoryCache.getByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(0, category.getMatchers().size());

        categoryDb = serviceProvider.getExpenditureCategoryService().findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(categoryDb);
        assertEquals(0, categoryDb.getMatchers().size());
        assertCacheEntityWithDbEntity(category, categoryDb);
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
                                                       List<HazelcastExpenditureCategoryMatcher> matchers) {
        return createCategory(null, name, parentCategoryId, matchers);
    }

    private StorableExpenditureCategory createCategory(Integer id, String name,
                                                       Integer parentCategoryId,
                                                       List<HazelcastExpenditureCategoryMatcher> matchers) {
        return new HazelcastExpenditureCategory(id, name, parentCategoryId, matchers);
    }

    private StorableExpenditureCategoryMatcher createMatcher(String pattern, ExpenditureCategoryMatcherType type) {
        return createMatcher(null, pattern, type);
    }

    private StorableExpenditureCategoryMatcher createMatcher(Integer id, String pattern, ExpenditureCategoryMatcherType type) {
        return new HazelcastExpenditureCategoryMatcher(id, pattern, type);
    }

}
