package pl.intelligent.finance.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcherType;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ExpenditureCategoryServiceDbTest extends ServiceTestBase {

    @Autowired
    @Qualifier("expenditureCategoryServiceDb")
    private IExpenditureCategoryService service;

    @Autowired
    @Qualifier("expenditureCategoryMatcherServiceDb")
    private IExpenditureCategoryMatcherService matcherService;

    @Test
    @DisplayName("Should return all ids stored")
    public void findAllIdsTest() {
        List<Integer> ids = service.findAllIds();
        assertEquals(3, ids.size());

        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
        assertTrue(ids.contains(3));
    }

    @Test
    @DisplayName("Should return empty list if no ids stored")
    @Sql(scripts = "/sql/02_create_tables.sql")
    public void findAllIdsEmptyStoreTest() {
        List<Integer> ids = service.findAllIds();
        assertEquals(0, ids.size());
    }

    @Test
    @DisplayName("Should return all categories stored by ids")
    public void findByIdsTest() {
        Collection<Integer> ids = Arrays.asList(1, 2);

        List<IExpenditureCategory> categories = service.findByIds(ids);
        assertEquals(2, categories.size());

        assertEquals(3, categories.get(0).getMatchers().size());
        assertEquals(0, categories.get(1).getMatchers().size());
    }

    @Test
    @DisplayName("Should return empty category list when empty id list provided")
    public void findByIdsEmptyListProvidedTest() {
        Collection<Integer> ids = Collections.emptyList();

        List<IExpenditureCategory> categories = service.findByIds(ids);
        assertEquals(0, categories.size());
    }

    @Test
    @DisplayName("Should return all categories stored by ids and ignore not existing ones")
    public void findByIdsOneOfCategoriesDoesNotExistTest() {
        Collection<Integer> ids = Arrays.asList(1, 2, 99);

        List<IExpenditureCategory> categories = service.findByIds(ids);
        assertEquals(2, categories.size());

        assertEquals(3, categories.get(0).getMatchers().size());
        assertEquals(0, categories.get(1).getMatchers().size());
    }

    @Test
    @DisplayName("Should return category with matchers if found")
    public void findByNameTest() {
        IExpenditureCategory category = service.findByName(STORED_EXPENDITURE_CATEGORY);
        assertEquals(3, category.getMatchers().size());
    }

    @Test
    @DisplayName("Should return null when category with provided name not found")
    public void findByNameNotFoundTest() {
        IExpenditureCategory category = service.findByName(NOT_EXISTING_EXPENDITURE_CATEGORY);
        assertNull(category);
    }

    @Test
    @DisplayName("Should add new category")
    public void createTest() throws Exception {
        String name = "test category";

        IExpenditureCategory instance = service.createInstance();
        instance.setName(name);

        IExpenditureCategory persistedCategory = service.create(instance);
        assertNotNull(persistedCategory);
        instance.setId(persistedCategory.getId());
        assertEquals(instance, persistedCategory);

        IExpenditureCategory category = service.findByName(name);
        assertEquals(persistedCategory, category);
    }

    @Test
    @DisplayName("Should add new category along with associated matchers")
    public void createWithMatchersTest() throws Exception {
        IExpenditureCategoryMatcher matcherInstance = matcherService.createInstance();
        matcherInstance.setPattern("test1");
        matcherInstance.setMatcherType(ExpenditureCategoryMatcherType.REGEX);

        IExpenditureCategoryMatcher matcherInstance2 = matcherService.createInstance();
        matcherInstance2.setPattern("test2");
        matcherInstance2.setMatcherType(ExpenditureCategoryMatcherType.NORMAL);

        String name = "test category";

        IExpenditureCategory instance = service.createInstance();
        instance.setName(name);
        instance.addMatcher(matcherInstance);
        instance.addMatcher(matcherInstance2);

        IExpenditureCategory persistedCategory = service.create(instance);
        assertNotNull(persistedCategory);
        assertEquals(2, persistedCategory.getMatchers().size());

        instance.setId(persistedCategory.getId());
        assertEquals(instance, persistedCategory);

        IExpenditureCategory category = service.findByName(name);
        assertEquals(persistedCategory, category);
    }

}
