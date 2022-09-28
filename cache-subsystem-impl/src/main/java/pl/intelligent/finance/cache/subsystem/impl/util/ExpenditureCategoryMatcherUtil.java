package pl.intelligent.finance.cache.subsystem.impl.util;

import org.slf4j.Logger;
import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureCategory;
import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureCategoryMatcher;
import pl.intelligent.finance.cache.subsystem.exception.ExceptionUtil;
import pl.intelligent.finance.cache.subsystem.exception.InvalidDataException;
import pl.intelligent.finance.cache.subsystem.impl.entity.HazelcastExpenditureCategory;
import pl.intelligent.finance.cache.subsystem.impl.entity.HazelcastExpenditureCategoryMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpenditureCategoryMatcherUtil {

    public static HazelcastExpenditureCategoryMatcherManager hzMatcherManager(StorableExpenditureCategory category, Logger logger) {
        return new HazelcastExpenditureCategoryMatcherManager(category, logger);
    }

    public static class HazelcastExpenditureCategoryMatcherManager {

        private HazelcastExpenditureCategory category;
        private List<HazelcastExpenditureCategoryMatcher> matchers;
        private Logger logger;

        protected HazelcastExpenditureCategoryMatcherManager(StorableExpenditureCategory category, Logger logger) {
            this.category = (HazelcastExpenditureCategory) category;
            this.matchers = Optional.ofNullable(category.getMatchers())
                    .map(matchers -> matchers.stream()
                        .map(m -> (HazelcastExpenditureCategoryMatcher) m)
                        .collect(Collectors.toList()))
                    .orElse(new ArrayList<>());

            this.category.setMatchers(matchers);
            this.logger = logger;
        }

        public void addMatcher(StorableExpenditureCategoryMatcher matcher) {
            matchers.add((HazelcastExpenditureCategoryMatcher) matcher);
            logger.debug("ExpenditureCategoryMatcherManager added matcher: {} to category: {}", matcher, category);
        }

        public StorableExpenditureCategoryMatcher detachMatcher(int id) throws InvalidDataException {
            var matcher = matchers.stream()
                    .filter(m -> m.getId().equals(id))
                    .findFirst().orElse(null);

            logger.debug("ExpenditureCategoryMatcherManager detach matcher: {} from category: {}", matcher, category);

            if (matcher == null) {
                throw ExceptionUtil.expenditureCategoryMatcherNotFound(category.getName(), id);
            }

            matchers.remove(matcher);
            logger.debug("ExpenditureCategoryMatcherManager matcher: {} from category: {} detached", matcher, category);

            return matcher;
        }

        public StorableExpenditureCategoryMatcher detachMatcherByPattern(String pattern) throws InvalidDataException {
            var matcher = matchers.stream()
                    .filter(m -> m.getPattern().equals(pattern))
                    .findFirst().orElse(null);

            logger.debug("ExpenditureCategoryMatcherManager detach matcher by pattern: {} from category: {}", matcher, category);

            if (matcher == null) {
                throw ExceptionUtil.expenditureCategoryMatcherNotFound(category.getName(), pattern);
            }

            matchers.remove(matcher);
            logger.debug("ExpenditureCategoryMatcherManager matcher: {} from category: {} detached", matcher, category);

            return matcher;
        }

        public List<? extends StorableExpenditureCategoryMatcher> detachAllMatchers() {
            List<StorableExpenditureCategoryMatcher> matchersBefore = new ArrayList<>(matchers);

            matchers.clear();
            logger.debug("ExpenditureCategoryMatcherManager all matchers from category: {} has been detached", category);

            return matchersBefore;
        }

    }

}
