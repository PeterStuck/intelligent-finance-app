package pl.intelligent.finance.cache.util;

import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;
import pl.intelligent.finance.resource.entity.ExpenditureCategoryMatcherType;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpenditureCategoryAdapter {

    public static HazelcastExpenditureCategory createExpenditureCategory(IExpenditureCategory categoryDb) {
        if (categoryDb == null) {
            return null;
        }

        return HazelcastExpenditureCategory.builder()
                .id(categoryDb.getId())
                .name(categoryDb.getName())
                .parentCategoryId(categoryDb.getParentCategoryId())
                .matchers(createExpenditureCategoryMatchers(categoryDb.getMatchers()))
                .build();
    }

    private static List<HazelcastExpenditureCategoryMatcher> createExpenditureCategoryMatchers(List<? extends IExpenditureCategoryMatcher> matchersDb) {
        if (matchersDb == null || matchersDb.isEmpty()) {
            return Collections.emptyList();
        }

        return matchersDb.stream()
                .map(ExpenditureCategoryAdapter::createExpenditureCategoryMatcher)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static HazelcastExpenditureCategoryMatcher createExpenditureCategoryMatcher(IExpenditureCategoryMatcher matcherDb) {
        if (matcherDb == null) {
            return null;
        }

        return HazelcastExpenditureCategoryMatcher.builder()
                .id(matcherDb.getId())
                .pattern(matcherDb.getPattern())
                .matcherType(ExpenditureCategoryMatcherType.valueOf(matcherDb.getMatcherType().getId()))
                .build();
    }

    public static IExpenditureCategory createExpenditureCategory(final IExpenditureCategoryService categoryService,
                                                                 final IExpenditureCategoryMatcherService matcherService,
                                                                 final StorableExpenditureCategory storedCategory) {
        if (categoryService == null || matcherService == null) {
            return null;
        }

        IExpenditureCategory instance = categoryService.createInstance();
        instance.setId(storedCategory.getId());
        instance.setName(storedCategory.getName());
        instance.setParentCategoryId(storedCategory.getParentCategoryId());
        instance.setMatchers(createExpenditureCategoryMatchers(matcherService, storedCategory));

        return instance;
    }

    private static List<? extends IExpenditureCategoryMatcher> createExpenditureCategoryMatchers(IExpenditureCategoryMatcherService matcherService,
                                                                                                 StorableExpenditureCategory storedCategory) {
        return storedCategory.getMatchers()
                .stream()
                .map(storedMatcher -> createExpenditureCategoryMatcher(matcherService, storedMatcher))
                .collect(Collectors.toList());
    }

    public static IExpenditureCategoryMatcher createExpenditureCategoryMatcher(IExpenditureCategoryMatcherService matcherService,
                                                                                StorableExpenditureCategoryMatcher storedMatcher) {
        var matcherDb = matcherService.createInstance();
        matcherDb.setId(storedMatcher.getId());
        matcherDb.setPattern(storedMatcher.getPattern());
        matcherDb.setMatcherType(pl.intelligent.finance.entity.ExpenditureCategoryMatcherType.valueOf(storedMatcher.getMatcherType().getId()));
        return matcherDb;
    }

}
