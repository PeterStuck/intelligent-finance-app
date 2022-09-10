package pl.intelligent.finance.cache.impl;

import org.springframework.dao.DataAccessException;
import pl.intelligent.finance.exception.ExceptionUtil;
import pl.intelligent.finance.exception.InvalidDataException;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class HazelcastCacheBase<T> {

    protected CacheEntityWithException<T> attemptOperation(Callable<T> cacheFunction) {
        InvalidDataException exception = null;
        T cacheEntity = null;

        try {
            cacheEntity = cacheFunction.call();
        } catch (Exception e) {
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

}
