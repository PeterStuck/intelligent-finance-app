package pl.intelligent.finance.service.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.intelligent.finance.service.IExpenditureRecordService;

@Component
public class ServiceDbProvider implements ServiceProvider {

    @Autowired
    private IExpenditureRecordService expenditureRecordService;

    @Override
    public IExpenditureRecordService getExpenditureRecordService() {
        return expenditureRecordService;
    }

}
