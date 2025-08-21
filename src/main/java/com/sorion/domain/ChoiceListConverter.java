// ChoiceListConverter.java
package com.sorion.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;

@Converter
public class ChoiceListConverter implements AttributeConverter<List<Choice>, String> {
    private static final ObjectMapper OM = new ObjectMapper();
    private static final TypeReference<List<Choice>> TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<Choice> attribute) {
        try {
            if (attribute == null || attribute.isEmpty()) return "[]";
            return OM.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("choices 직렬화 실패", e);
        }
    }

    @Override
    public List<Choice> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) return Collections.emptyList();
            String json = dbData.trim();

            // 이중 인코딩: "...." 형태면 한 번 더 해제
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = OM.readValue(json, String.class);
            }
            // 혹시 단일 객체가 저장된 케이스 {..} → [ {..} ]
            if (json.startsWith("{") && json.endsWith("}")) {
                return List.of(OM.readValue(json, Choice.class));
            }
            return OM.readValue(json, TYPE);
        } catch (Exception e) {
            throw new IllegalArgumentException("choices 역직렬화 실패: " + dbData, e);
        }
    }
}