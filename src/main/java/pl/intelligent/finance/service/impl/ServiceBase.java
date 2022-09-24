package pl.intelligent.finance.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class ServiceBase<I, E> {

    private static final Logger LOGGER = LoggerFactory.getLogger("ServiceDbLayerLogger");

    protected <T> T withExceptionHandler(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("Error occurred on service", e);
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

    protected Logger logger() {
        return LOGGER;
    }

}
