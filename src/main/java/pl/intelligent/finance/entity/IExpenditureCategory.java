package pl.intelligent.finance.entity;

import java.util.List;

public interface IExpenditureCategory {

    Integer getId();

    String getName();

    Integer getParentCategoryId();

    List<IExpenditureCategoryMatcher> getMatchers();

}
