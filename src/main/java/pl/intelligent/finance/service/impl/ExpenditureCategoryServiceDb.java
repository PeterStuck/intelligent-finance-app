package pl.intelligent.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.impl.ExpenditureCategory;
import pl.intelligent.finance.repository.ExpenditureCategoryRepository;
import pl.intelligent.finance.service.IExpenditureCategoryService;

import java.util.Collection;
import java.util.List;

@Service
public class ExpenditureCategoryServiceDb extends ServiceBase<IExpenditureCategory, ExpenditureCategory> implements IExpenditureCategoryService {

    @Autowired
    private ExpenditureCategoryRepository repository;

    @Override
    public IExpenditureCategory findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<IExpenditureCategory> findByIds(Collection<Integer> ids) {
        return mapToInterface(repository.findAllById(ids));
    }

    @Override
    public List<Integer> findAllIds() {
        return repository.findAllIds();
    }

    @Override
    public IExpenditureCategory findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public IExpenditureCategory create(IExpenditureCategory category) throws Exception {
        return withException(() -> repository.saveAndFlush((ExpenditureCategory) category));
    }

    @Override
    public IExpenditureCategory update(IExpenditureCategory category) throws Exception {
        return withException(() -> repository.saveAndFlush((ExpenditureCategory) category));
    }

    @Override
    public void delete(IExpenditureCategory category) {
        repository.delete((ExpenditureCategory) category);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public IExpenditureCategory createInstance() {
        return new ExpenditureCategory();
    }
}
