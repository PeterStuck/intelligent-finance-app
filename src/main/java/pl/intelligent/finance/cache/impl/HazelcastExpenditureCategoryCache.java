package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import pl.intelligent.finance.cache.ExpenditureCategoryStoreWithService;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.util.ExpenditureCategoryAdapter;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;
import pl.intelligent.finance.service.provider.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class HazelcastExpenditureCategoryCache extends HazelcastCacheBase<HazelcastExpenditureCategory> implements ExpenditureCategoryStoreWithService {

    public static final String CACHE_NAME = CACHE_NAME_PREFIX + "expenditure-categories";

    private IMap<Integer, HazelcastExpenditureCategory> expenditureCategoryMap;

    public HazelcastExpenditureCategoryCache(HazelcastInstance hazelcastInstance, ServiceProvider serviceProvider) {
        super(serviceProvider);
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

        addToCache(expenditureCategory);

        return expenditureCategory;
    }

    private HazelcastExpenditureCategory addToService(StorableExpenditureCategory category) throws Exception {
        IExpenditureCategoryService categoryService = serviceProvider.getExpenditureCategoryService();
        IExpenditureCategoryMatcherService matcherService = serviceProvider.getExpenditureCategoryMatcherService();
        IExpenditureCategory categoryToCreate = ExpenditureCategoryAdapter.createExpenditureCategory(categoryService, matcherService, category);
        IExpenditureCategory persistedCategory = categoryService.create(categoryToCreate);
        if (persistedCategory == null) {
            throw ExceptionUtil.entityCreationError("Expenditure Category");
        }

        return ExpenditureCategoryAdapter.createExpenditureCategory(persistedCategory);
    }

    private void addToCache(HazelcastExpenditureCategory expenditureCategory) {
        expenditureCategoryMap.set(expenditureCategory.getId(), expenditureCategory);
    }

    @Override
    public void attachMatcher(StorableExpenditureCategoryMatcher matcher) {

    }

    @Override
    public void detachMatcher(int id) {

    }

    @Override
    public void detachMatcherByPattern(String pattern) {

    }

    @Override
    public void detachAllMatchers() {

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

}
