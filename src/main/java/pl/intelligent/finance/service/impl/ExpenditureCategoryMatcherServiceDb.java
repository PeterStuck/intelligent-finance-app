package pl.intelligent.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.impl.ExpenditureCategory;
import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcher;
import pl.intelligent.finance.repository.ExpenditureCategoryMatcherRepository;
import pl.intelligent.finance.repository.ExpenditureCategoryRepository;
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
        matcherRepository.deleteById(id);
        categoryRepository.saveAndFlush((ExpenditureCategory) category);
    }

    @Transactional
    @Override
    public void delete(IExpenditureCategory category, IExpenditureCategoryMatcher matcher) {
        matcherRepository.delete((ExpenditureCategoryMatcher) matcher);
        categoryRepository.saveAndFlush((ExpenditureCategory) category);
    }

    @Transactional
    @Override
    public void deleteAllByIds(IExpenditureCategory category, List<Integer> ids) {
        matcherRepository.deleteAllById(ids);
        categoryRepository.saveAndFlush((ExpenditureCategory) category);
    }

}
