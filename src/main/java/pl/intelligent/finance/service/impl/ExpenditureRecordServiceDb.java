package pl.intelligent.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;
import pl.intelligent.finance.repository.ExpenditureRecordRepository;
import pl.intelligent.finance.service.IExpenditureRecordService;
import pl.intelligent.finance.service.ServiceBase;

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
        return withException(() -> repository.saveAndFlush((ExpenditureRecord) entity));
    }

    @Transactional
    @Override
    public List<IExpenditureRecord> create(List<IExpenditureRecord> entities) throws Exception {
        return withException(() ->
                mapToInterface(
                        repository.saveAllAndFlush(mapToEntity(entities))
                )
        );
    }

    @Override
    public void delete(IExpenditureRecord entity) {
        repository.delete((ExpenditureRecord) entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public IExpenditureRecord createInstance() {
        return new ExpenditureRecord();
    }

}
