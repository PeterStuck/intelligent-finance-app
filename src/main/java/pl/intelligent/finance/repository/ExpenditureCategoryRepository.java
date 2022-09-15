package pl.intelligent.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.intelligent.finance.entity.impl.ExpenditureCategory;

import java.util.List;

public interface ExpenditureCategoryRepository extends JpaRepository<ExpenditureCategory, Integer> {

    @Query("SELECT id FROM ExpenditureCategory")
    List<Integer> findAllIds();

    ExpenditureCategory findByName(String name);

}
