package pl.intelligent.finance.service.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;
import pl.intelligent.finance.service.IExpenditureRecordService;
import pl.intelligent.finance.service.ServiceProvider;

@Component
public class ServiceDbProvider implements ServiceProvider {

    private final IExpenditureRecordService expenditureRecordService;
    private final IExpenditureCategoryService expenditureCategoryService;
    private final IExpenditureCategoryMatcherService expenditureCategoryMatcherService;

    @Autowired
    public ServiceDbProvider(IExpenditureRecordService expenditureRecordService,
                             IExpenditureCategoryService expenditureCategoryService,
                             IExpenditureCategoryMatcherService expenditureCategoryMatcherService) {
        this.expenditureRecordService = expenditureRecordService;
        this.expenditureCategoryService = expenditureCategoryService;
        this.expenditureCategoryMatcherService = expenditureCategoryMatcherService;
    }

    @Override
    public IExpenditureRecordService getExpenditureRecordService() {
        return expenditureRecordService;
    }

    @Override
    public IExpenditureCategoryService getExpenditureCategoryService() {
        return expenditureCategoryService;
    }

    @Override
    public IExpenditureCategoryMatcherService getExpenditureCategoryMatcherService() {
        return expenditureCategoryMatcherService;
    }

}
