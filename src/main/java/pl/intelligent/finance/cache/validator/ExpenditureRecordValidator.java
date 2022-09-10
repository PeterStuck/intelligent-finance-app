package pl.intelligent.finance.cache.validator;

import pl.intelligent.finance.cache.ExpenditureRecordStore;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;

import java.util.List;

public class ExpenditureRecordValidator implements ExpenditureRecordStore {

    private ExpenditureRecordStore recordStore;

    public ExpenditureRecordValidator(ExpenditureRecordStore recordStore) {
        this.recordStore = recordStore;
    }

    @Override
    public StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord) {
        // TODO some validation logic
        return recordStore.add(expenditureRecord);
    }

    @Override
    public List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId) {
        return recordStore.getAllByBankStatementId(bankStatementId);
    }

}
