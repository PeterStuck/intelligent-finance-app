package pl.intelligent.finance.persistence.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.intelligent.finance.persistence.impl.BankStatement;
import pl.intelligent.finance.persistence.entity.IBankStatement;
import pl.intelligent.finance.persistence.repository.BankStatementRepository;
import pl.intelligent.finance.persistence.repository.ExpenditureRecordRepository;
import pl.intelligent.finance.persistence.service.IBankStatementService;

import java.util.Collection;
import java.util.List;

@Service
public class BankStatementServiceDb extends ServiceBase<IBankStatement, BankStatement> implements IBankStatementService {

    @Autowired
    private BankStatementRepository bsRepository;

    @Autowired
    private ExpenditureRecordRepository recordRepository;

    @Override
    public IBankStatement findById(Integer id) {
        return bsRepository.findById(id).orElse(null);
    }

    @Override
    public List<IBankStatement> findByIds(Collection<Integer> ids) {
        return mapToInterface(bsRepository.findAllById(ids));
    }

    @Override
    public List<Integer> findAllIds() {
        return bsRepository.findAllIds();
    }

    @Override
    public IBankStatement createInstance() {
        return new BankStatement();
    }

    @Override
    public IBankStatement create(IBankStatement bankStatement) throws Exception {
        return withExceptionHandler(() -> {
            logger().debug("Persisting new Bank Statement: {}", bankStatement);

            var persistedBs = bsRepository.saveAndFlush((BankStatement) bankStatement);
            logger().debug("Bank statement: {} was persisted successfully", bankStatement);

            return persistedBs;
        });
    }

    @Transactional
    @Override
    public void deleteById(Integer bsId, boolean forceRemove) {
        if (forceRemove) {
            var records = recordRepository.findByBankStatementId(bsId);
            recordRepository.deleteAll(records);
        }

        bsRepository.deleteById(bsId);
        bsRepository.flush();
        recordRepository.flush();
    }

    @Transactional
    @Override
    public void delete(IBankStatement bankStatement, boolean forceRemove) {
        if (forceRemove) {
            var records = recordRepository.findByBankStatementId(bankStatement.getId());
            recordRepository.deleteAll(records);
            recordRepository.flush();
        }

        bsRepository.delete((BankStatement) bankStatement);
    }
}
