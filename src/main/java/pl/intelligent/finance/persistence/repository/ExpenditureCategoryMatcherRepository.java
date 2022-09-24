package pl.intelligent.finance.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import pl.intelligent.finance.persistence.entity.impl.ExpenditureCategoryMatcher;

public interface ExpenditureCategoryMatcherRepository extends CrudRepository<ExpenditureCategoryMatcher, Integer> {

}
