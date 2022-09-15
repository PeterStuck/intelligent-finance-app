package pl.intelligent.finance.entity;

import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcherType;

public interface IExpenditureCategoryMatcher {

    Integer getId();

    void setId(Integer id);

    String getPattern();

    void setPattern(String pattern);

    ExpenditureCategoryMatcherType getMatcherType();

    void setMatcherType(ExpenditureCategoryMatcherType type);

}
