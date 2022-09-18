package pl.intelligent.finance.resource.entity;

public interface StorableExpenditureCategoryMatcher {

    Integer getId();

    String getPattern();

    ExpenditureCategoryMatcherType getMatcherType();

}
