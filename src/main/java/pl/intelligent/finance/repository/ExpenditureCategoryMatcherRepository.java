package pl.intelligent.finance.repository;

import org.springframework.data.repository.CrudRepository;
import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcher;

public interface ExpenditureCategoryMatcherRepository extends CrudRepository<ExpenditureCategoryMatcher, Integer> {

}
