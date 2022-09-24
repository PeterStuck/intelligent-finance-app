package pl.intelligent.finance.persistence.entity;

import java.util.List;

public interface IExpenditureCategory {

    Integer getId();

    void setId(Integer id);

    String getName();

    void setName(String name);

    Integer getParentCategoryId();

    void setParentCategoryId(Integer parentCategoryId);

    List<? extends IExpenditureCategoryMatcher> getMatchers();

    void setMatchers(List<? extends IExpenditureCategoryMatcher> matchers);

}
