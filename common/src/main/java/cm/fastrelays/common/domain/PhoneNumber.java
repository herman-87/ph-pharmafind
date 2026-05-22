package cm.fastrelays.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhoneNumber {
  private String countryCode;
  private String number;

  @Override
  public String toString() {
    return countryCode.concat("-").concat(number);
  }
}
