package pl.intelligent.finance.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class ServiceBase<I, E> {

    protected <T> T withException(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        } catch (Exception e) {
            // TODO add logging
            throw e;
        }
    }

    // repository requires entity interface on repo methods,
    // but we need to hide actual implementation on service layer
    protected List<I> mapToInterface(List<E> entities) {
        return entities.stream()
                .map(el -> (I) el)
                .collect(Collectors.toList());
    }

    protected List<E> mapToEntity(List<I> entities) {
        return entities.stream()
                .map(el -> (E) el)
                .collect(Collectors.toList());
    }

}
