package pl.intelligent.finance.cache.util;

import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.persistence.entity.IExpenditureRecord;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;
import pl.intelligent.finance.service.IExpenditureRecordService;

public class ExpenditureRecordAdapter {

    public static HazelcastExpenditureRecord createExpenditureRecord(IExpenditureRecord recordDb) {
        if (recordDb == null) {
            return null;
        }

        return HazelcastExpenditureRecord.builder()
                .id(recordDb.getId())
                .name(recordDb.getName())
                .bankStatementId(recordDb.getBankStatementId())
                .amount(recordDb.getAmount())
                .categoryId(recordDb.getCategoryId())
                .build();
    }

    public static IExpenditureRecord createExpenditureRecord(IExpenditureRecordService service, StorableExpenditureRecord record) {
        if (record == null) {
            return null;
        }

        IExpenditureRecord recordDb = service.createInstance();
        recordDb.setId(record.getId());
        recordDb.setName(record.getName());
        recordDb.setAmount(record.getAmount());
        recordDb.setCategoryId(record.getCategoryId());
        recordDb.setBankStatementId(record.getBankStatementId());

        return recordDb;
    }

}
