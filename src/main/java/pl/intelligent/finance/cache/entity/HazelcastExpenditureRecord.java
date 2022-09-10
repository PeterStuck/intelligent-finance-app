package pl.intelligent.finance.cache.entity;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.intelligent.finance.cache.serialization.CacheEntitySerializableFactory;
import pl.intelligent.finance.cache.serialization.CacheEntityType;
import pl.intelligent.finance.cache.serialization.SerializationUtil;
import pl.intelligent.finance.resource.entity.StorableExpenditureRecord;

import java.io.IOException;
import java.util.Objects;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HazelcastExpenditureRecord implements StorableExpenditureRecord, IdentifiedDataSerializable {

    private Long id;
    private String name;
    private Double amount;
    private Integer categoryId;
    private String bankStatementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getBankStatementId() {
        return bankStatementId;
    }

    public void setBankStatementId(String bankStatementId) {
        this.bankStatementId = bankStatementId;
    }

    public HazelcastExpenditureRecord getInstance() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HazelcastExpenditureRecord that = (HazelcastExpenditureRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(amount, that.amount) && Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(bankStatementId, that.bankStatementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, categoryId, bankStatementId);
    }

    @Override
    public int getFactoryId() {
        return CacheEntitySerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CacheEntityType.EXPENDITURE_RECORD_TYPE.getTypeId();
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        SerializationUtil.serializer(objectDataOutput)
                .writeLong(id)
                .writeString(name)
                .writeDouble(amount)
                .writeInteger(categoryId)
                .writeString(bankStatementId);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        var deserializer = SerializationUtil.deserializer(objectDataInput);

        id = deserializer.readLong();
        name = deserializer.readString();
        amount = deserializer.readDouble();
        categoryId = deserializer.readInteger();
        bankStatementId = deserializer.readString();
    }

}
