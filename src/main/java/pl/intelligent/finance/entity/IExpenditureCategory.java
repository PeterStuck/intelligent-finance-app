package pl.intelligent.finance.entity;

import java.util.List;

public interface IExpenditureCategory {

    Integer getId();

    void setId(Integer id);

    String getName();

    void setName(String name);

    Integer getParentCategoryId();

    void setParentCategoryId(Integer parentCategoryId);

    List<IExpenditureCategoryMatcher> getMatchers();

    void addMatcher(IExpenditureCategoryMatcher matcher);

}
