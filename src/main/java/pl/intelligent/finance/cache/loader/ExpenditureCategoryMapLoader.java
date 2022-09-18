package pl.intelligent.finance.cache.loader;

import com.hazelcast.map.MapLoader;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.util.ExpenditureCategoryAdapter;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.service.IExpenditureCategoryService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenditureCategoryMapLoader implements MapLoader<Integer, HazelcastExpenditureCategory> {

    private IExpenditureCategoryService service;

    public ExpenditureCategoryMapLoader(IExpenditureCategoryService service) {
        this.service = service;
    }

    @Override
    public HazelcastExpenditureCategory load(Integer id) {
        try {
            IExpenditureCategory categoryDb = service.findById(id);
            if (categoryDb != null) {
                return ExpenditureCategoryAdapter.createExpenditureCategory(categoryDb);
            }
        } catch (Exception e) {
            // TODO log exception
        }
        return null;
    }

    @Override
    public Map<Integer, HazelcastExpenditureCategory> loadAll(Collection<Integer> ids) {
        try {
            if (ids != null && !ids.isEmpty()) {
                List<IExpenditureCategory> categoriesDb = service.findByIds(ids);

                return categoriesDb.stream()
                        .map(ExpenditureCategoryAdapter::createExpenditureCategory)
                        .collect(Collectors.toMap(HazelcastExpenditureCategory::getId, HazelcastExpenditureCategory::getInstance));
            }
        } catch (Exception e) {
            // TODO log exception
        }

        return null;
    }

    @Override
    public Iterable<Integer> loadAllKeys() {
        try {
            return service.findAllIds();
        } catch (Exception e) {
            // TODO log exception
        }

        return null;
    }

}
