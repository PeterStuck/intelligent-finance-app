package pl.intelligent.finance.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.intelligent.finance.persistence.entity.impl.BankStatement;

public interface BankStatementRepository extends JpaRepository<BankStatement, String> {

}
