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
        return withExceptionHandler(() -> {
            logger().debug("Persisting expenditure category: {}", category);

            var persistedCategory = repository.saveAndFlush((ExpenditureCategory) category);
            logger().debug("Expenditure category persisted: {}", persistedCategory);

            return persistedCategory;
        });
    }

    @Override
    public IExpenditureCategory update(IExpenditureCategory category) throws Exception {
        return withExceptionHandler(() -> {
            logger().debug("Updating expenditure category: {}", category);

            var updatedCategory = repository.saveAndFlush((ExpenditureCategory) category);
            logger().debug("Expenditure category updated: {}", updatedCategory);

            return updatedCategory;
        });
    }

    @Override
    public void delete(IExpenditureCategory category) {
        logger().debug("Deleting expenditure category: {}", category);

        repository.delete((ExpenditureCategory) category);

        logger().debug("Expenditure category: {} deleted", category.getName());
    }

    @Override
    public void deleteById(Integer id) {
        logger().debug("Deleting expenditure category with id: {}", id);

        repository.deleteById(id);

        logger().debug("Expenditure category with id: {} deleted", id);
    }

    @Override
    public IExpenditureCategory createInstance() {
        return new ExpenditureCategory();
    }
}
