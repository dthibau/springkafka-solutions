package org.formation.model.serde;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPojoDeserializer<T> implements Deserializer<T>{

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> clazz;

    public JsonPojoDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de désérialisation", e);
        }
    }
}
