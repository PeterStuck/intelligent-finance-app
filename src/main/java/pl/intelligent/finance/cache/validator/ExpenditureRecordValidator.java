package pl.intelligent.finance.cache.validator;

import pl.intelligent.finance.cache.ExpenditureRecordStore;
import pl.intelligent.finance.cache.ExpenditureRecordStoreWithService;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;

import java.util.List;

public class ExpenditureRecordValidator implements ExpenditureRecordStore {

    private ExpenditureRecordStoreWithService recordStore;

    public ExpenditureRecordValidator(ExpenditureRecordStoreWithService recordStore) {
        this.recordStore = recordStore;
    }

    @Override
    public StorableExpenditureRecord get(Long id) {
        return recordStore.get(id);
    }

    @Override
    public StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord) throws InvalidDataException {
        // TODO some validation logic
        return recordStore.add(expenditureRecord);
    }

    @Override
    public List<StorableExpenditureRecord> batchAdd(List<StorableExpenditureRecord> expenditureRecords) throws InvalidDataException {
        return recordStore.batchAdd(expenditureRecords);
    }

    @Override
    public List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId) {
        return recordStore.getAllByBankStatementId(bankStatementId);
    }

}
