package pl.intelligent.finance.cache.subsystem.store;

import pl.intelligent.finance.cache.subsystem.entity.StorableExpenditureRecord;
import pl.intelligent.finance.cache.subsystem.exception.InvalidDataException;

import java.util.List;

public interface ExpenditureRecordStore {

    StorableExpenditureRecord get(Long id);

    StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord) throws InvalidDataException;

    List<StorableExpenditureRecord> batchAdd(List<StorableExpenditureRecord> expenditureRecords) throws InvalidDataException;

    List<StorableExpenditureRecord> getAllByBankStatementId(Integer bankStatementId);

}
