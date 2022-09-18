package pl.intelligent.finance.entity;

public interface IExpenditureCategoryMatcher {

    Integer getId();

    void setId(Integer id);

    String getPattern();

    void setPattern(String pattern);

    ExpenditureCategoryMatcherType getMatcherType();

    void setMatcherType(ExpenditureCategoryMatcherType type);

}
