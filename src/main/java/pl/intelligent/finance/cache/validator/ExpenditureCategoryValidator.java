package pl.intelligent.finance.cache.validator;

import pl.intelligent.finance.cache.ExpenditureCategoryStore;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.util.List;

public class ExpenditureCategoryValidator implements ExpenditureCategoryStore {

    private ExpenditureCategoryStore store;

    public ExpenditureCategoryValidator(ExpenditureCategoryStore store) {
        this.store = store;
    }

    @Override
    public List<? extends StorableExpenditureCategory> getAll() {
        return store.getAll();
    }

    @Override
    public StorableExpenditureCategory getByName(String name) {
        return store.getByName(name);
    }

    @Override
    public StorableExpenditureCategory add(StorableExpenditureCategory category) throws InvalidDataException {
        return store.add(category);
    }

    @Override
    public void attachMatcher(StorableExpenditureCategoryMatcher matcher) {
        store.attachMatcher(matcher);
    }

    @Override
    public void detachMatcher(int id) {
        store.detachMatcher(id);
    }

    @Override
    public void detachMatcherByPattern(String pattern) {
        store.detachMatcherByPattern(pattern);
    }

    @Override
    public void detachAllMatchers() {
        store.detachAllMatchers();
    }
}
