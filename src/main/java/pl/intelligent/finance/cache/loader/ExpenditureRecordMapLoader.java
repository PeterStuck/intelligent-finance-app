package pl.intelligent.finance.cache.loader;

import com.hazelcast.map.MapLoader;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;
import pl.intelligent.finance.cache.util.ExpenditureRecordAdapter;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.service.IExpenditureRecordService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenditureRecordMapLoader implements MapLoader<Long, HazelcastExpenditureRecord> {

    private IExpenditureRecordService service;

    public ExpenditureRecordMapLoader(IExpenditureRecordService service) {
        this.service = service;
    }

    @Override
    public HazelcastExpenditureRecord load(Long id) {
        try {
            IExpenditureRecord recordDb = service.findById(id);
            if (recordDb != null) {
                return ExpenditureRecordAdapter.createExpenditureRecord(recordDb);
            }
        } catch (Exception e) {
            // TODO log exception
        }
        return null;
    }

    @Override
    public Map<Long, HazelcastExpenditureRecord> loadAll(Collection<Long> ids) {
        try {
            if (ids != null && !ids.isEmpty()) {
                List<IExpenditureRecord> recordsDb = service.findByIds(ids);

                return recordsDb.stream()
                        .map(ExpenditureRecordAdapter::createExpenditureRecord)
                        .collect(Collectors.toMap(HazelcastExpenditureRecord::getId, HazelcastExpenditureRecord::getInstance));
            }
        } catch (Exception e) {
            // TODO log exception
        }

        return null;
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        try {
            return service.findAllIds();
        } catch (Exception e) {
            // TODO log exception
        }

        return null;
    }

}
