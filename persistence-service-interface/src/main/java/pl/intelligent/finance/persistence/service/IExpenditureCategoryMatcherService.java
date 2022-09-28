package pl.intelligent.finance.persistence.service;

import pl.intelligent.finance.persistence.entity.IExpenditureCategory;
import pl.intelligent.finance.persistence.entity.IExpenditureCategoryMatcher;

import java.util.List;

public interface IExpenditureCategoryMatcherService {

    IExpenditureCategoryMatcher createInstance();

    void deleteById(IExpenditureCategory category, int id);

    void delete(IExpenditureCategory category, IExpenditureCategoryMatcher matcher);

    void deleteAllByIds(IExpenditureCategory category, List<Integer> ids);

}
