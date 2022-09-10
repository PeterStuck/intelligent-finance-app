package pl.intelligent.finance.service;

import pl.intelligent.finance.entity.IExpenditureRecord;

import java.util.Collection;
import java.util.List;

public interface IExpenditureRecordService {

    IExpenditureRecord findById(Long id);

    List<IExpenditureRecord> findByIds(Collection<Long> ids);

    List<Long> findAllIds();

    IExpenditureRecord findByNameAndBankStatementId(String name, String bankStatementId);

    List<IExpenditureRecord> findByBankStatementId(String bankStatementId);

    IExpenditureRecord create(IExpenditureRecord entity) throws Exception;

    List<IExpenditureRecord> create(List<IExpenditureRecord> entities) throws Exception;

    void delete(IExpenditureRecord entity);

    void deleteById(Long id);

    IExpenditureRecord createInstance();

}
