package pl.intelligent.finance.cache.subsystem.impl.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import pl.intelligent.finance.cache.subsystem.exception.ExceptionUtil;
import pl.intelligent.finance.cache.subsystem.exception.InvalidDataException;
import pl.intelligent.finance.persistence.service.ServiceProvider;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class HazelcastCacheBase<T, E> {

    protected static final String CACHE_NAME_PREFIX = "if-";
    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastCacheBase.class);
    private static final Logger CACHE_LOGGER = LoggerFactory.getLogger("CacheLayerLogger");

    protected ServiceProvider serviceProvider;

    public HazelcastCacheBase(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    protected CacheEntityWithException<T> attemptOperation(Callable<T> cacheFunction) {
        InvalidDataException exception = null;
        T cacheEntity = null;

        try {
            cacheEntity = cacheFunction.call();
        } catch (Exception e) {
            LOGGER.error("Error occurred during attemptOperation", e);
            if (e instanceof DataAccessException) {
                exception = ExceptionUtil.dataIntegrityError();
            } else if (e instanceof InvalidDataException) {
                exception = (InvalidDataException) e;
            } else {
                exception = ExceptionUtil.unexpectedError(e.getMessage());
            }
        }

        return new CacheEntityWithException<>(cacheEntity, exception);
    }

    protected CacheEntityListWithException<T> attemptBatchOperation(Callable<List<T>> cacheFunction) {
        InvalidDataException exception = null;
        List<T> cacheEntities = null;

        try {
            cacheEntities = cacheFunction.call();
        } catch (Exception e) {
            LOGGER.error("Error occurred during attemptBatchOperation", e);
            if (e instanceof DataAccessException) {
                exception = ExceptionUtil.dataIntegrityError();
            } else if (e instanceof InvalidDataException) {
                exception = (InvalidDataException) e;
            } else {
                exception = ExceptionUtil.unexpectedError(e.getMessage());
            }
        }

        return new CacheEntityListWithException<>(cacheEntities, exception);
    }

    protected record CacheEntityListWithException<T>(List<T> entities, InvalidDataException exception) {
        boolean isError() {
            return exception != null;
        }
    }

    protected record CacheEntityWithException<T>(T cacheEntity, InvalidDataException exception) {
        boolean isError() {
            return exception != null;
        }
    }

    protected abstract T getConvertedCacheEntity(E entity);

    protected abstract E getConvertedDbEntity(T entity);

    protected Logger logger() {
        return CACHE_LOGGER;
    }

}
