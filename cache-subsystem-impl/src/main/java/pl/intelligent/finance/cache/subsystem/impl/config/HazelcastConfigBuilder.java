package pl.intelligent.finance.cache.subsystem.impl.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.HazelcastInstance;
import pl.intelligent.finance.cache.subsystem.impl.impl.HazelcastExpenditureCategoryCache;
import pl.intelligent.finance.cache.subsystem.impl.impl.HazelcastExpenditureRecordCache;
import pl.intelligent.finance.cache.subsystem.impl.loader.ExpenditureCategoryMapLoader;
import pl.intelligent.finance.cache.subsystem.impl.loader.ExpenditureRecordMapLoader;
import pl.intelligent.finance.persistence.service.ServiceProvider;

public class HazelcastConfigBuilder {

    public static void initialize(HazelcastInstance hazelcastInstance, ServiceProvider serviceProvider) {
        if (hazelcastInstance == null) {
            return;
        }

        Config config = hazelcastInstance.getConfig();

        config.getMapConfig(HazelcastExpenditureRecordCache.CACHE_NAME)
                .getMapStoreConfig()
                .setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY)
                .setImplementation(new ExpenditureRecordMapLoader(serviceProvider.getExpenditureRecordService()));

        config.getMapConfig(HazelcastExpenditureCategoryCache.CACHE_NAME)
                .getMapStoreConfig()
                .setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY)
                .setImplementation(new ExpenditureCategoryMapLoader(serviceProvider.getExpenditureCategoryService()));
    }

}
