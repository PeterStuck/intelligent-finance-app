package pl.intelligent.finance.cache.subsystem.entity;

public interface StorableExpenditureCategoryMatcher {

    Integer getId();

    String getPattern();

    ExpenditureCategoryMatcherType getMatcherType();

}
