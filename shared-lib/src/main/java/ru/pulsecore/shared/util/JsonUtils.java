package ru.pulsecore.shared.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
    private static ObjectMapper MAPPER;

    public JsonUtils(ObjectMapper objectMapper) {
        JsonUtils.MAPPER = objectMapper;
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка десериализации JSON в " + type.getSimpleName(), e);
        }
    }

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации в JSON", e);
        }
    }
}