package pl.intelligent.finance.cache.util;

import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.entity.IExpenditureRecord;

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

}
