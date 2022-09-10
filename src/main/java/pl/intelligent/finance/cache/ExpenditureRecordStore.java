package pl.intelligent.finance.cache;

import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;

import java.util.List;

public interface ExpenditureRecordStore {

    StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord);

    List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId);

}
