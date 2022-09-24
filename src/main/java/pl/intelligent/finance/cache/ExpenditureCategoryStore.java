package pl.intelligent.finance.cache;

import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

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
