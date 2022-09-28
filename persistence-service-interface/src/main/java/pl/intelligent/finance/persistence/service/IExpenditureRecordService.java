package pl.intelligent.finance.persistence.service;

import pl.intelligent.finance.persistence.entity.IExpenditureRecord;

import java.util.Collection;
import java.util.List;

public interface IExpenditureRecordService {

    IExpenditureRecord findById(Long id);

    List<IExpenditureRecord> findByIds(Collection<Long> ids);

    List<Long> findAllIds();

    IExpenditureRecord findByNameAndBankStatementId(String name, Integer bankStatementId);

    List<IExpenditureRecord> findByBankStatementId(Integer bankStatementId);

    IExpenditureRecord create(IExpenditureRecord entity) throws Exception;

    List<IExpenditureRecord> create(List<IExpenditureRecord> entities) throws Exception;

    void delete(IExpenditureRecord entity);

    void deleteById(Long id);

    IExpenditureRecord createInstance();

}
