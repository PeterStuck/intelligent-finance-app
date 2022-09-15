package pl.intelligent.finance.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.impl.ExpenditureCategory;
import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcherType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ExpenditureCategoryRepositoryTest extends RepositoryTestBase {

    @Autowired
    private ExpenditureCategoryRepository categoryRepository;

    @Test
    @DisplayName("Should return category along with associated matchers")
    public void findByNameTest() {
        ExpenditureCategory category = categoryRepository.findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(1, category.getId());
        assertEquals(STORED_EXPENDITURE_CATEGORY, category.getName());
        assertEquals(null, category.getParentCategoryId());
        assertEquals(3, category.getMatchers().size());

        List<IExpenditureCategoryMatcher> matchers = category.getMatchers();

        String pattern = "^test$";
        IExpenditureCategoryMatcher matcher = getMatcherByPattern(matchers, pattern);
        assertEquals(1, matcher.getId());
        assertEquals(ExpenditureCategoryMatcherType.REGEX, matcher.getMatcherType());
        assertEquals(pattern, matcher.getPattern());

        String pattern2 = "^test[0-9]*";
        matcher = getMatcherByPattern(matchers, pattern2);
        assertEquals(2, matcher.getId());
        assertEquals(ExpenditureCategoryMatcherType.REGEX, matcher.getMatcherType());
        assertEquals(pattern2, matcher.getPattern());

        String pattern3 = "^[A-Za-z]{1,}";
        matcher = getMatcherByPattern(matchers, pattern3);
        assertEquals(3, matcher.getId());
        assertEquals(ExpenditureCategoryMatcherType.REGEX, matcher.getMatcherType());
        assertEquals(pattern3, matcher.getPattern());
    }

    @Test
    @DisplayName("Should return category with empty matcher list")
    public void findByNameEmptyMatcherListTest() {
        ExpenditureCategory category = categoryRepository.findByName(STORED_EXPENDITURE_CATEGORY3);
        assertNotNull(category);
        assertEquals(STORED_EXPENDITURE_CATEGORY_ID3, category.getId());
        assertEquals(STORED_EXPENDITURE_CATEGORY3, category.getName());
        assertEquals(null, category.getParentCategoryId());
        assertEquals(0, category.getMatchers().size());
    }

    @Test
    @DisplayName("Should return all categories")
    public void findAllTest() {
        List<ExpenditureCategory> categories = categoryRepository.findAll();
        assertNotNull(categories);
        assertEquals(3, categories.size());

        assertNotNull(getCategoryByName(categories, STORED_EXPENDITURE_CATEGORY));
        assertNotNull(getCategoryByName(categories, STORED_EXPENDITURE_CATEGORY2));
        assertNotNull(getCategoryByName(categories, STORED_EXPENDITURE_CATEGORY3));
    }

    @Test
    @DisplayName("Should add new category along with associated matchers")
    public void addCategoryWithMatchersTest() {
        List<ExpenditureCategoryMatcher> matchers = Arrays.asList(
                createMatcher("test", ExpenditureCategoryMatcherType.NORMAL),
                createMatcher("test2", ExpenditureCategoryMatcherType.REGEX)
        );

        String categoryName = "testCategory";
        ExpenditureCategory category = createCategory(categoryName, matchers);

        ExpenditureCategory persistedCategory = categoryRepository.saveAndFlush(category);

        assertNotNull(persistedCategory);
        category.setId(persistedCategory.getId());
        assertEquals(category, persistedCategory);
    }

    @Test
    @DisplayName("Should add new category with no matchers associated yet")
    public void addCategoryTest() {
        String categoryName = "testCategory";
        ExpenditureCategory category = createCategory(categoryName, null);

        ExpenditureCategory persistedCategory = categoryRepository.saveAndFlush(category);

        assertNotNull(persistedCategory);
        category.setId(persistedCategory.getId());
        assertEquals(category, persistedCategory);
    }

    @Test
    @DisplayName("Should throw exception when provided category name already exists")
    public void addCategoryWithExistingNameTest() {
        ExpenditureCategory category = createCategory(STORED_EXPENDITURE_CATEGORY, null);

        assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.saveAndFlush(category));
    }

    @Test
    @DisplayName("Should throw exception when provided parent category doesn't exist")
    public void addCategoryWithParentCategoryNotFoundTest() {
        String categoryName = "testCategory";
        ExpenditureCategory category = createCategory(categoryName, null);
        category.setParentCategoryId(99);

        assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.saveAndFlush(category));
    }

    @Test
    @DisplayName("Should add new matcher to existing category")
    public void updateCategoryAddMatcherTest() {
        ExpenditureCategoryMatcher testMatcher = createMatcher("test", ExpenditureCategoryMatcherType.NORMAL);

        ExpenditureCategory category = categoryRepository.findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        category.addMatcher(testMatcher);

        ExpenditureCategory persistedCategory = categoryRepository.saveAndFlush(category);
        assertNotNull(persistedCategory);
        assertEquals(category, persistedCategory);
    }

    @Test
    @DisplayName("Should update existing category")
    public void updateCategoryTest() {
        ExpenditureCategory category = categoryRepository.findByName(STORED_EXPENDITURE_CATEGORY);
        assertNotNull(category);
        assertEquals(3, category.getMatchers().size());

        category.setName("new name");

        ExpenditureCategory persistedCategory = categoryRepository.saveAndFlush(category);
        assertNotNull(persistedCategory);
        assertEquals(category, persistedCategory);
    }

    @Test
    @DisplayName("Should delete existing category")
    public void deleteCategoryTest() {
        String categoryName = "testCategory";
        ExpenditureCategory categoryToPersist = createCategory(categoryName, null);

        categoryRepository.saveAndFlush(categoryToPersist);

        ExpenditureCategory category = categoryRepository.findByName(categoryName);
        assertNotNull(category);

        categoryRepository.delete(category);

        category = categoryRepository.findByName(categoryName);
        assertNull(category);
    }

    @Test
    @DisplayName("Should throw exception when category to delete not found")
    public void deleteCategoryNotFoundTest() {
        assertThrows(EmptyResultDataAccessException.class, () -> categoryRepository.deleteById(99));
    }

    private ExpenditureCategory createCategory(String name, List<ExpenditureCategoryMatcher> matchers) {
        return new ExpenditureCategory(null, name,null, matchers);
    }

    private ExpenditureCategoryMatcher createMatcher(String pattern, ExpenditureCategoryMatcherType matcherType) {
        return new ExpenditureCategoryMatcher(null, pattern, matcherType, null);
    }

    private IExpenditureCategoryMatcher getMatcherByPattern(List<IExpenditureCategoryMatcher> matchers, String pattern) {
        return matchers.stream()
                .filter(matcher -> matcher.getPattern().equals(pattern))
                .findFirst().orElse(null);
    }

    private IExpenditureCategory getCategoryByName(List<ExpenditureCategory> categories, String name) {
        return categories.stream()
                .filter(cat -> cat.getName().equals(name))
                .findFirst().orElse(null);
    }

}
