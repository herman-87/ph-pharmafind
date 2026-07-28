package com.ph.backoffice.domain.certifications.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class OpeningHourListConverter implements AttributeConverter<List<OpeningHour>, String> {

  private static final ObjectMapper MAPPER =
      new ObjectMapper().registerModule(new JavaTimeModule());

  @Override
  public String convertToDatabaseColumn(List<OpeningHour> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return null;
    }
    try {
      return MAPPER.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting OpeningHour list to JSON", e);
    }
  }

  @Override
  public List<OpeningHour> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty()) {
      return new ArrayList<>();
    }
    try {
      return MAPPER.readValue(dbData, new TypeReference<List<OpeningHour>>() {});
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting JSON to OpeningHour list", e);
    }
  }
}
