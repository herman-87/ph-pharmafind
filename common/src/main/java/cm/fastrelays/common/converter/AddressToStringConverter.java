package cm.fastrelays.common.converter;

import cm.fastrelays.common.domain.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class AddressToStringConverter implements AttributeConverter<Address, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Address address) {
    if (address == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(address);
    } catch (JsonProcessingException e) {
      log.error("Error converting Address to String", e);
      throw new IllegalArgumentException("Error converting Address to String", e);
    }
  }

  @Override
  public Address convertToEntityAttribute(String s) {
    if (s == null || s.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.readValue(s, Address.class);
    } catch (JsonProcessingException e) {
      log.error("Error converting String to Address", e);
      throw new IllegalArgumentException("Error converting String to Address", e);
    }
  }
}
