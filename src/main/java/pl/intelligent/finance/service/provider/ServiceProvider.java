package pl.intelligent.finance.service.provider;

import pl.intelligent.finance.service.IExpenditureCategoryMatcherService;
import pl.intelligent.finance.service.IExpenditureCategoryService;
import pl.intelligent.finance.service.IExpenditureRecordService;

public interface ServiceProvider {

    IExpenditureRecordService getExpenditureRecordService();

    IExpenditureCategoryService getExpenditureCategoryService();

    IExpenditureCategoryMatcherService getExpenditureCategoryMatcherService();

}
