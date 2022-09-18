package pl.intelligent.finance.resource.entity;

import java.util.List;

public interface StorableExpenditureCategory {

    Integer getId();

    String getName();

    Integer getParentCategoryId();

    List<? extends StorableExpenditureCategoryMatcher> getMatchers();

}
