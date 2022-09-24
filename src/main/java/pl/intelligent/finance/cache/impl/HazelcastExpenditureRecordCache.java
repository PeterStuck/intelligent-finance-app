package pl.intelligent.finance.cache.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import pl.intelligent.finance.cache.ExpenditureRecordStore;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;
import pl.intelligent.finance.service.IExpenditureRecordService;
import pl.intelligent.finance.service.ServiceProvider;

import java.util.List;

import static pl.intelligent.finance.cache.util.ExpenditureRecordAdapter.createExpenditureRecord;

public class HazelcastExpenditureRecordCache extends HazelcastCacheBase<HazelcastExpenditureRecord, IExpenditureRecord> implements ExpenditureRecordStore {

    public static final String CACHE_NAME = CACHE_NAME_PREFIX + "expenditure-records";

    private IMap<Long, HazelcastExpenditureRecord> expenditureRecordMap;

    private IExpenditureRecordService expenditureRecordService;

    public HazelcastExpenditureRecordCache(HazelcastInstance hazelcastInstance, ServiceProvider serviceProvider) {
        super(serviceProvider);
        this.expenditureRecordService = serviceProvider.getExpenditureRecordService();
        this.expenditureRecordMap = hazelcastInstance.getMap(CACHE_NAME);
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

        setOnCache(cacheRecord);

        return cacheRecord;
    }

    private HazelcastExpenditureRecord addToService(StorableExpenditureRecord expenditureRecord) throws Exception {
        IExpenditureRecord recordToCreate = getConvertedDbEntity((HazelcastExpenditureRecord) expenditureRecord);
        IExpenditureRecord persistedRecord = expenditureRecordService.create(recordToCreate);
        if (persistedRecord == null) {
            throw ExceptionUtil.entityCreationError("Expenditure Record");
        }

        return getConvertedCacheEntity(persistedRecord);
    }

    @Override
    public List<StorableExpenditureRecord> batchAdd(List<StorableExpenditureRecord> expenditureRecords) throws InvalidDataException {
        var result = attemptBatchOperation(() -> batchAddToService(expenditureRecords));

        if (result.isError()) {
            throw result.exception();
        }

        List<HazelcastExpenditureRecord> cacheRecords = result.entities();

        cacheRecords.forEach(this::setOnCache);

        return cacheRecords.stream()
                .map(er -> (StorableExpenditureRecord) er)
                .toList();
    }

    private List<HazelcastExpenditureRecord> batchAddToService(List<StorableExpenditureRecord> expenditureRecords) throws Exception {
        List<IExpenditureRecord> mappedEntities = expenditureRecords.stream()
                .map(er -> getConvertedDbEntity((HazelcastExpenditureRecord) er))
                .toList();

        List<IExpenditureRecord> persistedRecords = expenditureRecordService.create(mappedEntities);
        if (persistedRecords == null || persistedRecords.isEmpty()) {
            throw ExceptionUtil.entityCreationError("Expenditure record");
        }

        return persistedRecords.stream()
                .map(this::getConvertedCacheEntity)
                .toList();
    }

    private void setOnCache(HazelcastExpenditureRecord expenditureRecord) {
        expenditureRecordMap.set(expenditureRecord.getId(), expenditureRecord);
    }

    @Override
    public List<StorableExpenditureRecord> getAllByBankStatementId(String bankStatementId) {
        PredicateBuilder.EntryObject entryObject = Predicates.newPredicateBuilder().getEntryObject();
        Predicate predicate = entryObject.get("bankStatementId").equal(bankStatementId);

        return expenditureRecordMap.values(predicate).stream().toList();
    }

    @Override
    protected HazelcastExpenditureRecord getConvertedCacheEntity(IExpenditureRecord entity) {
        return createExpenditureRecord(entity);
    }

    @Override
    protected IExpenditureRecord getConvertedDbEntity(HazelcastExpenditureRecord entity) {
        return createExpenditureRecord(expenditureRecordService, entity);
    }
}
