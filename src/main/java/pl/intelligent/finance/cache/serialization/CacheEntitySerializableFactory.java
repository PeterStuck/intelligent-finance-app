package pl.intelligent.finance.cache.serialization;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import pl.intelligent.finance.cache.entity.HazelcastExpenditureRecord;

public class CacheEntitySerializableFactory implements DataSerializableFactory {

    public static final int FACTORY_ID = 1;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        CacheEntityType type = CacheEntityType.getTypeById(typeId);
        if (type == null) {
            return null;
        }

        return switch (type) {
            case EXPENDITURE_RECORD_TYPE -> new HazelcastExpenditureRecord();
            default -> null;
        };
    }
}
