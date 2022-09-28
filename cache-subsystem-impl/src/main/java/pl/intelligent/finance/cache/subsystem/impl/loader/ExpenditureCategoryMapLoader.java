package pl.intelligent.finance.cache.subsystem.impl.loader;

import com.hazelcast.map.MapLoader;
import pl.intelligent.finance.cache.subsystem.impl.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.subsystem.impl.util.ExpenditureCategoryAdapter;
import pl.intelligent.finance.persistence.entity.IExpenditureCategory;
import pl.intelligent.finance.persistence.service.IExpenditureCategoryService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenditureCategoryMapLoader extends MapLoaderBase<Integer, HazelcastExpenditureCategory> implements MapLoader<Integer, HazelcastExpenditureCategory> {

    private IExpenditureCategoryService service;

    public ExpenditureCategoryMapLoader(IExpenditureCategoryService service) {
        this.service = service;
    }

    @Override
    public HazelcastExpenditureCategory load(Integer id) {
        return loadWithExceptionHandler(() -> {
            logger().debug("Loading expenditure category by id: {}", id);

            IExpenditureCategory categoryDb = service.findById(id);
            logger().debug("Loaded expenditure category: {} by id: {}", categoryDb, id);

            return ExpenditureCategoryAdapter.createExpenditureCategory(categoryDb);
        });
    }

    @Override
    public Map<Integer, HazelcastExpenditureCategory> loadAll(Collection<Integer> ids) {
        return loadAllWithExceptionHandler(() -> {
            logger().debug("Loading all expenditure categories by ids: {}", ids);

            List<IExpenditureCategory> categoriesDb = service.findByIds(ids);
            logger().debug("Loaded expenditure categories: {} by ids: {}", categoriesDb.size(), ids);

            return categoriesDb.stream()
                    .map(ExpenditureCategoryAdapter::createExpenditureCategory)
                    .collect(Collectors.toMap(HazelcastExpenditureCategory::getId, HazelcastExpenditureCategory::getInstance));
        });
    }

    @Override
    public Iterable<Integer> loadAllKeys() {
        return loadAllKeysWithExceptionHandler(() -> {
            logger().debug("Loading all expenditure category ids");

            List<Integer> allIds = service.findAllIds();
            logger().debug("Loaded expenditure category ids: {}", allIds);

            return allIds;
        });
    }

}
