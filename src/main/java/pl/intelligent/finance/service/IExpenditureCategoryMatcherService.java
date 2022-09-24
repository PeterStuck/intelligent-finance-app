package pl.intelligent.finance.service;

import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;

import java.util.List;

public interface IExpenditureCategoryMatcherService {

    IExpenditureCategoryMatcher createInstance();

    void deleteById(IExpenditureCategory category, int id);

    void delete(IExpenditureCategory category, IExpenditureCategoryMatcher matcher);

    void deleteAllByIds(IExpenditureCategory category, List<Integer> ids);

}
