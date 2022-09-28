package pl.intelligent.finance.cache.subsystem.store;

import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureCategory;
import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureCategoryMatcher;
import pl.intelligent.finance.cache.subsystem.exception.InvalidDataException;

import java.util.List;

public interface ExpenditureCategoryStore {

    List<? extends StorableExpenditureCategory> getAll();

    StorableExpenditureCategory getByName(String name);

    StorableExpenditureCategory add(StorableExpenditureCategory category) throws InvalidDataException;

    boolean attachMatcher(String categoryName, StorableExpenditureCategoryMatcher matcher) throws InvalidDataException;

    boolean detachMatcher(String categoryName, int matcherId) throws InvalidDataException;

    boolean detachMatcherByPattern(String categoryName, String pattern) throws InvalidDataException;

    boolean detachAllMatchers(String categoryName) throws InvalidDataException;

}
