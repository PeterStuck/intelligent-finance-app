package pl.intelligent.finance.entity;

public interface IExpenditureCategory {

    Integer getId();

    String getName();

    Integer getParentCategoryId();

    // TODO list of matchers

}
