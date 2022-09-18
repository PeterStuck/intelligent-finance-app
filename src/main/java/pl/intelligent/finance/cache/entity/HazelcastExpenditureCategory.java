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
import pl.intelligent.finance.resource.entity.StorableExpenditureCategory;
import pl.intelligent.finance.resource.entity.StorableExpenditureCategoryMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HazelcastExpenditureCategory implements StorableExpenditureCategory, IdentifiedDataSerializable {

    private Integer id;
    private String name;
    private Integer parentCategoryId;
    private List<? extends StorableExpenditureCategoryMatcher> matchers;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    @Override
    public List<? extends StorableExpenditureCategoryMatcher> getMatchers() {
        return matchers;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public void setMatchers(List<? extends StorableExpenditureCategoryMatcher> matchers) {
        this.matchers = matchers;
    }

    public HazelcastExpenditureCategory getInstance() {
        return this;
    }

    @Override
    public int getFactoryId() {
        return CacheEntitySerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CacheEntityType.EXPENDITURE_CATEGORY_TYPE.getTypeId();
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        SerializationUtil.serializer(objectDataOutput)
                .writeInteger(id)
                .writeString(name)
                .writeInteger(parentCategoryId)
                .writeDataSerializable(Optional.ofNullable(matchers)
                        .map(m -> m.stream()
                                .map(el -> (IdentifiedDataSerializable) el)
                                .collect(Collectors.toList()))
                        .orElse(null));
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        var deserializer = SerializationUtil.deserializer(objectDataInput);

        id = deserializer.readInteger();
        name = deserializer.readString();
        parentCategoryId = deserializer.readInteger();
        matchers = Optional.ofNullable(deserializer.readDataSerializables(HazelcastExpenditureCategoryMatcher::new))
                        .map(m -> m.stream()
                                .map(matcher -> (StorableExpenditureCategoryMatcher) matcher)
                                .collect(Collectors.toList()))
                        .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HazelcastExpenditureCategory that = (HazelcastExpenditureCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(parentCategoryId, that.parentCategoryId) &&
                Objects.equals(matchers, that.matchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentCategoryId, matchers);
    }

    @Override
    public String toString() {
        return "HazelcastExpenditureCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                ", matchers=" + matchers +
                '}';
    }
}
