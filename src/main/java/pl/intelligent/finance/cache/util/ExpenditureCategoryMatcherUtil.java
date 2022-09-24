package pl.intelligent.finance.cache.util;

import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureCategoryMatcher;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpenditureCategoryMatcherUtil {

    public static HazelcastExpenditureCategoryMatcherManager hzMatcherManager(StorableExpenditureCategory category) {
        return new HazelcastExpenditureCategoryMatcherManager(category);
    }

    public static class HazelcastExpenditureCategoryMatcherManager {

        private HazelcastExpenditureCategory category;
        private List<HazelcastExpenditureCategoryMatcher> matchers;

        protected HazelcastExpenditureCategoryMatcherManager(StorableExpenditureCategory category) {
            this.category = (HazelcastExpenditureCategory) category;
            this.matchers = Optional.ofNullable(category.getMatchers())
                    .map(matchers -> matchers.stream()
                        .map(m -> (HazelcastExpenditureCategoryMatcher) m)
                        .collect(Collectors.toList()))
                    .orElse(new ArrayList<>());

            this.category.setMatchers(matchers);
        }

        public void addMatcher(StorableExpenditureCategoryMatcher matcher) {
            matchers.add((HazelcastExpenditureCategoryMatcher) matcher);
        }

        public StorableExpenditureCategoryMatcher detachMatcher(int id) throws InvalidDataException {
            var matcher = matchers.stream()
                    .filter(m -> m.getId().equals(id))
                    .findFirst().orElse(null);

            if (matcher == null) {
                throw ExceptionUtil.expenditureCategoryMatcherNotFound(category.getName(), id);
            }

            matchers.remove(matcher);

            return matcher;
        }

        public StorableExpenditureCategoryMatcher detachMatcherByPattern(String pattern) throws InvalidDataException {
            var matcher = matchers.stream()
                    .filter(m -> m.getPattern().equals(pattern))
                    .findFirst().orElse(null);

            if (matcher == null) {
                throw ExceptionUtil.expenditureCategoryMatcherNotFound(category.getName(), pattern);
            }

            matchers.remove(matcher);

            return matcher;
        }

        public List<? extends StorableExpenditureCategoryMatcher> detachAllMatchers() {
            List<StorableExpenditureCategoryMatcher> matchersBefore = new ArrayList<>(matchers);

            matchers.clear();

            return matchersBefore;
        }

    }

}
