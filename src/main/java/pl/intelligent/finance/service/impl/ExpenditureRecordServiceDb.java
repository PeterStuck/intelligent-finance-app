package pl.intelligent.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.intelligent.finance.entity.IExpenditureRecord;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;
import pl.intelligent.finance.repository.ExpenditureRecordRepository;
import pl.intelligent.finance.service.IExpenditureRecordService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenditureRecordServiceDb implements IExpenditureRecordService {

    @Autowired
    private ExpenditureRecordRepository repository;

    @Override
    public IExpenditureRecord findByNameAndBankStatementId(String name, String bankStatementId) {
        return repository.findByNameAndBankStatementId(name, bankStatementId);
    }

    @Override
    public List<IExpenditureRecord> findByBankStatementId(String bankStatementId) {
        return repository.findByBankStatementId(bankStatementId).stream()
                .map(el -> (IExpenditureRecord) el)
                .collect(Collectors.toList());
    }

    @Override
    public IExpenditureRecord create(IExpenditureRecord entity) {
        return repository.save((ExpenditureRecord) entity);
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
