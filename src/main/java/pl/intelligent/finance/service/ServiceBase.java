package pl.intelligent.finance.service;

import java.util.concurrent.Callable;

public abstract class ServiceBase {

    protected  <T> T withException(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        } catch (Exception e) {
            // TODO add logging
            throw e;
        }

    }

}
