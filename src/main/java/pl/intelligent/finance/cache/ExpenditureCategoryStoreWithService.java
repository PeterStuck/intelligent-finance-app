package pl.intelligent.finance.cache;

import pl.intelligent.finance.service.provider.ServiceProvider;

public interface ExpenditureCategoryStoreWithService extends ExpenditureCategoryStore {

    ServiceProvider getServiceProvider();

}
