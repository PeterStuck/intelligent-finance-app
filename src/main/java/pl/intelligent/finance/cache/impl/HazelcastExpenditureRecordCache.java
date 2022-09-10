package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import pl.intelligent.finance.cache.ExpenditureRecordStore;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;
import pl.intelligent.finance.service.provider.ServiceProvider;

import java.util.List;

public class HazelcastExpenditureRecordCache implements ExpenditureRecordStore {

    private ServiceProvider serviceProvider;

    private static final String CACHE_NAME_PREFIX = "if-";
    public static final String CACHE_NAME = CACHE_NAME_PREFIX + "expenditure-records";

    private IMap<Long, HazelcastExpenditureRecord> expenditureRecordMap;

    public HazelcastExpenditureRecordCache(HazelcastInstance hazelcastInstance, ServiceProvider serviceProvider) {
        this.expenditureRecordMap = hazelcastInstance.getMap(CACHE_NAME);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord) {
        return null;
    }

    @Override
    public List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId) {
        PredicateBuilder.EntryObject entryObject = Predicates.newPredicateBuilder().getEntryObject();
        Predicate predicate = entryObject.get("bankStatementId").equal(bankStatementId);

        return expenditureRecordMap.values(predicate).stream().toList();

    }
}
