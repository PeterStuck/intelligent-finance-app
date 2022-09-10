package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import pl.intelligent.finance.cache.ExpenditureRecordStoreWithService;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.cache.util.ExpenditureRecordAdapter;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;
import pl.intelligent.finance.service.IExpenditureRecordService;
import pl.intelligent.finance.service.provider.ServiceProvider;

import java.util.List;

public class HazelcastExpenditureRecordCache extends HazelcastCacheBase<HazelcastExpenditureRecord> implements ExpenditureRecordStoreWithService {

    private ServiceProvider serviceProvider;

    private static final String CACHE_NAME_PREFIX = "if-";
    public static final String CACHE_NAME = CACHE_NAME_PREFIX + "expenditure-records";

    private IMap<Long, HazelcastExpenditureRecord> expenditureRecordMap;

    public HazelcastExpenditureRecordCache(HazelcastInstance hazelcastInstance, ServiceProvider serviceProvider) {
        this.expenditureRecordMap = hazelcastInstance.getMap(CACHE_NAME);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public StorableExpenditureRecord get(Long id) {
        return expenditureRecordMap.get(id);
    }

    @Override
    public StorableExpenditureRecord add(StorableExpenditureRecord expenditureRecord) throws InvalidDataException {
        var result = attemptOperation(() -> addToService(expenditureRecord));

        if (result.isError()) {
            throw result.exception();
        }

        HazelcastExpenditureRecord cacheRecord = result.cacheEntity();

        addToCache(cacheRecord);

        return cacheRecord;
    }

    private HazelcastExpenditureRecord addToService(StorableExpenditureRecord expenditureRecord) throws Exception {
        IExpenditureRecordService service = serviceProvider.getExpenditureRecordService();
        IExpenditureRecord recordToCreate = ExpenditureRecordAdapter.createExpenditureRecord(expenditureRecord, service);
        IExpenditureRecord persistedRecord = service.create(recordToCreate);
        if (persistedRecord == null) {
            throw ExceptionUtil.entityCreationError("Expenditure Record");
        }

        return ExpenditureRecordAdapter.createExpenditureRecord(persistedRecord);
    }

    private void addToCache(HazelcastExpenditureRecord expenditureRecord) {
        expenditureRecordMap.set(expenditureRecord.getId(), expenditureRecord);
    }

    @Override
    public StorableExpenditureRecord batchAdd(List<StorableExpenditureRecord> expenditureRecords) {
        return null;
    }

    @Override
    public List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId) {
        PredicateBuilder.EntryObject entryObject = Predicates.newPredicateBuilder().getEntryObject();
        Predicate predicate = entryObject.get("bankStatementId").equal(bankStatementId);

        return expenditureRecordMap.values(predicate).stream().toList();

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
