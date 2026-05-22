package cm.fastrelays.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class ListStringToStringConverter implements AttributeConverter<List<String>, String> {
  @Override
  public String convertToDatabaseColumn(List<String> stringList) {
    if (stringList == null || stringList.isEmpty()) {
      return null;
    }
    return stringList.toString();
  }

  @Override
  public List<String> convertToEntityAttribute(String value) {
    if (value == null) {
      return Collections.emptyList();
    }
    return Arrays.stream(value.split(";")).toList();
  }
}
