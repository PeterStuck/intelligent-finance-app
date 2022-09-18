package pl.intelligent.finance.cache;

import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.util.List;

public interface ExpenditureCategoryStore {

    List<? extends StorableExpenditureCategory> getAll();

    StorableExpenditureCategory getByName(String name);

    StorableExpenditureCategory add(StorableExpenditureCategory category) throws InvalidDataException;

    void attachMatcher(StorableExpenditureCategoryMatcher matcher);

    void detachMatcher(int id);

    void detachMatcherByPattern(String pattern);

    void detachAllMatchers();

}
