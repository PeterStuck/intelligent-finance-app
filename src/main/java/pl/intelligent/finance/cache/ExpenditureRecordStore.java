package pl.intelligent.finance.cache;

import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;

import java.util.List;

public interface ExpenditureRecordStore {

    StorableExpenditureRecord get(Long id);

    StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord) throws InvalidDataException;

    List<StorableExpenditureRecord> batchAdd(List<StorableExpenditureRecord> expenditureRecords) throws InvalidDataException;

    List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId);

}
