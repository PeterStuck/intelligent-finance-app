package pl.intelligent.finance.cache.subsystem.impl.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

public abstract class MapLoaderBase<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger("MapLoaderLayerLogger");

    protected V loadWithExceptionHandler(Callable<V> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("Exception occurred", e);
        }
        return null;
    }

    protected Map<K, V> loadAllWithExceptionHandler(Callable<Map<K, V>> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("Exception occurred", e);
        }
        return null;
    }

    protected Iterable<K> loadAllKeysWithExceptionHandler(Callable<Iterable<K>> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("Exception occurred", e);
        }
        return null;
    }

    protected Logger logger() {
        return LOGGER;
    }

}
