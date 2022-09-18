package pl.intelligent.finance.cache.serialization;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class SerializationUtil {

    public static Serializer serializer(ObjectDataOutput objectDataOutput) {
        return new Serializer(objectDataOutput);
    }

    public static Deserializer deserializer(ObjectDataInput objectDataInput) {
        return new Deserializer(objectDataInput);
    }

    public record Serializer(ObjectDataOutput objectDataOutput) {

        public Serializer writeLong(Long param) throws IOException {
            if (param != null) {
                objectDataOutput.writeBoolean(true);
                objectDataOutput.writeLong(param);
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }

        public Serializer writeString(String param) throws IOException {
            if (param != null) {
                objectDataOutput.writeBoolean(true);
                objectDataOutput.writeString(param);
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }

        public Serializer writeDouble(Double param) throws IOException {
            if (param != null) {
                objectDataOutput.writeBoolean(true);
                objectDataOutput.writeDouble(param);
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }

        public Serializer writeInteger(Integer param) throws IOException {
            if (param != null) {
                objectDataOutput.writeBoolean(true);
                objectDataOutput.writeInt(param);
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }

        public Serializer writeObject(Object param) throws IOException {
            if (param != null) {
                objectDataOutput.writeBoolean(true);
                objectDataOutput.writeObject(param);
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }

        public Serializer writeDataSerializable(List<IdentifiedDataSerializable> dataSerializables) throws IOException {
            if (dataSerializables != null) {
                objectDataOutput.writeBoolean(true);
                objectDataOutput.writeInt(dataSerializables.size());
                for (IdentifiedDataSerializable dataSerializable : dataSerializables) {
                    this.writeDataSerializable(dataSerializable);
                }
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }

        public Serializer writeDataSerializable(IdentifiedDataSerializable dataSerializable) throws IOException {
            if (dataSerializable != null) {
                objectDataOutput.writeBoolean(true);
                dataSerializable.writeData(objectDataOutput);
            } else {
                objectDataOutput.writeBoolean(false);
            }

            return this;
        }
    }

    public record Deserializer(ObjectDataInput objectDataInput) {

        public Long readLong() throws IOException {
            return (Long) deserializeValue(objectDataInput::readLong);
        }

        public String readString() throws IOException {
            return (String) deserializeValue(objectDataInput::readString);
        }

        public Double readDouble() throws IOException {
            return (Double) deserializeValue(objectDataInput::readDouble);
        }

        public Integer readInteger() throws IOException {
            return (Integer) deserializeValue(objectDataInput::readInt);
        }

        public Object readObject() throws IOException {
            return deserializeValue(objectDataInput::readObject);
        }

        public List<IdentifiedDataSerializable> readDataSerializables(Supplier<IdentifiedDataSerializable> entityCreateCall) throws IOException {
            if (objectDataInput.readBoolean()) {
                int size = objectDataInput.readInt();
                List<IdentifiedDataSerializable> entities = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    entities.add(readDataSerializable(entityCreateCall.get()));
                }

                return entities;
            }

            return null;
        }

        public IdentifiedDataSerializable readDataSerializable(IdentifiedDataSerializable obj) throws IOException {
            return (IdentifiedDataSerializable) deserializeValue(() -> {
                obj.readData(objectDataInput);
                return obj;
            });
        }

        private Object deserializeValue(Callable<Object> callable) throws IOException {
            try {
                if (objectDataInput.readBoolean()) {
                    return callable.call();
                }
            } catch (Exception e) {
                throw new IOException(e);
            }

            return null;
        }

    }

}
