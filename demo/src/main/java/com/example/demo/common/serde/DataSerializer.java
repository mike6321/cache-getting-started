package com.example.demo.common.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeOrException(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("Serialization failed for data: {}", data, e);
            throw new RuntimeException("Serialization failed", e);
        }
    }

    public static <T> T deserializeOrNull(String data, Class<T> valueType) {
        try {
            return objectMapper.readValue(data, valueType);
        } catch (Exception e) {
            log.error("Deserialization failed for data: {}", data, e);
            throw new RuntimeException("Deserialization failed", e);
        }
    }

}
