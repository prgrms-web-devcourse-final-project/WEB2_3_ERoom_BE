package com.example.eroom.domain.chat.converter;

import com.example.eroom.domain.entity.ColorInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ColorInfoConverter implements AttributeConverter<ColorInfo, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ColorInfo colorInfo) {
        try {
            return objectMapper.writeValueAsString(colorInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ColorInfo to JSON", e);
        }
    }

    @Override
    public ColorInfo convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, ColorInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to ColorInfo", e);
        }
    }
}
