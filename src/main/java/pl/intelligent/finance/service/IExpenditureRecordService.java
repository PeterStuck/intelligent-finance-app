package pl.intelligent.finance.service;

import pl.intelligent.finance.entity.IExpenditureRecord;

import java.util.List;

public interface IExpenditureRecordService {

    IExpenditureRecord findByNameAndBankStatementId(String name, String bankStatementId);

    List<IExpenditureRecord> findByBankStatementId(String bankStatementId);

    IExpenditureRecord create(IExpenditureRecord entity);

    void delete(IExpenditureRecord entity);

    void deleteById(Long id);

    IExpenditureRecord createInstance();

}
