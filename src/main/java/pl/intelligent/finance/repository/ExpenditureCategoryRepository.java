package pl.intelligent.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.intelligent.finance.entity.impl.ExpenditureCategory;

public interface ExpenditureCategoryRepository extends JpaRepository<ExpenditureCategory, Integer> {
}
