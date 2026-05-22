package cm.fastrelays.common.converter;

import cm.fastrelays.common.domain.PhoneNumber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class MapOfStringPhoneNumberToStringConverter
    implements AttributeConverter<Map<String, List<PhoneNumber>>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, List<PhoneNumber>> phones) {
    if (phones == null || phones.isEmpty()) {
      return null;
    }

    Map<String, List<String>> stringMap = new HashMap<>();
    for (Map.Entry<String, List<PhoneNumber>> entry : phones.entrySet()) {
      List<String> formattedNumbers =
          entry.getValue().stream()
              .map(phone -> phone.getCountryCode() + ";" + phone.getNumber())
              .collect(Collectors.toList());
      stringMap.put(entry.getKey(), formattedNumbers);
    }

    try {
      return objectMapper.writeValueAsString(stringMap);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting phones to JSON", e);
    }
  }

  @Override
  public Map<String, List<PhoneNumber>> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return new HashMap<>();
    }

    try {
      TypeReference<Map<String, List<String>>> typeRef = new TypeReference<>() {};
      Map<String, List<String>> stringMap = objectMapper.readValue(dbData, typeRef);

      Map<String, List<PhoneNumber>> phonesMap = new HashMap<>();
      for (Map.Entry<String, List<String>> entry : stringMap.entrySet()) {
        List<PhoneNumber> phoneNumbers =
            entry.getValue().stream()
                .map(
                    formattedString -> {
                      String[] parts = formattedString.split(";");
                      if (parts.length != 2) {
                        throw new IllegalArgumentException(
                            "Invalid phone format: " + formattedString);
                      }
                      return new PhoneNumber(parts[0], parts[1]);
                    })
                .collect(Collectors.toList());
        phonesMap.put(entry.getKey(), phoneNumbers);
      }
      return phonesMap;

    } catch (IOException e) {
      throw new IllegalArgumentException("Error converting JSON to phones: " + dbData, e);
    }
  }
}
