package pl.intelligent.finance.entity;

import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcherType;

public interface IExpenditureCategoryMatcher {

    Integer getId();

    String getPattern();

    ExpenditureCategoryMatcherType getMatcherType();

}
