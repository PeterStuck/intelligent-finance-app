package pl.intelligent.finance.service.provider;

import org.springframework.beans.factory.annotation.Autowired;
import pl.intelligent.finance.service.IExpenditureRecordService;

public class ServiceDbProvider implements ServiceProvider {

    @Autowired
    private IExpenditureRecordService expenditureRecordService;

    @Override
    public IExpenditureRecordService getExpenditureRecordService() {
        return expenditureRecordService;
    }

}
