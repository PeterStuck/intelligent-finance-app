package pl.intelligent.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.intelligent.finance.entity.impl.BankStatement;

public interface BankStatementRepository extends JpaRepository<BankStatement, String> {

}
