package pl.intelligent.finance.cache.validation;

import pl.intelligent.finance.cache.ExpenditureCategoryStore;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.util.List;

public class ExpenditureCategoryStoreWithValidation implements ExpenditureCategoryStore {

    private ExpenditureCategoryStore store;

    public ExpenditureCategoryStoreWithValidation(ExpenditureCategoryStore store) {
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
    public boolean attachMatcher(String categoryName, StorableExpenditureCategoryMatcher matcher) throws InvalidDataException {
        return store.attachMatcher(null, matcher);
    }

    @Override
    public boolean detachMatcher(String categoryName, int matcherId) throws InvalidDataException {
        return store.detachMatcher(null, matcherId);
    }

    @Override
    public boolean detachMatcherByPattern(String categoryName, String pattern) throws InvalidDataException {
        return store.detachMatcherByPattern(null, pattern);
    }

    @Override
    public boolean detachAllMatchers(String categoryName) throws InvalidDataException {
        return store.detachAllMatchers(null);
    }
}
