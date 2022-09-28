package pl.intelligent.finance.cache.subsystem.impl.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureCategory;
import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureCategoryMatcher;
import pl.intelligent.finance.cache.subsystem.exception.ExceptionUtil;
import pl.intelligent.finance.cache.subsystem.exception.InvalidDataException;
import pl.intelligent.finance.cache.subsystem.impl.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.subsystem.store.ExpenditureCategoryStore;
import pl.intelligent.finance.persistence.entity.IExpenditureCategory;
import pl.intelligent.finance.persistence.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.persistence.service.IExpenditureCategoryService;
import pl.intelligent.finance.persistence.service.ServiceProvider;


import java.util.ArrayList;
import java.util.List;

import static pl.intelligent.finance.cache.subsystem.impl.util.ExpenditureCategoryAdapter.createExpenditureCategory;
import static pl.intelligent.finance.cache.subsystem.impl.util.ExpenditureCategoryMatcherUtil.hzMatcherManager;

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
        logger().info("Add expenditure category to cache: {}", category);
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
        logger().info("Attach matcher: {} to expenditure category: {} on cache", matcher, categoryName);
        var storedCategory = getByName(categoryName);
        logger().debug("Attach matcher: {}, found category: {}", matcher, storedCategory);

        hzMatcherManager(storedCategory, logger()).addMatcher(matcher);

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
        logger().info("Detach matcher: {} from expenditure category: {} on cache", matcherId, categoryName);
        var storedCategory = getByName(categoryName);
        logger().debug("Detach matcher: {}, found category: {}", matcherId, storedCategory);

        var matcherToDelete = hzMatcherManager(storedCategory, logger()).detachMatcher(matcherId);

        var result = attemptOperation(() -> removeCategoryMatcherOnService(storedCategory, matcherToDelete));

        if (result.isError()) {
            throw result.exception();
        }

        setOnCache(storedCategory);

        return true;
    }

    @Override
    public boolean detachMatcherByPattern(String categoryName, String pattern) throws InvalidDataException {
        logger().info("Detach matcher by patten: {} from expenditure category: {} on cache", pattern, categoryName);
        var storedCategory = getByName(categoryName);
        logger().debug("Detach matcher by pattern: {}, found category: {}", pattern, storedCategory);

        var matcherToDelete = hzMatcherManager(storedCategory, logger()).detachMatcherByPattern(pattern);

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
        logger().info("Detach all matchers from expenditure category: {} on cache", categoryName);
        var storedCategory = getByName(categoryName);
        logger().debug("Detach all matchers, found category: {}", storedCategory);

        var matchersToDelete = hzMatcherManager(storedCategory, logger()).detachAllMatchers();

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
        logger().debug("Setting expenditure category on cache: {}", expenditureCategory);
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
