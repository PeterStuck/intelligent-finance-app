package pl.intelligent.finance.cache.entity;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.intelligent.finance.cache.serialization.CacheEntitySerializableFactory;
import pl.intelligent.finance.cache.serialization.CacheEntityType;
import pl.intelligent.finance.cache.serialization.SerializationUtil;
import pl.intelligent.finance.resource.entity.ExpenditureCategoryMatcherType;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HazelcastExpenditureCategoryMatcher implements StorableExpenditureCategoryMatcher, IdentifiedDataSerializable {

    private Integer id;
    private String pattern;
    private ExpenditureCategoryMatcherType matcherType;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public ExpenditureCategoryMatcherType getMatcherType() {
        return matcherType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setMatcherType(ExpenditureCategoryMatcherType matcherType) {
        this.matcherType = matcherType;
    }

    @Override
    public int getFactoryId() {
        return CacheEntitySerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CacheEntityType.EXPENDITURE_CATEGORY_MATCHER_TYPE.getTypeId();
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        SerializationUtil.serializer(objectDataOutput)
                .writeInteger(id)
                .writeString(pattern)
                .writeInteger(Optional.ofNullable(matcherType).map(ExpenditureCategoryMatcherType::getId).orElse(null));
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        var deserializer = SerializationUtil.deserializer(objectDataInput);

        id = deserializer.readInteger();
        pattern = deserializer.readString();
        matcherType = ExpenditureCategoryMatcherType.valueOf(deserializer.readInteger());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HazelcastExpenditureCategoryMatcher that = (HazelcastExpenditureCategoryMatcher) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pattern, that.pattern) &&
                matcherType == that.matcherType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pattern, matcherType);
    }

    @Override
    public String toString() {
        return "HazelcastExpenditureCategoryMatcher{" +
                "id=" + id +
                ", pattern='" + pattern + '\'' +
                ", matcherType=" + matcherType +
                '}';
    }
}
