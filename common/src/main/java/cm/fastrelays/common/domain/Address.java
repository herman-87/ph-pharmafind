package cm.fastrelays.common.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
  private String street;
  private String city;
  private String district;
  private String country;
  private String postalCode;
}
