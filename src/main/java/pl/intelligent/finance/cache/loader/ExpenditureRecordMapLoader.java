package pl.intelligent.finance.cache.loader;

import com.hazelcast.map.MapLoader;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.cache.util.ExpenditureRecordAdapter;
import pl.intelligent.finance.persistence.entity.IExpenditureRecord;
import pl.intelligent.finance.service.IExpenditureRecordService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenditureRecordMapLoader extends MapLoaderBase<Long, HazelcastExpenditureRecord> implements MapLoader<Long, HazelcastExpenditureRecord> {

    private IExpenditureRecordService service;

    public ExpenditureRecordMapLoader(IExpenditureRecordService service) {
        this.service = service;
    }

    @Override
    public HazelcastExpenditureRecord load(Long id) {
        return loadWithExceptionHandler(() -> {
            logger().debug("Load expenditure record by id: {}", id);

            IExpenditureRecord recordDb = service.findById(id);
            logger().debug("Loaded expenditure record: {} by id: {}", recordDb, id);

            return ExpenditureRecordAdapter.createExpenditureRecord(recordDb);
        });
    }

    @Override
    public Map<Long, HazelcastExpenditureRecord> loadAll(Collection<Long> ids) {
        return loadAllWithExceptionHandler(() -> {
            logger().debug("Load all expenditure records by ids: {}", ids);

            List<IExpenditureRecord> recordsDb = service.findByIds(ids);
            logger().debug("Loaded expenditure records: {} by ids: {}", recordsDb.size(), ids);

            return recordsDb.stream()
                    .map(ExpenditureRecordAdapter::createExpenditureRecord)
                    .collect(Collectors.toMap(HazelcastExpenditureRecord::getId, HazelcastExpenditureRecord::getInstance));
        });
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        return loadAllKeysWithExceptionHandler(() -> {
            logger().debug("Load all expenditure record keys");

            List<Long> allIds = service.findAllIds();
            logger().debug("Loaded all expenditure record keys: {}", allIds);

            return allIds;
        });
    }

}
