package pl.intelligent.finance.cache.serialization;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.concurrent.Callable;

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
