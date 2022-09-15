package pl.intelligent.finance.service;

import pl.intelligent.finance.entity.IExpenditureCategory;

import java.util.Collection;
import java.util.List;

public interface IExpenditureCategoryService {

    IExpenditureCategory findById(Integer id);

    List<IExpenditureCategory> findByIds(Collection<Integer> ids);

    List<Integer> findAllIds();

    IExpenditureCategory findByName(String name);

    IExpenditureCategory create(IExpenditureCategory category) throws Exception;

    void delete(IExpenditureCategory category);

    void deleteById(Integer id);

    IExpenditureCategory createInstance();

}
