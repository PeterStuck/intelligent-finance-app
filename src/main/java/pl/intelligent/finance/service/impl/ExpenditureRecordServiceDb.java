package pl.intelligent.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.intelligent.finance.persistence.entity.IExpenditureRecord;
import pl.intelligent.finance.persistence.entity.impl.ExpenditureRecord;
import pl.intelligent.finance.persistence.repository.ExpenditureRecordRepository;
import pl.intelligent.finance.service.IExpenditureRecordService;

import java.util.Collection;
import java.util.List;

@Service
public class ExpenditureRecordServiceDb extends ServiceBase<IExpenditureRecord, ExpenditureRecord> implements IExpenditureRecordService {

    @Autowired
    private ExpenditureRecordRepository repository;

    @Override
    public IExpenditureRecord findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<IExpenditureRecord> findByIds(Collection<Long> ids) {
        return mapToInterface(repository.findAllById(ids));
    }

    @Override
    public List<Long> findAllIds() {
        return repository.findAllIds();
    }

    @Override
    public IExpenditureRecord findByNameAndBankStatementId(String name, String bankStatementId) {
        return repository.findByNameAndBankStatementId(name, bankStatementId);
    }

    @Override
    public List<IExpenditureRecord> findByBankStatementId(String bankStatementId) {
        return mapToInterface(repository.findByBankStatementId(bankStatementId));
    }

    @Override
    public IExpenditureRecord create(IExpenditureRecord entity) throws Exception {
        return withExceptionHandler(() -> {
            logger().debug("Persisting expenditure record: {}", entity);

            var persistedRecord = repository.saveAndFlush((ExpenditureRecord) entity);
            logger().debug("Persisted expenditure record: {}", persistedRecord);

            return persistedRecord;
        });
    }

    @Transactional
    @Override
    public List<IExpenditureRecord> create(List<IExpenditureRecord> entities) throws Exception {
        return withExceptionHandler(() -> {
            logger().debug("Batch persisting expenditure records: {}", entities);

            var persistedRecords = mapToInterface(
                        repository.saveAllAndFlush(mapToEntity(entities)));
            logger().debug("Batch persisted expenditure records: {}", persistedRecords);

            return persistedRecords;
        });
    }

    @Override
    public void delete(IExpenditureRecord entity) {
        logger().debug("Deleting expenditure record: {}", entity);

        repository.delete((ExpenditureRecord) entity);

        logger().debug("Expenditure record: {} deleted successfully", entity);
    }

    @Override
    public void deleteById(Long id) {
        logger().debug("Deleting expenditure record with id: {}", id);

        repository.deleteById(id);

        logger().debug("Expenditure record with id: {} deleted successfully", id);
    }

    @Override
    public IExpenditureRecord createInstance() {
        return new ExpenditureRecord();
    }

}
