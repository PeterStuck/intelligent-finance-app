package pl.intelligent.finance.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.intelligent.finance.persistence.impl.BankStatement;

import java.util.List;

public interface BankStatementRepository extends JpaRepository<BankStatement, Integer> {

    @Query("select id from BankStatement")
    List<Integer> findAllIds();

}
