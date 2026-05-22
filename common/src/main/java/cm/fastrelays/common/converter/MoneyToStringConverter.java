package cm.fastrelays.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.javamoney.moneta.Money;

@Converter(autoApply = true)
public class MoneyToStringConverter implements AttributeConverter<Money, String> {
  @Override
  public String convertToDatabaseColumn(Money money) {
    return money == null ? null : money.toString();
  }

  @Override
  public Money convertToEntityAttribute(String s) {
    return s == null ? null : Money.parse(s);
  }
}
