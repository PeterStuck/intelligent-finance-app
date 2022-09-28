package pl.intelligent.finance.persistence.service;

public interface ServiceProvider {

    IExpenditureRecordService getExpenditureRecordService();

    IExpenditureCategoryService getExpenditureCategoryService();

    IExpenditureCategoryMatcherService getExpenditureCategoryMatcherService();

}
