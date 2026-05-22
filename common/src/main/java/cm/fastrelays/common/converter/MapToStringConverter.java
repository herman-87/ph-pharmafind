package cm.fastrelays.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Converter(autoApply = true)
public class MapToStringConverter implements AttributeConverter<Map<String, String>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, String> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return null;
    }

    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      log.error("Error converting Map<String, String> to String: {}", e.getMessage());
      throw new IllegalArgumentException("Unable to convert Map<String, String> to JSON", e);
    }
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty()) {
      return new HashMap<>();
    }

    try {
      return objectMapper.readValue(dbData, new TypeReference<Map<String, String>>() {});
    } catch (JsonProcessingException e) {
      log.error(
              "Error converting String to Map<String, UUID>: {} - Data: {}", e.getMessage(), dbData);
      throw new IllegalArgumentException("Unable to convert JSON to Map", e);
    }
  }
}
