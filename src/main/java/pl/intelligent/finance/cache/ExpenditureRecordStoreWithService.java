package pl.intelligent.finance.cache;

import pl.intelligent.finance.service.provider.ServiceProvider;

public interface ExpenditureRecordStoreWithService extends ExpenditureRecordStore {

    ServiceProvider getServiceProvider();

}
