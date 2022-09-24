package pl.intelligent.finance.service;

public interface ServiceProvider {

    IExpenditureRecordService getExpenditureRecordService();

    IExpenditureCategoryService getExpenditureCategoryService();

    IExpenditureCategoryMatcherService getExpenditureCategoryMatcherService();

}
