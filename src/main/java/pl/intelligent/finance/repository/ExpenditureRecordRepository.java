package pl.intelligent.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.intelligent.finance.entity.impl.ExpenditureRecord;

import java.util.List;

public interface ExpenditureRecordRepository extends JpaRepository<ExpenditureRecord, Long> {

    @Query("SELECT id FROM ExpenditureRecord")
    List<Long> findAllIds();

    ExpenditureRecord findByNameAndBankStatementId(String name, String bankStatementId);

    List<ExpenditureRecord> findByBankStatementId(String bankStatementId);

}
