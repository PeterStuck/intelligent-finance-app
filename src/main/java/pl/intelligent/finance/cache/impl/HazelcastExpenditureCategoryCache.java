package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import pl.intelligent.finance.cache.ExpenditureCategoryStore;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;
import pl.intelligent.finance.service.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

import static pl.intelligent.finance.cache.util.ExpenditureCategoryAdapter.createExpenditureCategory;
import static pl.intelligent.finance.cache.util.ExpenditureCategoryMatcherUtil.hzMatcherManager;

public class HazelcastExpenditureCategoryCache extends HazelcastCacheBase<HazelcastExpenditureCategory, IExpenditureCategory>
        implements ExpenditureCategoryStore {

    public static final String CACHE_NAME = CACHE_NAME_PREFIX + "expenditure-categories";

    private IMap<Integer, HazelcastExpenditureCategory> expenditureCategoryMap;

    private IExpenditureCategoryService categoryService;
    private IExpenditureCategoryMatcherService matcherService;

    public HazelcastExpenditureCategoryCache(HazelcastInstance hazelcastInstance, ServiceProvider serviceProvider) {
        super(serviceProvider);
        this.categoryService = serviceProvider.getExpenditureCategoryService();
        this.matcherService = serviceProvider.getExpenditureCategoryMatcherService();
        this.expenditureCategoryMap = hazelcastInstance.getMap(CACHE_NAME);
    }

    @Override
    public List<? extends StorableExpenditureCategory> getAll() {
        return new ArrayList<>(expenditureCategoryMap.values());
    }

    @Override
    public StorableExpenditureCategory getByName(String name) {
        PredicateBuilder.EntryObject entryObject = Predicates.newPredicateBuilder().getEntryObject();
        Predicate predicate = entryObject.get("name").equal(name);

        return (StorableExpenditureCategory) expenditureCategoryMap.values(predicate)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public StorableExpenditureCategory add(StorableExpenditureCategory category) throws InvalidDataException {
        var result = attemptOperation(() -> addToService(category));

        if (result.isError()) {
            throw result.exception();
        }

        HazelcastExpenditureCategory expenditureCategory = result.cacheEntity();

        setOnCache(expenditureCategory);

        return expenditureCategory;
    }

    private HazelcastExpenditureCategory addToService(StorableExpenditureCategory category) throws Exception {
        IExpenditureCategory categoryToCreate = getConvertedDbEntity((HazelcastExpenditureCategory) category);
        IExpenditureCategory persistedCategory = categoryService.create(categoryToCreate);
        if (persistedCategory == null) {
            throw ExceptionUtil.entityCreationError("Expenditure Category");
        }

        return getConvertedCacheEntity(persistedCategory);
    }

    @Override
    public boolean attachMatcher(final String categoryName, StorableExpenditureCategoryMatcher matcher) throws InvalidDataException {
        var storedCategory = getByName(categoryName);

        hzMatcherManager(storedCategory).addMatcher(matcher);

        var result = attemptOperation(() -> updateCategoryOnService(storedCategory));

        if (result.isError()) {
            throw result.exception();
        }

        setOnCache(result.cacheEntity());

        return true;
    }

    private HazelcastExpenditureCategory updateCategoryOnService(StorableExpenditureCategory category) throws Exception {
        IExpenditureCategory categoryToUpdate = getConvertedDbEntity((HazelcastExpenditureCategory) category);
        IExpenditureCategory updatedCategory = categoryService.update(categoryToUpdate);
        if (updatedCategory == null) {
            throw ExceptionUtil.entityUpdateError("Expenditure Category");
        }

        return getConvertedCacheEntity(updatedCategory);
    }

    @Override
    public boolean detachMatcher(String categoryName, int matcherId) throws InvalidDataException {
        var storedCategory = getByName(categoryName);

        var matcherToDelete = hzMatcherManager(storedCategory).detachMatcher(matcherId);

        var result = attemptOperation(() -> removeCategoryMatcherOnService(storedCategory, matcherToDelete));

        if (result.isError()) {
            throw result.exception();
        }

        setOnCache(storedCategory);

        return true;
    }

    @Override
    public boolean detachMatcherByPattern(String categoryName, String pattern) throws InvalidDataException {
        var storedCategory = getByName(categoryName);

        var matcherToDelete = hzMatcherManager(storedCategory).detachMatcherByPattern(pattern);

        var result = attemptOperation(() -> removeCategoryMatcherOnService(storedCategory, matcherToDelete));

        if (result.isError()) {
            throw result.exception();
        }

        setOnCache(storedCategory);

        return true;
    }

    private HazelcastExpenditureCategory removeCategoryMatcherOnService(StorableExpenditureCategory category, StorableExpenditureCategoryMatcher matcher) {
        var categoryDb = getConvertedDbEntity((HazelcastExpenditureCategory) category);
        matcherService.deleteById(categoryDb, matcher.getId());

        return null;
    }

    @Override
    public boolean detachAllMatchers(String categoryName) throws InvalidDataException {
        var storedCategory = getByName(categoryName);

        var matchersToDelete = hzMatcherManager(storedCategory).detachAllMatchers();

        var result = attemptOperation(() -> removeCategoryMatchersOnService(storedCategory, matchersToDelete));

        if (result.isError()) {
            throw result.exception();
        }

        setOnCache(storedCategory);

        return true;
    }

    private HazelcastExpenditureCategory removeCategoryMatchersOnService(StorableExpenditureCategory category,
                                                                         List<? extends StorableExpenditureCategoryMatcher> matchers) {
        var categoryDb = getConvertedDbEntity((HazelcastExpenditureCategory) category);
        var matcherIdsToDelete = matchers.stream().map(StorableExpenditureCategoryMatcher::getId).toList();
        matcherService.deleteAllByIds(categoryDb, matcherIdsToDelete);

        return null;
    }

    private void setOnCache(StorableExpenditureCategory expenditureCategory) {
        expenditureCategoryMap.set(expenditureCategory.getId(), (HazelcastExpenditureCategory) expenditureCategory);
    }

    @Override
    protected HazelcastExpenditureCategory getConvertedCacheEntity(IExpenditureCategory entity) {
        return createExpenditureCategory(entity);
    }

    @Override
    protected IExpenditureCategory getConvertedDbEntity(HazelcastExpenditureCategory entity) {
        return createExpenditureCategory(categoryService, matcherService, entity);
    }
}
