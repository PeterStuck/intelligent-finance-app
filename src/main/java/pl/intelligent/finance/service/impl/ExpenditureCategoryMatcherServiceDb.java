package pl.intelligent.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.intelligent.finance.persistence.entity.IExpenditureCategory;
import pl.intelligent.finance.persistence.entity.IExpenditureCategoryMatcher;
import pl.intelligent.finance.persistence.entity.impl.ExpenditureCategory;
import pl.intelligent.finance.persistence.entity.impl.ExpenditureCategoryMatcher;
import pl.intelligent.finance.persistence.repository.ExpenditureCategoryMatcherRepository;
import pl.intelligent.finance.persistence.repository.ExpenditureCategoryRepository;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;

import java.util.List;

@Service
public class ExpenditureCategoryMatcherServiceDb extends ServiceBase<IExpenditureCategoryMatcher, ExpenditureCategoryMatcher>
        implements IExpenditureCategoryMatcherService {

    @Autowired
    private ExpenditureCategoryMatcherRepository matcherRepository;

    @Autowired
    private ExpenditureCategoryRepository categoryRepository;

    @Override
    public IExpenditureCategoryMatcher createInstance() {
        return new ExpenditureCategoryMatcher();
    }

    @Transactional
    @Override
    public void deleteById(IExpenditureCategory category, int id) {
        logger().debug("Deleting expenditure category matcher by id: {}", id);

        matcherRepository.deleteById(id);
        categoryRepository.saveAndFlush((ExpenditureCategory) category);
        logger().debug("Expenditure category matcher with id: {} was successfully deleted", id);
    }

    @Transactional
    @Override
    public void delete(IExpenditureCategory category, IExpenditureCategoryMatcher matcher) {
        logger().debug("Deleting expenditure category matcher: {}", matcher);

        matcherRepository.delete((ExpenditureCategoryMatcher) matcher);
        categoryRepository.saveAndFlush((ExpenditureCategory) category);
        logger().debug("Expenditure category matcher: {} was successfully deleted", matcher);
    }

    @Transactional
    @Override
    public void deleteAllByIds(IExpenditureCategory category, List<Integer> ids) {
        logger().debug("Deleting expenditure category matcher by ids: {}", ids);

        matcherRepository.deleteAllById(ids);
        categoryRepository.saveAndFlush((ExpenditureCategory) category);

        logger().debug("Expenditure category matchers with ids: {} was successfully deleted", ids);
    }

}
