package pl.intelligent.finance.service.impl;

import org.springframework.stereotype.Service;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;
import pl.intelligent.finance.entity.impl.ExpenditureCategoryMatcher;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;

@Service
public class ExpenditureCategoryMatcherServiceDb implements IExpenditureCategoryMatcherService {

    @Override
    public IExpenditureCategoryMatcher createInstance() {
        return new ExpenditureCategoryMatcher();
    }

}
