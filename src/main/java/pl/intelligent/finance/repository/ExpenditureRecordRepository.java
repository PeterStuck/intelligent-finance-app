package pl.intelligent.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;

import java.util.List;

public interface ExpenditureRecordRepository extends JpaRepository<ExpenditureRecord, Long> {

    ExpenditureRecord findByNameAndBankStatementId(String name, String bankStatementId);

    List<ExpenditureRecord> findByBankStatementId(String bankStatementId);

}
